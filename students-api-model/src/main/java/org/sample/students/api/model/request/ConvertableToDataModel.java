package org.sample.students.api.model.request;

public interface ConvertableToDataModel<T> {
    public T convertToDataModel();
}
