package org.sample.students.dal.memory;

import org.sample.students.dal.memory.datastore.Connection;
import org.sample.students.dal.memory.datastore.Datastore;
import org.sample.students.dal.memory.models.OperationStatus;
import org.sample.students.dal.model.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DatastoreTest {

    @Test
    public void testCanAccessDatastoreUsingConnection() {
        Connection connection = Connection.getConnection();
        Datastore datastore = connection.getDatastore();

        assert  datastore != null;
        connection.close();
    }

    @Test
    public void testCanInsertStudentObjectIntoDatastore() {
        Connection connection = Connection.getConnection();
        Datastore datastore = connection.getDatastore();
        datastore.empty();
        Student student = new Student("id-1", "John Doe", 200);
        OperationStatus status = datastore.insert(student);
        assert status == OperationStatus.INSERT_SUCCESS;
        connection.close();
    }

    @Test
    public void testCannotInsertDataWithExistingKey() {
        Connection connection = Connection.getConnection();
        Datastore datastore = connection.getDatastore();
        OperationStatus emptyOperationStatus = datastore.empty();

        Student student = new Student("id-1", "John Doe", 200);
        OperationStatus status = datastore.insert(student);
        assert status == OperationStatus.INSERT_SUCCESS;

        status = datastore.insert(student);
        assert status == OperationStatus.INSERT_FAILED_DUPLICATE_ID;

        connection.close();
    }

    @Test
    public void testCanSelectInsertedDataByKey() {
        Connection connection = Connection.getConnection();
        Datastore datastore = connection.getDatastore();
        OperationStatus emptyOperationStatus = datastore.empty();

        Student student = new Student("id-1", "John Doe", 200);
        OperationStatus status = datastore.insert(student);
        assert status == OperationStatus.INSERT_SUCCESS;

        Student studentReturned = datastore.selectByKey(student.getId()).getResultSet();

        assert studentReturned.getId().equals(student.getId());

        connection.close();
    }

    @Test
    public void testDatastoreIsSharedAcrossConnections() {
        Connection connection1 = Connection.getConnection();
        Datastore datastore = connection1.getDatastore();
        OperationStatus emptyOperationStatus = datastore.empty();

        Student student1 = new Student("id-connection-1", "John Doe", 200);
        datastore.insert(student1);

        Student student2 = new Student("id-connection-2", "Jane Doe", 200);
        datastore.insert(student2);

        Connection connection2 = Connection.getConnection();
        Datastore datastore2 = connection2.getDatastore();

        Student student1FromConnection2 = datastore2.selectByKey(student1.getId()).getResultSet();
        assert student1FromConnection2.getId().equals(student1.getId());

        Student student2FromConnection2 = datastore2.selectByKey(student2.getId()).getResultSet();
        assert student2FromConnection2.getId().equals(student2.getId());
    }

    @Test
    public void testInsertOperationIsThreadSafe() {
        List<Student> studentsList = new ArrayList<>();
        Random randomScoreGenerator = new Random();

        for (int i=0; i<100; i++) {
            studentsList.add(
                new Student(
                    "id-" + i,
                    "Student " + 1,
                    randomScoreGenerator.nextInt(100)
                )
            );
        }

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Runnable runnable = () -> {
            Connection connection = Connection.getConnection();
            try {
                Datastore datastore = connection.getDatastore();
                studentsList.forEach(datastore::insert);
            }catch (Exception ignored) {
            }finally {
                connection.close();
            }
        };

        for (int i=0; i<10; i++) {
            executorService.submit(runnable);
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException ignored) {
        }

        Connection connection = Connection.getConnection();
        Datastore datastore = connection.getDatastore();
        List<Student> returnedStudent = datastore.selectAll().getResultSet();
        assert returnedStudent.size() == 100;
    }
}
