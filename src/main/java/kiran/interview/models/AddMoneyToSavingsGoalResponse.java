package kiran.interview.models;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record AddMoneyToSavingsGoalResponse(
        String transferUid,
        boolean success
) {
}
