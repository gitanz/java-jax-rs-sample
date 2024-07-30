package org.sample.students.dal.mysql;

import org.sample.students.dal.model.AverageGrade;
import org.sample.students.dal.model.Student;
import org.sample.students.dal.model.StudentsUploadSummary;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentsDAOTest extends BaseTest {

    @Test
    public void testBatchUploadStudents() {
        List<Student> studentList = this.randomStudentsDataProvider(150);

        StudentsUploadSummary studentsUploadSummary = mysqlDAOFactory.getStudentsDAO().uploadStudents(studentList);

        assert studentsUploadSummary.getSuccessCount() != null;
        assert studentsUploadSummary.getDuplicatesCount() != null;

        assert studentsUploadSummary.getSuccessCount() == 150;
        assert studentsUploadSummary.getDuplicatesCount() == 0;
    }

    @Test
    public void testBatchUploadStudents2() {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("1", "John", 10));
        studentList.add(new Student("1", "John", 10));

        StudentsUploadSummary studentsUploadSummary = mysqlDAOFactory.getStudentsDAO().uploadStudents(studentList);

        assert studentsUploadSummary.getSuccessCount() != null;
        assert studentsUploadSummary.getDuplicatesCount() != null;

        assert studentsUploadSummary.getSuccessCount() == 1;
        assert studentsUploadSummary.getDuplicatesCount() == 1;
    }


    @Test
    public void testDuplicatesAreIgnored() throws Exception {
        List<Student> studentList = this.randomStudentsDataProvider(100);

        List<Student> prePopulateStudentsList = studentList
                .stream()
                .limit(10)
                .toList();

        this.prePopulateStudentsTable(prePopulateStudentsList);

        DAOFactory mysqlDAOFactory = new DAOFactory(mySQLConfig);
        StudentsUploadSummary studentsUploadSummary = mysqlDAOFactory.getStudentsDAO().uploadStudents(studentList);

        assert studentsUploadSummary.getSuccessCount() != null;
        assert studentsUploadSummary.getDuplicatesCount() != null;

        assert studentsUploadSummary.getSuccessCount() == 90;
        assert studentsUploadSummary.getDuplicatesCount() == 10;
    }

    @Test
    public void testAverageGradeScoreStudents() {
        List<Student> students = this.prePopulateStudentsTable(10);

        AverageGrade averageGrade = mysqlDAOFactory.getStudentsDAO().getAverageGrade();

        Integer sum = students.stream().mapToInt(Student::getScore).sum();
        assert sum.equals((int) (averageGrade.getAverage() * averageGrade.getCount()));
    }

    @Test
    public void testTopStudents() {
        this.prePopulateStudentsTable(10);
        List<Student> topStudents = mysqlDAOFactory.getStudentsDAO().getTopStudents(5);

        Integer lastStudentsScore = null;
        for (Student student : topStudents) {
            assert lastStudentsScore == null || lastStudentsScore.compareTo(student.getScore()) >= 0;

            lastStudentsScore = student.getScore();
        }
    }

    private List<Student> randomStudentsDataProvider(int randomStudentsCount) {
        List<Student> studentsList = new ArrayList<>();
        Random randomScoreGenerator = new Random();
        for (int i = 0; i < randomStudentsCount; i++) {
            studentsList.add(
                    new Student(
                            "id-" + i,
                            "Student " + 1,
                            randomScoreGenerator.nextInt(100)
                    )
            );
        }

        return studentsList;
    }

    private List<Student> prePopulateStudentsTable(int count) {
        List<Student> studentUploadBatch = this.randomStudentsDataProvider(5);
        String sqlPrePopulateStudentsTable = " INSERT INTO `students` " +
                " (`id`, `name`, `score`) " +
                " VALUES (?, ?, ?) ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlPrePopulateStudentsTable);
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
                        }
                    });

            preparedStatement.executeBatch();
            connection.commit();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return studentUploadBatch;
    }

    private void prePopulateStudentsTable(List<Student> studentUploadBatch) {
        String sqlPrePopulateStudentsTable = " INSERT INTO `students` " +
                " (`id`, `name`, `score`) " +
                " VALUES (?, ?, ?) ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlPrePopulateStudentsTable);
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
                        }
                    });

            preparedStatement.executeBatch();
            connection.commit();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
