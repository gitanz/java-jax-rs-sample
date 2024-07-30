package org.sample.students.dal.mysql;

import org.sample.students.appcontext.config.MySQLConfig;
import org.sample.students.dal.IStudentsDAO;
import org.sample.students.dal.exception.StudentException;
import org.sample.students.dal.model.AverageGrade;
import org.sample.students.dal.model.Student;
import org.sample.students.dal.model.StudentsUploadSummary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentsDAO extends BaseDAO implements IStudentsDAO {
    private StudentBatchUploadHelpers studentBatchUploadHelpers;

    public StudentsDAO(MySQLConfig config) {
        super(config);
        this.studentBatchUploadHelpers = new StudentBatchUploadHelpers();
    }

    @Override
    public StudentsUploadSummary uploadStudents(List<Student> studentUploadBatch) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            this.studentBatchUploadHelpers.createTempStudentsTable(connection);
            this.studentBatchUploadHelpers.populateTemporaryTable(connection, studentUploadBatch);

            StudentsUploadSummary studentsUploadSummary = this.studentBatchUploadHelpers.importStudents(connection);
            this.studentBatchUploadHelpers.dropTempTable(connection);

            return studentsUploadSummary;
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            throw new StudentException(exception.getErrorCode(), exception.getMessage());
        }
        finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public AverageGrade getAverageGrade() {
        AverageGrade averageGrade = new AverageGrade();
        try (Connection connection = this.dataSource.getConnection()) {
            String sqlCountAndAverageOfAll = " SELECT COUNT(*) as `count`, AVG(score) as `average` from students; ";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlCountAndAverageOfAll);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            averageGrade.setCount(resultSet.getInt("count"));
            averageGrade.setAverage(resultSet.getDouble("average"));

            return averageGrade;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new StudentException(exception.getErrorCode(), exception.getMessage());
        }
    }

    @Override
    public List<Student> getTopStudents(Integer count) {
        try (Connection connection = this.dataSource.getConnection()) {
            List<Student> students = new ArrayList<>();

            String sqlTopStudents = " SELECT `id`, `name`, `score` from students ORDER BY score DESC LIMIT  ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlTopStudents);
            preparedStatement.setInt(1, count);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getString("id"));
                student.setName(resultSet.getString("name"));
                student.setScore(resultSet.getInt("score"));

                students.add(student);
            }

            return students;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new StudentException(exception.getErrorCode(), exception.getMessage());
        }
    }
}
