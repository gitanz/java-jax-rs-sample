package org.sample.students.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.sample.students.api.model.request.StudentBatchUploadRequestModel;
import org.sample.students.api.model.response.AverageGradeResponseModel;
import org.sample.students.api.model.response.UploadStudentsResponseModel;
import org.sample.students.api.transformers.data_model.to.response_model.AverageGradeTransformer;
import org.sample.students.api.transformers.data_model.to.response_model.UploadStudentsTransformer;
import org.sample.students.api.transformers.request_model.to.data_model.StudentBatchUploadTransformer;
import org.sample.students.dal.exception.StudentException;
import org.sample.students.dal.model.AverageGrade;
import org.sample.students.dal.model.Student;
import org.sample.students.dal.model.StudentsUploadSummary;
import org.sample.students.services.DataService;

import java.util.List;

@Path("api/v1/students")
public class StudentsResource {
    @POST
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<UploadStudentsResponseModel> uploadStudents (
            List<StudentBatchUploadRequestModel> uploadStudentsPayload
    ) {
        StudentBatchUploadTransformer studentBatchUploadTransformer = new StudentBatchUploadTransformer();

        List<Student> students = uploadStudentsPayload
                .stream()
                .map(studentPayload -> {
                    studentBatchUploadTransformer.setStudentToUpload(studentPayload);
                    return studentBatchUploadTransformer.transform();
                })
                .toList();

        StudentsUploadSummary studentsUploadSummary = DataService
                .getDAOFactory()
                .getStudentsDAO()
                .uploadStudents(students);

        UploadStudentsTransformer transformer = new UploadStudentsTransformer(studentsUploadSummary);

        return transformer.transform();
    }

    @GET
    @Path("/average-grade")
    @Produces(MediaType.APPLICATION_JSON)
    public AverageGradeResponseModel getAverageGrade() {
        AverageGrade averageGrade = DataService.getDAOFactory().getStudentsDAO().getAverageGrade();
        AverageGradeTransformer transformer = new AverageGradeTransformer(averageGrade);

        return transformer.transform();
    }

    @GET
    @Path("/top-students")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getTopStudents(@QueryParam("limit") Integer limit) {
        List<Student> students = DataService.getDAOFactory().getStudentsDAO().getTopStudents(limit);
        return students;
    }

    @GET
    @Path("/error-response")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testErrorResponse() {
        throw new StudentException(100, "This is test error message");
    }

    @GET
    @Path("/runtime-error")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testRuntimeException() {
        throw new RuntimeException("eg. exmple");
    }
}
