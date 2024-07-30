package org.sample.students.api.transformers.request_model.to.data_model;

import org.sample.students.api.model.request.StudentBatchUploadRequestModel;
import org.sample.students.api.transformers.Transformable;
import org.sample.students.dal.model.Student;

public class StudentBatchUploadTransformer implements Transformable<Student> {
    private StudentBatchUploadRequestModel studentToUpload;

    public StudentBatchUploadTransformer() {}

    public StudentBatchUploadTransformer(StudentBatchUploadRequestModel studentToUpload) {
        this.studentToUpload = studentToUpload;
    }

    public void setStudentToUpload(StudentBatchUploadRequestModel studentToUpload) {
        this.studentToUpload = studentToUpload;
    }

    @Override
    public Student transform() {
        Student student = new Student();
        student.setId(this.studentToUpload.getId());
        student.setName(this.studentToUpload.getName());
        student.setScore(this.studentToUpload.getScore());

        return student;
    }
}
