package kiran.interview.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record FeedItem(
        CurrencyAndAmount amount,
        String direction,
        String reference,
        String transactionTime,
        String spendingCategory
) {
}
