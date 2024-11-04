package kiran.interview.models;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Amount(
        CurrencyAndAmount amount
) {
}
