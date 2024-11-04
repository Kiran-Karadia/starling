package kiran.interview.mockdata;

import kiran.interview.models.*;

import java.util.List;

public class MockData {

    public static FeedItem mockFeedItem(int minorUnits, String spendingCategory) {
        return new FeedItem(
                new CurrencyAndAmount("GBP", minorUnits),
                "OUT",
                "REF",
                "TIME",
                spendingCategory
        );
    }
    public static TransactionsBetweenResponse mockTransactionsBetweenResponse() {
        List<FeedItem> feedItems = List.of(
                mockFeedItem(1, "PAYMENT"),
                mockFeedItem(10, "SAVING")
        );
        return new TransactionsBetweenResponse(feedItems);
    }

    public static AddMoneyToSavingsGoalResponse mockAddMoneyToSavingsGoalResponse() {
        return new AddMoneyToSavingsGoalResponse("transferUid", true);
    }

    public static String generateGetTransactionsFromWeekPath(String uuid, String timestamp) {
        return "/starling" +
                "/feed" +
                "/account/"
                + uuid
                + "/outgoing" +
                "/week?" +
                "weekStartTimestamp=" + timestamp;
    }

    public static String generateAddRoundedUpWeekOutgoingToSavingsGoalPath(String uuid, String timestamp) {
        return "/starling" +
                "/account/" +
                uuid +
                "/savings-goals/" +
                uuid +
                "/add-money" +
                "/round-up?" +
                "weekStartTimestamp=" + timestamp;
    }

}
