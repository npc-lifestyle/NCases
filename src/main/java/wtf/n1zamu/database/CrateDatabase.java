package wtf.n1zamu.database;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import rip.jnic.nativeobfuscator.Native;
import wtf.n1zamu.NCases;
import wtf.n1zamu.database.impl.MySQLDataBase;
import wtf.n1zamu.database.impl.SQLiteDataBase;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CrateDatabase {
    private final Connection conn;
    private final Cache<String, Integer> recordCache;
    private final boolean isSQLite;
    private final String tableName = "crates";

    @Native
    public CrateDatabase() {
        DataBase dataBase = getDataBaseByName(NCases.getInstance().getConfig().getString("dataBaseType"));
        this.isSQLite = dataBase instanceof SQLiteDataBase;

        try {
            conn = dataBase.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.recordCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .initialCapacity(1000)
                .expireAfterWrite(60000, TimeUnit.MILLISECONDS)
                .build();

        createTable();
        migrateOldKeys();
    }

    private void createTable() {
        try (Statement stmt = conn.createStatement()) {
            String sql;
            if (isSQLite) {
                sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "player TEXT NOT NULL, " +
                        "case_name TEXT NOT NULL, " +
                        "amount INTEGER NOT NULL, " +
                        "PRIMARY KEY (player, case_name)" +
                        ");";
            } else {
                sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "player VARCHAR(255) NOT NULL, " +
                        "case_name VARCHAR(255) NOT NULL, " +
                        "amount INT NOT NULL, " +
                        "PRIMARY KEY (player, case_name)" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
            }
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void migrateOldKeys() {
        String sql;
        if (!tableExists("crate_keys")) {
            Bukkit.getLogger().info("Таблица crate_keys не найдена, миграция пропущена.");
            return;
        }
        if (isSQLite) {
            sql = "INSERT OR REPLACE INTO " + tableName + " (player, case_name, amount) " +
                    "SELECT player, case_name, amount FROM crate_keys";
        } else {
            sql = "INSERT INTO " + tableName + " (player, case_name, amount) " +
                    "SELECT player, case_name, amount FROM crate_keys " +
                    "ON DUPLICATE KEY UPDATE amount = VALUES(amount)";
        }

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            Bukkit.getLogger().info("Миграция данных из crate_keys в " + tableName + " завершена успешно.");
            stmt.executeUpdate("DROP TABLE IF EXISTS crate_keys");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean tableExists(String tableName) {
        try {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, null, tableName, null)) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Native
    public Map<String, Integer> getAllKeys(String playerName) {
        Map<String, Integer> hashMap = new HashMap<>();
        String sql = "SELECT case_name, amount FROM " + tableName + " WHERE player = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, playerName.toUpperCase());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String caseName = rs.getString("case_name");
                int amount = rs.getInt("amount");
                hashMap.put(caseName, amount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    @Native
    public int getKeys(String playerName, String caseName) {
        String cacheKey = playerName + "-" + caseName;
        Integer cached = recordCache.getIfPresent(cacheKey);
        if (cached != null) return cached;

        String sql = "SELECT amount FROM " + tableName + " WHERE player = ? AND case_name = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, playerName.toUpperCase());
            preparedStatement.setString(2, caseName);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int amount = rs.getInt("amount");
                recordCache.put(cacheKey, amount);
                return amount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Native
    public void updateKeyAmount(String playerName, String caseName, int amount) {
        recordCache.put(playerName + "-" + caseName, amount);
        String upsertSQL;
        if (isSQLite) {
            upsertSQL = "INSERT INTO " + tableName + " (player, case_name, amount) VALUES (?, ?, ?) " +
                    "ON CONFLICT(player, case_name) DO UPDATE SET amount = excluded.amount";
        } else {
            upsertSQL = "INSERT INTO " + tableName + " (player, case_name, amount) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE amount = VALUES(amount)";
        }
        Runnable runnable = () -> {
            try {
                if (amount == 0) {
                    String deleteSQL = "DELETE FROM " + tableName + " WHERE player = ? AND case_name = ?";
                    try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
                        ps.setString(1, playerName.toUpperCase());
                        ps.setString(2, caseName);
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = conn.prepareStatement(upsertSQL)) {
                        ps.setString(1, playerName.toUpperCase());
                        ps.setString(2, caseName);
                        ps.setInt(3, amount);
                        ps.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        if (isSQLite) {
            runnable.run();
            return;
        }
        CompletableFuture.runAsync(runnable);
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DataBase getDataBaseByName(String name) {
        Map<String, DataBase> dataBaseMap = new HashMap<>();
        dataBaseMap.put("SQLite", new SQLiteDataBase());
        dataBaseMap.put("MySQL", new MySQLDataBase());
        return dataBaseMap.getOrDefault(name, null);
    }
}
