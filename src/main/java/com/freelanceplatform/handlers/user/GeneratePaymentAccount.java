package com.freelanceplatform.handlers.user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GeneratePaymentAccount implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        context.getLogger().log("Generating payment account for: " + input);

        // Create a payment account number (simple numeric format)
        String accountNumber = String.format("%010d", new Random().nextInt(1_000_000_000));

        // Use the firstname + lastname as account name (fallback if null)
        String accountName = (input.getOrDefault("firstname", "") + " " +
                input.getOrDefault("lastname", "")).trim();

        // Initial balance (could be 0.00 or some signup bonus)
        double initialBalance = 0.00;

        // Add to output payload
        Map<String, Object> output = new HashMap<>(input);
        output.put("paymentAccountNumber", accountNumber);
        output.put("paymentAccountName", accountName);
        output.put("paymentInitialBalance", initialBalance);

        context.getLogger().log("Generated payment account: " + accountNumber);

        return output;
    }
}