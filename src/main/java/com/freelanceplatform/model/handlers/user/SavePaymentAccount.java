package com.freelanceplatform.model.handlers.user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.freelance.model.model.PaymentAccount;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Map;

public class SavePaymentAccount implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<PaymentAccount> accountTable;

    public SavePaymentAccount() {
        DynamoDbClient ddb = DynamoDbClient.create();
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();
        this.accountTable = enhancedClient.table(System.getenv("ACCOUNTS_TABLE"), TableSchema.fromBean(PaymentAccount.class));
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        context.getLogger().log("Saving payment account: " + input);

        PaymentAccount account = new PaymentAccount();
        account.setUserId((String) input.get("userId"));
        account.setAccountNumber((String) input.get("paymentAccountNumber"));
        account.setAccountName((String) input.get("paymentAccountName"));
        account.setInitialBalance((Double) input.get("paymentInitialBalance"));

        accountTable.putItem(account);

        context.getLogger().log("Payment account saved for userId: " + account.getUserId());

        return input;
    }
}