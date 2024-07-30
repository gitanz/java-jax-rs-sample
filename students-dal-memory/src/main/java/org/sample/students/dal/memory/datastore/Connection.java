package org.sample.students.dal.memory.datastore;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Connection {
    public static Integer concurrentConnections = 0;
    public static Map<String, Connection> allConnections = new HashMap<>();
    private final static Semaphore permit = new Semaphore(100, true);
    private String id;
    private boolean alive;

    private Connection() {
    }

    public boolean getAlive() {
        return alive;
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            permit.acquire();

            connection = new Connection();
            connection.id = UUID.randomUUID().toString();
            connection.alive = true;

            allConnections.put(connection.id, connection);

            concurrentConnections++;
        } catch (Exception e) {
            permit.release();
        }

        return connection;
    }

    public static boolean purgeAllConnections() {
        allConnections.forEach(((connection_id, connection) -> {
            if (connection != null) {
                connection.close();
            }
        }));

        return true;
    }

    public Datastore getDatastore() {
        if (this.alive) {
            return new Datastore(this);
        }

        return null;
    }

    public void close() {
        permit.release();
        this.alive = false;
        allConnections.remove(this.id);
        concurrentConnections--;
    }

    public static Integer getAvailablePermits() {
        return permit.availablePermits();
    }
}
