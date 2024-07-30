package org.sample.students.dal.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.sample.students.appcontext.config.DynamoDBConfig;
import org.sample.students.dal.IStudentsDAO;
import org.sample.students.dal.dynamodb.models.StudentDynamoDBModel;
import org.sample.students.dal.model.AverageGrade;
import org.sample.students.dal.model.Student;
import org.sample.students.dal.model.StudentsUploadSummary;

import java.util.ArrayList;
import java.util.List;

public class StudentsDAO extends BaseDAO implements IStudentsDAO {
    public static String PartitionKey = "Students";
    public StudentsDAO(DynamoDBConfig dynamoDBConfig) {
        super(dynamoDBConfig);
    }

    @Override
    public StudentsUploadSummary uploadStudents(List<Student> students) {
        StudentsUploadSummary uploadSummary = new StudentsUploadSummary();
        uploadSummary.setSuccessCount(0);
        uploadSummary.setDuplicatesCount(0);

        students.forEach(student -> {
            StudentDynamoDBModel studentDynamoDBModel = mapper.load(
                StudentDynamoDBModel.class,
                StudentsDAO.PartitionKey,
                student.getId()
            );

            if (studentDynamoDBModel != null) {
                uploadSummary.setDuplicatesCount(uploadSummary.getDuplicatesCount() + 1);
            } else {
                studentDynamoDBModel = new StudentDynamoDBModel();
                studentDynamoDBModel.setPk(StudentsDAO.PartitionKey);
                studentDynamoDBModel.setId(student.getId());
                studentDynamoDBModel.setName(student.getName());
                studentDynamoDBModel.setScore(student.getScore());
                this.mapper.save(studentDynamoDBModel);
                uploadSummary.setSuccessCount(uploadSummary.getSuccessCount() + 1);
            }
        });

        return uploadSummary;
    }

    @Override
    public AverageGrade getAverageGrade() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        List<StudentDynamoDBModel> students = mapper.scan(StudentDynamoDBModel.class, scanExpression);
        Double average = students
                .stream()
                .mapToDouble(student -> {
                    return (double) student.getScore();
                })
                .average()
                .orElse(0.0);

        Integer count = students.size();

        return new AverageGrade(average, count);
    }

    @Override
    public List<Student> getTopStudents(Integer count) {
        StudentDynamoDBModel hashKeyObject = new StudentDynamoDBModel();
        hashKeyObject.setPk(StudentsDAO.PartitionKey);

        DynamoDBQueryExpression<StudentDynamoDBModel> query = new DynamoDBQueryExpression<StudentDynamoDBModel>()
            .withHashKeyValues(hashKeyObject)
            .withIndexName("student-score-index")
            .withScanIndexForward(false)
            .withConsistentRead(false)
            .withLimit(count);

        List<StudentDynamoDBModel> items = mapper.query(StudentDynamoDBModel.class, query);
        List<Student> students = new ArrayList<>();

        items.forEach(studentDynamoDBModel -> {
            Student student = new Student();

            student.setId(studentDynamoDBModel.getId());
            student.setName(studentDynamoDBModel.getName());
            student.setScore(((Long)studentDynamoDBModel.getScore()).intValue());

            students.add(student);
        });

        return students;
    }
}
