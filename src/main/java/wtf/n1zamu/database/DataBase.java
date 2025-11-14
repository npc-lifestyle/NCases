package wtf.n1zamu.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataBase {
    Connection getConnection() throws SQLException;
}
