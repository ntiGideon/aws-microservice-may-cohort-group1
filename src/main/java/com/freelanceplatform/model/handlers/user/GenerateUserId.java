package com.freelanceplatform.model.handlers.user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class GenerateUserId implements RequestHandler<Map<String, Object>, Map<String, Object>> {

        @Override
        public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
            context.getLogger().log("Input received: " + input);

            String userId = (String) input.get("userId");
            context.getLogger().log("Using Cognito userId (sub): " + userId);

            return new HashMap<>(input);
        }
}
