package wtf.n1zamu.database.impl;

import wtf.n1zamu.NCases;
import wtf.n1zamu.database.DataBase;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDataBase implements DataBase {
    @Override
    public Connection getConnection() throws SQLException {
        File dataFile = new File(NCases.getInstance().getDataFolder(), "data.db");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                boolean isCreated = dataFile.createNewFile();
                if (isCreated) {
                    Bukkit.getLogger().info("[NCases] SQLite База Данных успешно создана!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return DriverManager.getConnection("jdbc:sqlite:plugins/NCases/data.db");
    }
}
