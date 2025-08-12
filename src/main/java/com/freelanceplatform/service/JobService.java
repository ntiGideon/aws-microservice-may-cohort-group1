package com.freelanceplatform.service;

import com.freelance.model.Job;
import com.freelance.utils.DynamoDB;
import com.freelance.utils.JobMapper;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Map;
import java.util.UUID;

public class JobService {
    DynamoDB dynamoDB = new DynamoDB();
    String jobTableName = System.getenv("JOB_TABLE_NAME");


    public String saveJobData(Map<String, Object> input, String postedByUserId) {

        Job job = JobMapper.fromMap(input);

        if (job.getJobId() == null) {
            job.setJobId(generateJobId());
        }

        job.setPostedByUserId(postedByUserId);

        DynamoDbTable<Job> table = dynamoDB.client()
                .table(jobTableName, TableSchema.fromBean(Job.class));
        table.putItem(job);

        return job.getJobId();
    }

    private String generateJobId() {
        return "job-" + UUID.randomUUID();
    }


    public Job getJobById(String jobId) {
        DynamoDbTable<Job> jobTable = dynamoDB.client()
                .table(jobTableName, TableSchema.fromBean(Job.class));
        return jobTable.getItem(Key.builder().partitionValue(jobId).build());
    }

}
