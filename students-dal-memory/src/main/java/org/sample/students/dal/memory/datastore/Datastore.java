package org.sample.students.dal.memory.datastore;

import org.sample.students.dal.memory.models.DatastoreResponseModel;
import org.sample.students.dal.memory.models.OperationStatus;
import org.sample.students.dal.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class Datastore {
    private volatile static ConcurrentHashMap<String, Student> datastore = new ConcurrentHashMap<>();

    private volatile static Set<Student> scoreIndex = new TreeSet<>();

    public Connection connection;

    public Datastore(Connection connection) {
        this.connection = connection;
    }

    private boolean reInit() {
        datastore = new ConcurrentHashMap<>();
        scoreIndex = new TreeSet<>();
        return true;
    }

    public synchronized OperationStatus insert(Student student) {
        if (!this.connection.getAlive()) {
            return OperationStatus.INVALID_CONNECTION_OBJECT;
        }

        if (datastore.containsKey(student.getId())) {
            return OperationStatus.INSERT_FAILED_DUPLICATE_ID;
        }
        Student insertStudent = new Student(student.getId(), student.getName(), student.getScore());

        datastore.put(insertStudent.getId(), insertStudent);
        scoreIndex.add(insertStudent);

        return OperationStatus.INSERT_SUCCESS;
    }

    public DatastoreResponseModel<Student> selectByKey(String key) {
        Student student;

        if (!this.connection.getAlive()) {
            return new DatastoreResponseModel<>(OperationStatus.INVALID_CONNECTION_OBJECT, new Student());
        }

        student = datastore.get(key);
        return new DatastoreResponseModel<>(OperationStatus.INVALID_CONNECTION_OBJECT, student);
    }

    public OperationStatus empty() {
        if (!this.connection.getAlive()) {
            return OperationStatus.INVALID_CONNECTION_OBJECT;
        }

        this.reInit();
        return OperationStatus.EMPTY_SUCCESS;
    }

    public DatastoreResponseModel<List<Student>> selectAll() {
        if (!this.connection.getAlive()) {
            return new DatastoreResponseModel<>(OperationStatus.INVALID_CONNECTION_OBJECT, new ArrayList<>());
        }

        List<Student> students = datastore.values().stream().toList();

        return new DatastoreResponseModel<>(OperationStatus.SELECT_SUCCESS, students);
    }

    public DatastoreResponseModel<List<Student>> getTopStudents(Integer count) {
        List<Student> students = scoreIndex.stream().limit(count).toList();

        return new DatastoreResponseModel<>(OperationStatus.SELECT_SUCCESS, students);
    }
}
