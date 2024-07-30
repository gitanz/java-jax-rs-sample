package org.sample.students.dal.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.sample.students.dal.dynamodb.models.StudentDynamoDBModel;
import org.junit.Test;

import java.util.Random;
import java.util.SimpleTimeZone;

public class DatasourceTest extends BaseTest {

    @Test
    public void testCanStaticallyRetrieveDynamoDBDatasource() {
        DynamoDBMapper dynamoDBMapper = Datasource.getMapper();
        assert dynamoDBMapper != null;
    }

    @Test
    public void testDynamoDBMapperFromDynamoDBDataSourceCanSaveDynamoDBModelObjects() {
        DynamoDBMapper dynamoDBMapper = Datasource.getMapper();

        StudentDynamoDBModel studentDynamoDBModel = new StudentDynamoDBModel();
        Random random = new Random();

        int randomInt = random.nextInt(1000);
        studentDynamoDBModel.setPk(StudentsDAO.PartitionKey);
        studentDynamoDBModel.setId("ddb-datasource-test-" + randomInt);
        studentDynamoDBModel.setName("ddb-data-source-test-" + randomInt);
        studentDynamoDBModel.setScore(random.nextInt(100));

        dynamoDBMapper.save(studentDynamoDBModel);

        StudentDynamoDBModel studentDynamoDBObjectRetrieved = dynamoDBMapper.load(
                StudentDynamoDBModel.class,
                StudentsDAO.PartitionKey,
                studentDynamoDBModel.getId()
        );

        assert studentDynamoDBObjectRetrieved != null;
    }
}
