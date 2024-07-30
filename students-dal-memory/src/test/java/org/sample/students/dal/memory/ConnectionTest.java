package org.sample.students.dal.memory;

import org.sample.students.dal.memory.datastore.Connection;
import org.sample.students.dal.memory.datastore.Datastore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionTest {

    @Test
    public void testCanRetrieveConnectionFromConnectionClass() {
        Connection connection = Connection.getConnection();
        assert connection != null;
        connection.close();
    }

    @Test
    public void testCanRetrieveOnlyAMaximumNumberOfConnection() {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Runnable runnable = () -> {
            Connection connection = Connection.getConnection();
            try {
                Thread.sleep(1);
            }catch (Exception ignored) {
            }finally {
                connection.close();
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i=0; i<50; i++) {
            executorService.submit(runnable);
            assert Connection.concurrentConnections <= 100;
        }

        executorService.shutdown();
    }

    @Test
    public void testPermitsAreDecrementedPerConnection() {
        int lastAvailablePermits = Connection.getAvailablePermits();
        List<Connection> connections = new ArrayList<>();

        for(int i=0; i<5; i++) {
            connections.add(Connection.getConnection());
            assert Connection.getAvailablePermits() == lastAvailablePermits-1;
            lastAvailablePermits = Connection.getAvailablePermits();
        }

        connections.forEach(Connection::close);
    }

    @Test
    public void testPermitsAreIncrementedAfterClosingConnection() {
        int lastAvailablePermits = Connection.getAvailablePermits();
        List<Connection> connections = new ArrayList<>();

        for(int i=0; i<2; i++) {
            connections.add(Connection.getConnection());
            lastAvailablePermits = Connection.getAvailablePermits();
        }

        for(int i=0; i<2; i++) {
            connections.get(i).close();
            assert Connection.getAvailablePermits() == lastAvailablePermits + 1;
            lastAvailablePermits = Connection.getAvailablePermits();
        }
    }

    @Test
    public void testConnectionIsAliveAtTimeOfCreation() {
        Connection connection = Connection.getConnection();
        assert connection.getAlive();
        connection.close();
    }

    @Test
    public void testConnectionIsNotAliveAfterClosed() {
        Connection connection = Connection.getConnection();
        connection.close();
        assert !connection.getAlive();
    }

    @Test
    public void testCanAccessDatastoreUsingOpenedConnection() {
        Connection connection = Connection.getConnection();
        Datastore datastore = connection.getDatastore();

        assert datastore != null;
    }

    @Test public void testCanNotAccessDatastoreUsingClosedConnection() {
        Connection connection = Connection.getConnection();
        connection.close();
        Datastore datastore = connection.getDatastore();

        assert datastore == null;
    }
}
