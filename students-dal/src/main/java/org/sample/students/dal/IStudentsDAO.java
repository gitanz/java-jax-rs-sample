package org.sample.students.dal;

import org.sample.students.dal.model.AverageGrade;
import org.sample.students.dal.model.Student;
import org.sample.students.dal.model.StudentsUploadSummary;

import java.util.List;

public interface IStudentsDAO {

    public StudentsUploadSummary uploadStudents(List<Student> students);
    public AverageGrade getAverageGrade();

    public List<Student> getTopStudents(Integer count);
}
