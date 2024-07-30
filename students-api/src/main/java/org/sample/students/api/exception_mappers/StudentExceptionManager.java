package org.sample.students.api.exception_mappers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.sample.students.api.model.response.ErrorResponseModel;
import org.sample.students.dal.exception.StudentException;

@Provider
public class StudentExceptionManager extends BaseExceptionManager implements ExceptionMapper<StudentException> {
    @Context
    HttpServletRequest request;

    @Override
    public Response toResponse(StudentException exception) {
        ErrorResponseModel errorResponse = new ErrorResponseModel(
            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
            exception.getMessage()
        );

        errorResponse.setRequestID(request.getRequestedSessionId());
        errorResponse.setRequestTime(this.getCurrentTime());
        errorResponse.setApiCalled(request.getRequestURI());

        return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
            .entity(errorResponse)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}
