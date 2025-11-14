package wtf.n1zamu.database.impl;

import wtf.n1zamu.NCases;
import wtf.n1zamu.database.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDataBase implements DataBase {
    @Override
    public Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://"
                + NCases.getInstance().getConfig().getString("host") + ":"
                + NCases.getInstance().getConfig().getInt("port") + "/"
                + NCases.getInstance().getConfig().getString("schema") + "?useSSL=false&serverTimezone=UTC";

        String user = NCases.getInstance().getConfig().getString("username");
        String password = NCases.getInstance().getConfig().getString("password");

        return DriverManager.getConnection(url, user, password);
    }
}
