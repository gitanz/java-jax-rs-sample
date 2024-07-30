package org.sample.students.dal.dynamodb;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.sample.students.appcontext.config.DynamoDBConfig;

public class Datasource {
    private Datasource() {
    }

    private static DynamoDBMapper mapper;
    private static AmazonDynamoDB client;

    public static void setDatasource(DynamoDBConfig dynamoDBConfig) {
        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();
        if (dynamoDBConfig.getUseLocal()) {
            builder = builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dynamoDBConfig.getLocalUrl(), "eu-west-1"));
            builder = builder.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("any", "any")));
        } else {
            builder = builder.withCredentials(new DefaultAWSCredentialsProviderChain());
        }

        client = builder.build();
        mapper = new DynamoDBMapper(client);
    }

    public static AmazonDynamoDB getClient() {
        return client;
    }
    public static DynamoDBMapper getMapper() {
        return mapper;
    }
}
