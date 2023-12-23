package repo.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleConnection implements AutoCloseable {
    public interface DoWithConnection<T> {
        T use(Connection conn) throws Exception;
    }
    private Connection conn;
    synchronized void reset() throws SQLException {
        if(conn != null && conn.isClosed()) conn.close();
        conn = DriverManager.getConnection(url, username, password);
    }
    private final String url;
    private final String username;
    private final String password;
    private final boolean isQueuing;
    private final ReentrantLock lock = new ReentrantLock();
    public SimpleConnection(String url, String username, String password, boolean isQueuing) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.isQueuing = isQueuing;
        reset();
    }
    @Override
    public synchronized void close() throws Exception {
        if(conn != null && conn.isClosed())
            conn.close();
    }

    public<T> T doStuff(DoWithConnection<T> func) throws Exception {
        if(isQueuing) {
            lock.lock();
            try {
                return func.use(conn);
            }
            finally {
                lock.unlock();
            }
        }
        else return func.use(this.conn);
    }
}
