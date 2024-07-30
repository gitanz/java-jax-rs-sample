package org.sample.students.dal.exception;

import jakarta.ws.rs.WebApplicationException;

public class StudentException extends WebApplicationException {
    private Integer errorCode;

    private String errorMessage;

    public StudentException(Integer errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
