package org.sample.students.dal.model;

public class StudentsUploadSummary implements DataModel {
    private Integer successCount;
    private Integer duplicatesCount;

    @Override
    public String toString() {
        return "StudentsUploadSummary{" +
                "successCount=" + successCount +
                ", duplicatesCount=" + duplicatesCount +
                '}';
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getDuplicatesCount() {
        return duplicatesCount;
    }

    public void setDuplicatesCount(Integer duplicatesCount) {
        this.duplicatesCount = duplicatesCount;
    }

}
