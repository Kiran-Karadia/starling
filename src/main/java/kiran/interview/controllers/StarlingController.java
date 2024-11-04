package kiran.interview.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.inject.Inject;
import kiran.interview.annotations.validation.timestamp.uuid.UuidFormat;
import kiran.interview.annotations.validation.timestamp.Timestamp;
import kiran.interview.models.AddRoundedUpWeekOutgoingToSavingsGoal;
import kiran.interview.models.AmountToSaveAndTransactions;
import kiran.interview.services.StarlingService;

@Controller("/starling")
@ExecuteOn(TaskExecutors.BLOCKING)
public class StarlingController {

    private final StarlingService service;

    @Inject
    public StarlingController(StarlingService service) {
        this.service = service;
    }

    @Get("/feed/account/{accountUid}/outgoing/week")
    @Operation(summary = "Outgoings for a given week", description = "Get every outgoing transaction for a given week")

    public AmountToSaveAndTransactions getTransactionsFromWeek(
            @PathVariable @UuidFormat String accountUid,
            @QueryValue @Timestamp String weekStartTimestamp
    ) {
        return service.getTransactionsFromWeek(accountUid, weekStartTimestamp);
    }

    @Put("/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/round-up")
    @Operation(
            summary = "Save rounded up outgoings",
            description = "Calculates outgoing spending for a given week, " +
                    "rounds up each transaction, " +
                    "then saves to the savings account"
    )
    public AddRoundedUpWeekOutgoingToSavingsGoal addRoundedUpWeekOutgoingToSavingsGoal(
            @PathVariable @UuidFormat String accountUid,
            @PathVariable @UuidFormat String savingsGoalUid,
            @QueryValue @Timestamp String weekStartTimestamp
    ) {
        return service.addRoundedUpWeekOutgoingToSavingsGoal(
                accountUid,
                savingsGoalUid,
                weekStartTimestamp
        );
    }
}