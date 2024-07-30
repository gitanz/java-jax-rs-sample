package org.sample.students.dal.memory;

import org.sample.students.dal.IStudentsDAO;
import org.sample.students.dal.memory.datastore.Connection;
import org.sample.students.dal.memory.datastore.Datastore;
import org.sample.students.dal.memory.models.OperationStatus;
import org.sample.students.dal.model.AverageGrade;
import org.sample.students.dal.model.Student;
import org.sample.students.dal.model.StudentsUploadSummary;

import java.util.ArrayList;
import java.util.List;

public class StudentsDAO implements IStudentsDAO {
    public StudentsDAO() {
    }

    @Override
    public StudentsUploadSummary uploadStudents(List<Student> students) {
        Connection connection = Connection.getConnection();
        Datastore datastore = connection.getDatastore();

        StudentsUploadSummary uploadSummary = new StudentsUploadSummary();

        try {
            uploadSummary.setSuccessCount(0);
            uploadSummary.setDuplicatesCount(0);

            students.forEach(student -> {
                OperationStatus status = datastore.insert(student);

                if (status == OperationStatus.INSERT_SUCCESS) {
                    uploadSummary.setSuccessCount(uploadSummary.getSuccessCount() + 1);
                }
                if (status == OperationStatus.INSERT_FAILED_DUPLICATE_ID) {
                    uploadSummary.setDuplicatesCount(uploadSummary.getDuplicatesCount() + 1);
                }
            });

            connection.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            connection.close();
        }

        return uploadSummary;
    }

    @Override
    public AverageGrade getAverageGrade() {
        Connection connection = Connection.getConnection();
        Datastore datastore = connection.getDatastore();
        AverageGrade averageGrade = new AverageGrade();

        try {
            List<Student> allStudents = datastore
                    .selectAll()
                    .getResultSet();

            Double average =
                    allStudents
                        .stream()
                        .mapToDouble(Student::getScore)
                        .average()
                        .orElse(0.0);

            Integer count = allStudents.size();

            averageGrade = new AverageGrade(average, count);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return averageGrade;
    }

    @Override
    public List<Student> getTopStudents(Integer count) {
        Connection connection = Connection.getConnection();
        Datastore datastore = connection.getDatastore();
        List<Student> topStudents = new ArrayList<>();
        try {
            topStudents = datastore
                    .getTopStudents(count)
                    .getResultSet();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return topStudents;
    }
}
