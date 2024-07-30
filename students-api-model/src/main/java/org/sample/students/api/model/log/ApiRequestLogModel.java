package org.sample.students.api.model.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApiRequestLogModel {
    private String requestId;
    private String requestTime;
    private String apiCalled;

    @Override
    public String toString() {
        return "ApiRequestLogModel{" +
                "requestId='" + requestId + '\'' +
                ", requestTime='" + requestTime + '\'' +
                ", apiCalled='" + apiCalled + '\'' +
                '}';
    }

    public ApiRequestLogModel(){

    }

    public ApiRequestLogModel(String requestId, String apiCalled) {
        this.requestId = requestId;
        this.requestTime = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss").format(
                LocalDateTime.now()
        );
        this.apiCalled = apiCalled;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public String getApiCalled() {
        return apiCalled;
    }
}
