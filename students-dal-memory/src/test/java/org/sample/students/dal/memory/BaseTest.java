package org.sample.students.dal.memory;

import org.sample.students.dal.memory.datastore.Connection;
import org.sample.students.dal.memory.datastore.Datastore;
import org.junit.After;
import org.junit.Before;

public class BaseTest {
    protected Datastore datastore;
    @Before
    public void setUp() {
        Connection connection = Connection.getConnection();
        datastore = connection.getDatastore();
    }

    @After
    public void tearDown() {
        if (datastore != null) {
            datastore.empty();
        }
    }
}
