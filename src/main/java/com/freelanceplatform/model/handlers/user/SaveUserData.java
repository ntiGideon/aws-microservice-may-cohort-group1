package com.freelanceplatform.model.handlers.user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.freelance.model.model.User;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Map;

public class SaveUserData implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    private final String tableName = System.getenv("USER_TABLE_NAME");

    private final DynamoDbEnhancedClient enhancedClient;

    public SaveUserData() {
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.of(System.getenv("AWS_REGION")))
                .build();

        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        context.getLogger().log("Saving user data: " + input);

        // Map the incoming payload to a User object
        User user = new User();
        user.setCognitoId((String) input.get("sub"));
        user.setUserId((String) input.get("userId"));
        user.setEmail((String) input.get("email"));
        user.setFirstname((String) input.get("firstname"));
        user.setMiddlename((String) input.getOrDefault("middlename", ""));
        user.setLastname((String) input.get("lastname"));
        user.setTelephone((String) input.getOrDefault("telephone", ""));
        user.setPreferredJobCategories((String) input.getOrDefault("preferred_job", ""));

        // Save to DynamoDB
        DynamoDbTable<User> userTable = enhancedClient.table(tableName, TableSchema.fromBean(User.class));
        userTable.putItem(user);

        context.getLogger().log("User saved successfully to " + tableName);

        // Pass along the same payload to the next Step Function step
        return input;
    }
}
