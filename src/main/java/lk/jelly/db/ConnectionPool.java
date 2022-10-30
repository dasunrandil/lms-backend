package lk.jelly.db;

import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {

    private List<Connection> pool = new ArrayList<>();
    private List<Connection> consumerPool = new ArrayList<>();

    public ConnectionPool (int poolSize){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep9", "root", "Dasun@1996");
                pool.add(connection);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Connection getConnection(){
        while (pool.isEmpty()){     //if -> while to check again if a spurious wakeup happens
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Connection connection = pool.get(0);
        consumerPool.add(connection);
        pool.remove(connection);
        return connection;
    }

    public synchronized void releaseConnection(Connection connection){
        pool.add(connection);
        consumerPool.remove(connection);
        notify();
    }

    public synchronized void releaseAllConnections(){
        pool.addAll(consumerPool);
        consumerPool.clear();
        notifyAll();
    }
}


