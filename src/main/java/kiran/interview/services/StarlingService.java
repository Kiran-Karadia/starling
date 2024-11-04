package kiran.interview.services;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import kiran.interview.httpclients.StarlingClient;
import kiran.interview.models.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class StarlingService {

    private final StarlingClient starlingClient;

    @Inject
    public StarlingService(StarlingClient starlingClient) {
        this.starlingClient = starlingClient;
    }

    public AmountToSaveAndTransactions getTransactionsFromWeek(String accountUid, String startTimestamp) {
        String endTimestamp = addWeekToTimestamp(startTimestamp);

        List<FeedItem> outgoingFeedItems = getOutgoingTransactionsBetween(
                accountUid,
                startTimestamp,
                endTimestamp
        );

        List<OutgoingTransaction> outgoingTransactions = new ArrayList<>();

        int totalAmountToSave = calculateSavingsFromWeek(accountUid, startTimestamp);

        for (FeedItem item : outgoingFeedItems) {
            outgoingTransactions.add(
                    new OutgoingTransaction(
                            item.reference(),
                            item.amount(),
                            item.transactionTime()
                    )
            );
        }

        return new AmountToSaveAndTransactions(
                totalAmountToSave,
                outgoingTransactions
        );
    }

    public AddRoundedUpWeekOutgoingToSavingsGoal addRoundedUpWeekOutgoingToSavingsGoal(
            String accountUid,
            String savingsGoalUid,
            String weekStartTimestamp
    ) {
        int minorUnitsToSave = calculateSavingsFromWeek(accountUid, weekStartTimestamp);
        CurrencyAndAmount totalAmountToSave = new CurrencyAndAmount("GBP", minorUnitsToSave);

        return new AddRoundedUpWeekOutgoingToSavingsGoal(
                starlingClient.addMoneyToSavingsGoal(
                        accountUid,
                        savingsGoalUid,
                        UUID.randomUUID().toString(),
                        new Amount(totalAmountToSave)
                ),
                totalAmountToSave
        );
    }

    private int calculateAmountToSave(int minorUnits) {
        if (minorUnits % 100 == 0) {
            return 0;
        }
        return 100 - (minorUnits % 100);
    }

    private String addWeekToTimestamp(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        return ZonedDateTime.parse(timestamp, formatter)
                .plusWeeks(1)
                .format(formatter);
    }

    private int calculateSavingsFromWeek(String accountUid, String startTimestamp) { // CHANGE THIS NAME, DOESN@T MAKE SENSE
        String endTimestamp = addWeekToTimestamp(startTimestamp);

        List<FeedItem> outgoingFeedItems = getOutgoingTransactionsBetween(
                accountUid,
                startTimestamp,
                endTimestamp
        );
        int amountToSave = 0;
        for (FeedItem item : outgoingFeedItems) {
            amountToSave += calculateAmountToSave(item.amount().minorUnits());
        }
        return amountToSave;
    }

    private List<FeedItem> getOutgoingTransactionsBetween(
            String accountUid,
            String startTimestamp,
            String endTimestamp
    ) {
        return starlingClient.getTransactionsBetween(
                accountUid, startTimestamp, endTimestamp)
                .feedItems().stream()
                .filter(feedItem -> feedItem.direction().equals("OUT"))
                .filter(feedItem -> !feedItem.spendingCategory().equals("SAVING"))
                .collect(Collectors.toList());
    }
}
