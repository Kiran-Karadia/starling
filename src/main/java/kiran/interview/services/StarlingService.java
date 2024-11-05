package kiran.interview.services;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import kiran.interview.httpclients.StarlingClient;
import kiran.interview.models.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Singleton
public class StarlingService {

    private final StarlingClient starlingClient;

    @Inject
    public StarlingService(StarlingClient starlingClient) {
        this.starlingClient = starlingClient;
    }

    public AmountToSaveAndTransactions getAmountToSaveAndTransactions(HttpRequest<?> request, String accountUid, String weekStartTimestamp) {
        // Format and add 1 week (7 days) to the given timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        String endTimestamp = ZonedDateTime.parse(weekStartTimestamp, formatter)
                .plusWeeks(1)
                .format(formatter);

        String access_token = extractAccessToken(request);

        // Get all outgoing transactions within the given week, that are not part of the SAVING category
        var outgoingFeedItems = starlingClient.getTransactionsBetween(access_token, accountUid, weekStartTimestamp, endTimestamp)
                .feedItems().stream()
                .filter(feedItem -> feedItem.direction().equals("OUT"))
                .filter(feedItem -> !feedItem.spendingCategory().equals("SAVING"))
                .toList();

        // Accumulate amount to save for each item:
        // Round minorUnits to the nearest round
        // Minus the original minorUnits
        int totalAmountToSave = 0;
        for (FeedItem item: outgoingFeedItems) {
            int minorUnits = item.amount().minorUnits();
            if (minorUnits % 100 != 0) {
                totalAmountToSave += (100 - (minorUnits % 100));
            }
        }

        return new AmountToSaveAndTransactions(
                totalAmountToSave,
                outgoingFeedItems
        );
    }

    public AddRoundedUpWeekOutgoingToSavingsGoal addRoundedUpWeekOutgoingToSavingsGoal(
            String accountUid,
            String savingsGoalUid,
            String weekStartTimestamp,
            HttpRequest<?> request
    ) {
        String access_token = extractAccessToken(request);
        AmountToSaveAndTransactions amountToSaveAndTransactions = getAmountToSaveAndTransactions(request, accountUid, weekStartTimestamp);

        int totalAmountToSave = amountToSaveAndTransactions.totalAmountToSave();
        CurrencyAndAmount savingsCurrencyAndAmount = new CurrencyAndAmount("GBP", totalAmountToSave);

        // Needed for call to starling API
        Amount savingsAmount = new Amount(savingsCurrencyAndAmount);

        return new AddRoundedUpWeekOutgoingToSavingsGoal(
                starlingClient.addMoneyToSavingsGoal(
                        access_token,
                        accountUid,
                        savingsGoalUid,
                        UUID.randomUUID().toString(),
                        savingsAmount
                ),
                savingsCurrencyAndAmount
        );
    }

    private String extractAccessToken(HttpRequest<?> request) {
        return request.getHeaders().get(HttpHeaders.AUTHORIZATION);
    }
}
