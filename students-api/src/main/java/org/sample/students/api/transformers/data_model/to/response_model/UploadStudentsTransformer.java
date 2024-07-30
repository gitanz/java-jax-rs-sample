package org.sample.students.api.transformers.data_model.to.response_model;

import org.sample.students.api.model.response.UploadStudentsResponseModel;
import org.sample.students.api.transformers.Transformable;
import org.sample.students.dal.model.StudentsUploadSummary;

import java.util.ArrayList;
import java.util.List;

public class UploadStudentsTransformer implements Transformable<List<UploadStudentsResponseModel>> {
    public StudentsUploadSummary uploadStudentsDataObject;

    public UploadStudentsTransformer(StudentsUploadSummary uploadStudentsDataObject) {
        this.uploadStudentsDataObject = uploadStudentsDataObject;
    }

    public List<UploadStudentsResponseModel> transform() {
        List<UploadStudentsResponseModel> response = new ArrayList<>();

        UploadStudentsResponseModel successCount = new UploadStudentsResponseModel(
                "SUCCESS",
                this.uploadStudentsDataObject.getSuccessCount()
        );
        UploadStudentsResponseModel duplicatesCount = new UploadStudentsResponseModel(
                "DUPLICATES",
                this.uploadStudentsDataObject.getDuplicatesCount()
        );

        response.add(successCount);
        response.add(duplicatesCount);

        return response;
    }
}
