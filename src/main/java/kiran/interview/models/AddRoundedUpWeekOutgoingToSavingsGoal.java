package kiran.interview.models;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record AddRoundedUpWeekOutgoingToSavingsGoal(
        AddMoneyToSavingsGoalResponse addMoneyToSavingsGoalResponse,
        CurrencyAndAmount amountAdded
) {
}