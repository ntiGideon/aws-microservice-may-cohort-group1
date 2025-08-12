package com.freelanceplatform.model.model;

import com.freelance.model.utils.JobStatus;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;

@DynamoDbBean
public class Job {
    private String jobId;
    private String name;
    private String description;
    private double payAmount;
    private JobStatus status;
    private Instant datePosted;
    private Instant expiryDate;
    private String timeToComplete;
    private String postedByUserId;
    private String claimedByUserId;
    private String category;
    private String location;

    @DynamoDbPartitionKey
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getClaimedByUserId(){
        return claimedByUserId;
    }
    public void setClaimedByUserId(String claimedByUserId){
        this.claimedByUserId = claimedByUserId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPayAmount() { return payAmount; }
    public void setPayAmount(double payAmount) { this.payAmount = payAmount; }

    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }

    public Instant getDatePosted() { return datePosted; }
    public void setDatePosted(Instant datePosted) { this.datePosted = datePosted; }

    public Instant getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }

    public String getTimeToComplete() { return timeToComplete; }
    public void setTimeToComplete(String timeToComplete) { this.timeToComplete = timeToComplete; }

    public String getPostedByUserId() { return postedByUserId; }
    public void setPostedByUserId(String postedByUserId) { this.postedByUserId = postedByUserId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}