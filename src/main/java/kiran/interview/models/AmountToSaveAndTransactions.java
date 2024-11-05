package kiran.interview.models;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record AmountToSaveAndTransactions(
        int totalAmountToSave,
        List<FeedItem> outgoingTransactions
) {
}
