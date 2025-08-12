package com.freelanceplatform.model.handlers.job;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freelance.model.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class SaveJobData implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>  {

    Logger logger = LoggerFactory.getLogger(SaveJobData.class);
    ObjectMapper mapper = new ObjectMapper();
    JobService jobService = new JobService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setHeaders(Map.of("Content-Type", "application/json"));

        try {

            String userId = extractUserIdFromToken(event);
            if (userId == null || userId.isEmpty()) {
                response.setStatusCode(401);
                response.setBody("{\"error\":\"Unauthorized - Invalid or missing token\"}");
                return response;
            }


            Map<String, Object> input = mapper.readValue(event.getBody(), HashMap.class);


            String jobId = jobService.saveJobData(input, userId);

            response.setStatusCode(201);
            response.setBody(mapper.writeValueAsString(
                    Map.of("status", "SUCCESS", "jobId", jobId)
            ));

        } catch (JsonProcessingException e) {
            logger.error("Invalid input format", e);
            response.setStatusCode(400);
            response.setBody("{\"error\":\"Invalid request body\"}");
        } catch (Exception e) {
            logger.error("Job creation failed", e);
            response.setStatusCode(500);
            response.setBody("{\"error\":\"Internal server error\"}");
        }

        return response;
    }



    private String extractUserIdFromToken(APIGatewayProxyRequestEvent event) {
        try {
            // Get the Authorization header
            String authHeader = event.getHeaders().get("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return null;
            }

            // Extract the token
            String token = authHeader.substring(7);

            // The token is a JWT with 3 parts separated by dots
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }

            // Decode the payload (middle part)
            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));

            // Parse the JSON payload to get the cognito:username
            Map<String, Object> payloadMap = mapper.readValue(payload, HashMap.class);
            return (String) payloadMap.get("cognito:username");

        } catch (Exception e) {
            logger.error("Error extracting userId from token", e);
            return null;
        }
    }
}