package org.sample.students.api.model.response;

public class AverageGradeResponseModel {
    private Double average;
    private Integer totalStudents;

    public AverageGradeResponseModel() {

    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Integer getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Integer totalStudents) {
        this.totalStudents = totalStudents;
    }
}
