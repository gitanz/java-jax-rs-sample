package org.sample.students.dal.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.sample.students.appcontext.config.DynamoDBConfig;

public class BaseDAO {

    protected DynamoDBMapper mapper;

    public BaseDAO(DynamoDBConfig dynamoDBConfig) {
        Datasource.setDatasource(dynamoDBConfig);
        this.mapper = Datasource.getMapper();
    }
}
