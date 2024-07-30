package org.sample.students.dal.mysql;

import org.sample.students.dal.model.Student;
import org.sample.students.dal.model.StudentsUploadSummary;

import java.sql.*;
import java.util.List;

public class StudentBatchUploadHelpers {
    public void createTempStudentsTable(Connection connection) throws SQLException {
        String sqlCreateTempTable = " CREATE TEMPORARY TABLE IF NOT EXISTS `temp_students` ( " +
                "`id` varchar(255) NOT NULL, " +
                "`name` varchar(255) NOT NULL, " +
                "`score` int(11) NOT NULL " +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8; ";


        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlCreateTempTable);
    }

    public void populateTemporaryTable(Connection connection, List<Student> studentUploadBatch) throws SQLException {
        String sqlPopulateTemporaryTable = " INSERT INTO `temp_students` " +
                " (`id`, `name`, `score`) " +
                " VALUES (?, ?, ?) ";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlPopulateTemporaryTable);

        studentUploadBatch
            .stream()
            .filter(student -> student.getId() != null)
            .forEach(student -> {
                try {
                    preparedStatement.setString(1, student.getId());
                    preparedStatement.setString(2, student.getName());
                    preparedStatement.setInt(3, student.getScore());
                    preparedStatement.addBatch();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                    throw new RuntimeException(exception);
                }
            });

        preparedStatement.executeBatch();
    }

    public StudentsUploadSummary importStudents(Connection connection) throws SQLException {
        StudentsUploadSummary studentsUploadSummary = new StudentsUploadSummary();

        try {
            connection.setAutoCommit(false);
            Integer duplicatesCount = countDuplicates(connection);

            Integer insertedCount = importFromTemp(connection);
            connection.commit();
            studentsUploadSummary.setDuplicatesCount(duplicatesCount);
            studentsUploadSummary.setSuccessCount(insertedCount);
        } catch (SQLException exception) {
            connection.rollback();
            connection.setAutoCommit(true);
            throw exception;
        }

        return studentsUploadSummary;
    }

    public Integer countDuplicates(Connection connection) throws SQLException {
        ResultSet resultSet = null;
        Integer countDuplicateStudents = null;

        String sqlCountDuplicateStudents = " SELECT SUM(IF(exists_in_table, count, count-1)) AS `duplicate_count` " +
                " FROM  ( " +
                    " SELECT temp_students.id temp_id, students.id IS NOT NULL as exists_in_table, count(*) as count" +
                    " FROM `temp_students` " +
                    " LEFT JOIN `students` ON `students`.`id` = `temp_students`.`id` " +
                    " GROUP BY `temp_students`.`id`, `students`.`id` " +
                " ) existing_id_count";


        PreparedStatement preparedStatement = connection.prepareStatement(sqlCountDuplicateStudents);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        countDuplicateStudents = resultSet.getInt("duplicate_count");

        return countDuplicateStudents;
    }

    public Integer importFromTemp(Connection connection) throws SQLException {
        String sqlImportFromTemp = " INSERT INTO students " +
                " (`id`, `name`, `score`) " +
                " SELECT " +
                "   `temp_students`.`id`, " +
                "   `temp_students`.`name`, " +
                "   `temp_students`.`score` " +
                " FROM `temp_students` " +
                " LEFT JOIN `students` ON `students`.`id` = `temp_students`.`id` " +
                " WHERE students.id IS NULL GROUP BY `temp_students`.`id`";


        PreparedStatement preparedStatement = connection.prepareStatement(sqlImportFromTemp);

        return preparedStatement.executeUpdate();
    }

    public void dropTempTable(Connection connection) throws SQLException {
        String sqlDropTempTable = " DROP TEMPORARY TABLE IF EXISTS `temp_students`; ";

        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlDropTempTable);
    }
}
