package org.sample.students.dal.memory;

import org.sample.students.dal.IDAOFactory;
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
        StudentsDAO inMemoryStudentsDAO = new StudentsDAO();

        List<Student> studentList = this.randomStudentsDataProvider(100);
        StudentsUploadSummary studentsUploadSummary = inMemoryStudentsDAO.uploadStudents(studentList);
        assert studentsUploadSummary.getSuccessCount() != null;
        assert studentsUploadSummary.getDuplicatesCount() != null;

        assert studentsUploadSummary.getSuccessCount() == 100;
        assert studentsUploadSummary.getDuplicatesCount() == 0;
    }

    @Test
    public void testAverageGradeScoreStudents() {
        List<Student> students = this.prePopulateStudentsTable(15);

        IDAOFactory inMemoryStudentsDAO = new DAOFactory();
        AverageGrade averageGrade = inMemoryStudentsDAO.getStudentsDAO().getAverageGrade();

        Integer sum = students.stream().mapToInt(Student::getScore).sum();
        assert sum.equals((int) (averageGrade.getAverage() * averageGrade.getCount()));
    }

    @Test
    public void testTopStudents() {
        this.prePopulateStudentsTable(10);

        IDAOFactory inMemoryStudentsDAO = new DAOFactory();
        List<Student> topStudents = inMemoryStudentsDAO.getStudentsDAO().getTopStudents(5);

        Integer lastStudentsScore = null;
        for (Student student : topStudents) {
            assert lastStudentsScore == null || lastStudentsScore.compareTo(student.getScore()) >= 0;

            lastStudentsScore = student.getScore();
        }
    }

    private List<Student> prePopulateStudentsTable(int count) {
        List<Student> students = this.randomStudentsDataProvider(count);
        students.forEach(student -> {
            datastore.insert(student);
        });

        return students;
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
}
