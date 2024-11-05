package kiran.interview.httpclients;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import kiran.interview.models.AddMoneyToSavingsGoalResponse;
import kiran.interview.models.Amount;
import kiran.interview.models.TransactionsBetweenResponse;

@Client("https://api-sandbox.starlingbank.com/api/v2")
@Header(name = "Accept", value = "application/json")
@ExecuteOn(TaskExecutors.BLOCKING)
public interface StarlingClient {

    @Get("/feed/account/{accountUid}/settled-transactions-between")
    TransactionsBetweenResponse getTransactionsBetween(
            @Header(name = "Authorization") String access_token,
            @PathVariable String accountUid,
            @QueryValue String minTransactionTimestamp,
            @QueryValue String maxTransactionTimestamp
    );

    @Put("/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}")
    AddMoneyToSavingsGoalResponse addMoneyToSavingsGoal(
            @Header(name = "Authorization") String access_token,
            @PathVariable String accountUid,
            @PathVariable String savingsGoalUid,
            @PathVariable String transferUid,
            @Body Amount body
    );
}
