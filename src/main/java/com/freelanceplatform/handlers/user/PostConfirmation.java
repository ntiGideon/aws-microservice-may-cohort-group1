package com.freelanceplatform.handlers.user;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CognitoUserPoolPostConfirmationEvent;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClientBuilder;
import com.amazonaws.services.stepfunctions.model.StartExecutionRequest;
import com.amazonaws.services.stepfunctions.model.StartExecutionResult;

import java.util.HashMap;
import java.util.Map;

public class PostConfirmation implements RequestHandler<CognitoUserPoolPostConfirmationEvent, CognitoUserPoolPostConfirmationEvent> {

    private final AWSStepFunctions stepFunctionsClient;
    private final AWSCognitoIdentityProvider cognitoClient;
    private final String stateMachineArn;
    private final String defaultGroup = "Users";

    public PostConfirmation() {
        this.stepFunctionsClient = AWSStepFunctionsClientBuilder.defaultClient();
        this.cognitoClient = AWSCognitoIdentityProviderClientBuilder.defaultClient();
        this.stateMachineArn = System.getenv("STATE_MACHINE_ARN");
    }

    @Override
    public CognitoUserPoolPostConfirmationEvent handleRequest(CognitoUserPoolPostConfirmationEvent event, Context context) {
        context.getLogger().log("Received PostConfirmation event: " + event);

        try {
            String username = event.getUserName();
            String userPoolId = event.getUserPoolId();

            // Add user to the default group
            AdminAddUserToGroupRequest groupRequest = new AdminAddUserToGroupRequest()
                    .withUserPoolId(userPoolId)
                    .withUsername(username)
                    .withGroupName(defaultGroup);
            cognitoClient.adminAddUserToGroup(groupRequest);
            context.getLogger().log("User " + username + " added to group " + defaultGroup);

            // Prepare input for Step Functions
            Map<String, Object> input = new HashMap<>();
            String cognitoSub = event.getRequest().getUserAttributes().get("sub");
            input.put("userId", cognitoSub);
            input.put("cognitoId", cognitoSub);
            input.put("email", event.getRequest().getUserAttributes().get("email"));
            input.put("firstname", event.getRequest().getUserAttributes().get("given_name"));
            input.put("lastname", event.getRequest().getUserAttributes().get("family_name"));

            if (event.getRequest().getUserAttributes().containsKey("middle_name")) {
                input.put("middlename", event.getRequest().getUserAttributes().get("middle_name"));
            }
            if (event.getRequest().getUserAttributes().containsKey("phone_number")) {
                input.put("telephone", event.getRequest().getUserAttributes().get("phone_number"));
            }

            // Start Step Functions execution
            StartExecutionRequest executionRequest = new StartExecutionRequest()
                    .withStateMachineArn(stateMachineArn)
                    .withInput(com.amazonaws.util.json.Jackson.toJsonString(input));

            StartExecutionResult executionResult = stepFunctionsClient.startExecution(executionRequest);
            context.getLogger().log("Started Step Function execution: " + executionResult.getExecutionArn());

        } catch (Exception e) {
            context.getLogger().log("Error in PostConfirmation: " + e.getMessage());
            throw e;
        }

        return event;
    }
}