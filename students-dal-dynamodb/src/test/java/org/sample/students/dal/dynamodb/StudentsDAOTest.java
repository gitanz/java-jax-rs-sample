package org.sample.students.dal.dynamodb;

import org.sample.students.dal.dynamodb.models.StudentDynamoDBModel;
import org.sample.students.dal.model.AverageGrade;
import org.sample.students.dal.model.Student;
import org.sample.students.dal.model.StudentsUploadSummary;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentsDAOTest extends BaseTest {
    @Test
    public void testBatchUploadStudents() {
        List<Student> studentList = this.randomStudentsDataProvider(100);

        StudentsUploadSummary studentsUploadSummary = dynamoDBDAOFactory.getStudentsDAO().uploadStudents(studentList);

        assert studentsUploadSummary.getSuccessCount() != null;
        assert studentsUploadSummary.getDuplicatesCount() != null;

        assert studentsUploadSummary.getSuccessCount() == 100;
        assert studentsUploadSummary.getDuplicatesCount() == 0;
    }

    @Test
    public void testDuplicatesAreIgnored() {
        List<Student> studentList = this.randomStudentsDataProvider(100);
        List<Student> prePopulateStudentsList = this.prePopulateStudentsTable(
            studentList
                .stream()
                .limit(10)
                .toList()
        );

        StudentsUploadSummary studentsUploadSummary = dynamoDBDAOFactory.getStudentsDAO().uploadStudents(studentList);

        assert studentsUploadSummary.getSuccessCount() != null;
        assert studentsUploadSummary.getDuplicatesCount() != null;

        assert studentsUploadSummary.getSuccessCount() == 90;
        assert studentsUploadSummary.getDuplicatesCount() == 10;
    }


    @Test
    public void testAverageGradeScoreStudents() {
        List<Student> students = this.prePopulateStudentsTable(10);

        AverageGrade averageGrade = dynamoDBDAOFactory.getStudentsDAO().getAverageGrade();

        Integer sum = students.stream().mapToInt(Student::getScore).sum();
        assert sum.equals((int) (averageGrade.getAverage() * averageGrade.getCount()));
    }

    @Test
    public void testTopStudents() {
        this.prePopulateStudentsTable(10);
        List<Student> topStudents = dynamoDBDAOFactory.getStudentsDAO().getTopStudents(5);

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
        List<Student> studentList = this.randomStudentsDataProvider(count);

        StudentDynamoDBModel studentDynamoDBModel = new StudentDynamoDBModel();
        studentList.forEach(student -> {
            studentDynamoDBModel.setPk(StudentsDAO.PartitionKey);
            studentDynamoDBModel.setId(student.getId());
            studentDynamoDBModel.setName(student.getName());
            studentDynamoDBModel.setScore(student.getScore());
            dynamoDBMapper.save(studentDynamoDBModel);
        });

        return studentList;
    }

    private List<Student> prePopulateStudentsTable(List<Student> studentList) {
        StudentDynamoDBModel studentDynamoDBModel = new StudentDynamoDBModel();
        studentList.forEach(student -> {
            studentDynamoDBModel.setPk(StudentsDAO.PartitionKey);
            studentDynamoDBModel.setId(student.getId());
            studentDynamoDBModel.setName(student.getName());
            studentDynamoDBModel.setScore(student.getScore());
            dynamoDBMapper.save(studentDynamoDBModel);
        });

        return studentList;
    }
}
