package org.sample.students.api.transformers.data_model.to.response_model;

import org.sample.students.api.model.response.AverageGradeResponseModel;
import org.sample.students.api.transformers.Transformable;
import org.sample.students.dal.model.AverageGrade;

public class AverageGradeTransformer implements Transformable<AverageGradeResponseModel> {
    public AverageGrade averageGrade;

    public AverageGradeTransformer(AverageGrade averageGrade) {
        this.averageGrade = averageGrade;
    }

    public AverageGradeResponseModel transform() {
        AverageGradeResponseModel averageGradeResponseModel = new AverageGradeResponseModel();
        averageGradeResponseModel.setAverage(this.averageGrade.getAverage());
        averageGradeResponseModel.setTotalStudents(this.averageGrade.getCount());

        return averageGradeResponseModel;
    }
}
