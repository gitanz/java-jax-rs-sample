package org.sample.students.dal.dynamodb.models;


import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName = "students")
public class StudentDynamoDBModel {
    @DynamoDBHashKey(attributeName = "pk")
    private String pk;
    @DynamoDBRangeKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "name")
    private String name;
    @DynamoDBIndexRangeKey(localSecondaryIndexName = "student-score-index", attributeName = "score")
    private long score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }
}
