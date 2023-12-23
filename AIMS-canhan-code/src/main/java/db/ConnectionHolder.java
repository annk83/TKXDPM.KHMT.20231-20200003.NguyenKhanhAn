package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHolder {
    private final String username;
    private final String database;
    private final String password;
    private Connection conn;
    public ConnectionHolder(String username, String database, String password) {
        this.username = username;
        this.database = database;
        this.password = password;
    }

    public Connection checkAndGetConnection() throws SQLException {
        while(conn == null || conn.isClosed())
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/"+database, username, password);
        return conn;
    }
}
