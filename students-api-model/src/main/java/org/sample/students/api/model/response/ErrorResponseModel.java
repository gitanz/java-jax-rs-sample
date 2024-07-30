package org.sample.students.api.model.response;

public class ErrorResponseModel {
    private String requestID;
    private String requestTime;
    private String apiCalled;
    private Integer httpStatusCode;
    private String errorMessage;

    public ErrorResponseModel() {

    }

    public ErrorResponseModel(Integer httpStatusCode, String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorMessage;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getApiCalled() {
        return apiCalled;
    }

    public void setApiCalled(String apiCalled) {
        this.apiCalled = apiCalled;
    }
}
