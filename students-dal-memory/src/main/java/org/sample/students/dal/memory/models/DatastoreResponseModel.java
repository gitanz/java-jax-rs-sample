package org.sample.students.dal.memory.models;

public class DatastoreResponseModel<T> {
    private OperationStatus status;

    private T resultSet;

    public DatastoreResponseModel(OperationStatus status, T resultSet) {
        this.status = status;
        this.resultSet = resultSet;
    }

    public OperationStatus getStatus() {
        return status;
    }

    public void setStatus(OperationStatus status) {
        this.status = status;
    }

    public T getResultSet() {
        return resultSet;
    }

    public void setResultSet(T resultSet) {
        this.resultSet = resultSet;
    }
}
