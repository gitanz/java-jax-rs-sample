package org.sample.students.dal.model;

public class AverageGrade implements DataModel {
    private Double average;
    private Integer count;

    public AverageGrade() {

    }

    @Override
    public String toString() {
        return "AverageGrade{" +
                "average=" + average +
                ", count=" + count +
                '}';
    }

    public AverageGrade(Double average, Integer count) {
        this.average = average;
        this.count = count;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getAverage() {
        return average;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }
}
