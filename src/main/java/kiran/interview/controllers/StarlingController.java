package kiran.interview.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Put("/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/round-up")
    @Operation(
            summary = "Save rounded up outgoings",
            description = "Gets all outgoings for a given week, " +
                    "rounds up each transaction, " +
                    "then saves the rounded up amount to the savings account"
    )
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "Account UID not found or invalid parameter")
    @ApiResponse(responseCode = "403", description = "Access token invalid or missing")
    @Tag(name = "Round-up")
    public AddRoundedUpWeekOutgoingToSavingsGoal addRoundedUpWeekOutgoingToSavingsGoal(
            @PathVariable @UuidFormat String accountUid,
            @PathVariable @UuidFormat String savingsGoalUid,
            @QueryValue @Timestamp String weekStartTimestamp,
            HttpRequest<?> request
    ) {
        return service.addRoundedUpWeekOutgoingToSavingsGoal(
                accountUid,
                savingsGoalUid,
                weekStartTimestamp,
                request
        );
    }

    @Get("/feed/account/{accountUid}/outgoing/week")
    @Operation(summary = "Outgoings for a given week", description = "Get every outgoing transaction for a given week")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "Account UID not found or invalid parameter")
    @ApiResponse(responseCode = "403", description = "Access token invalid or missing")
    @Tag(name = "Round-up")
    public AmountToSaveAndTransactions getTransactionsFromWeek(
            @PathVariable @UuidFormat String accountUid,
            @QueryValue @Timestamp String weekStartTimestamp,
            HttpRequest<?> request
    ) {
        return service.getAmountToSaveAndTransactions(request, accountUid, weekStartTimestamp);
    }

}