package org.sample.students.dal.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import org.sample.students.appcontext.config.DynamoDBConfig;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {
    protected DynamoDBConfig dynamoDBConfig;
    protected DAOFactory dynamoDBDAOFactory;
    protected DynamoDBMapper dynamoDBMapper;

    protected DynamoDB dynamoDBClient;

    @Before
    public void setup() throws Exception {
        dynamoDBConfig = new DynamoDBConfig("http://localhost:8001");
        dynamoDBDAOFactory = new DAOFactory(dynamoDBConfig);
        stopIfRealDatabase();
        setDynamoDBDatasource();
        createStudentTable();
    }

    private void setDynamoDBDatasource() {
        Datasource.setDatasource(dynamoDBConfig);
        dynamoDBMapper = Datasource.getMapper();
        dynamoDBClient = new DynamoDB(Datasource.getClient());
    }

    private void createStudentTable() throws InterruptedException {
        String tableName = "students";

        CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName);
        createTableRequest.setProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(5L));

        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("pk").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("id").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("score").withAttributeType("N"));

        createTableRequest.setAttributeDefinitions(attributeDefinitions);

        List<KeySchemaElement> tableKeySchema = new ArrayList<>();
        tableKeySchema.add(new KeySchemaElement().withAttributeName("pk").withKeyType(KeyType.HASH));
        tableKeySchema.add(new KeySchemaElement().withAttributeName("id").withKeyType(KeyType.RANGE));

        createTableRequest.setKeySchema(tableKeySchema);

        List<KeySchemaElement> indexKeySchema = new ArrayList<>();
        indexKeySchema.add(new KeySchemaElement().withAttributeName("pk").withKeyType(KeyType.HASH));
        indexKeySchema.add(new KeySchemaElement().withAttributeName("score").withKeyType(KeyType.RANGE));

        Projection projection = new Projection().withProjectionType(ProjectionType.ALL);

        LocalSecondaryIndex studentScoreIndex = new LocalSecondaryIndex()
            .withIndexName("student-score-index")
            .withKeySchema(indexKeySchema)
            .withProjection(projection);

        ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<>();
        localSecondaryIndexes.add(studentScoreIndex);
        createTableRequest.setLocalSecondaryIndexes(localSecondaryIndexes);


        Table table = dynamoDBClient.createTable(createTableRequest);
        table.waitForActive();
    }

    private void stopIfRealDatabase() throws Exception {
        if (dynamoDBConfig != null && dynamoDBConfig.getLocalUrl().contains("8000")) {
            System.out.println("Executing against real database.");
            throw new Exception("No execution against real database.");
        }
    }

    @After
    public void tearDown() {
        try {
            Table table = dynamoDBClient.getTable("students");
            table.delete();
            table.waitForDelete();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
