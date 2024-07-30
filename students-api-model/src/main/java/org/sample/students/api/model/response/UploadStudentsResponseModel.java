package org.sample.students.api.model.response;

public class UploadStudentsResponseModel implements ResponseModel {
    private String status;

    private Integer count;

    public UploadStudentsResponseModel() {

    }

    public UploadStudentsResponseModel(String status, Integer count) {
        this.status = status;
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
