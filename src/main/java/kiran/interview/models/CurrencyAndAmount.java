package kiran.interview.models;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CurrencyAndAmount(String currency, int minorUnits) { }