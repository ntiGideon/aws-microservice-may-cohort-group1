package com.freelanceplatform.reponse;

public class ObjectResponse {
    private String status;
    private Object jobId;

    public ObjectResponse(String status, String jobId) {
        this.status = status;
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
