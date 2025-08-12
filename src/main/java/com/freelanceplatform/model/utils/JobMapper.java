package com.freelanceplatform.model.utils;

import com.freelance.model.model.Job;

import java.time.Instant;
import java.util.Map;

public class JobMapper {
    public static Job fromMap(Map<String, Object> input) {
        Job job = new Job();

        job.setName((String) input.get("name"));
        job.setDescription((String) input.get("description"));

        job.setPayAmount(transformPayAmount(input.get("payAmount")));
        job.setExpiryDate(transformExpiryDate(input.get("expiryDate")));

        job.setStatus(JobStatus.OPEN);
        job.setDatePosted(Instant.now());

        return job;
    }

    private static double transformPayAmount(Object payAmount) {
        if (payAmount instanceof Number) {
            return ((Number) payAmount).doubleValue();
        }
        return Double.parseDouble(payAmount.toString());
    }

    private static Instant transformExpiryDate(Object expiryDate) {
        return Instant.parse(expiryDate.toString());
    }
}
