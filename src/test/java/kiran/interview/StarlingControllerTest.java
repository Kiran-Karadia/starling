package kiran.interview;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import kiran.interview.httpclients.StarlingClient;
import kiran.interview.mockdata.MockData;
import kiran.interview.models.AddRoundedUpWeekOutgoingToSavingsGoal;
import kiran.interview.models.AmountToSaveAndTransactions;
import org.junit.jupiter.api.Test;

import static kiran.interview.mockdata.MockData.generateAddRoundedUpWeekOutgoingToSavingsGoalPath;
import static kiran.interview.mockdata.MockData.generateGetTransactionsFromWeekPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
class StarlingControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    StarlingClient starlingClient;

    @MockBean(StarlingClient.class)
    public StarlingClient starlingClient() {
        return mock(StarlingClient.class);
    }


    @Test
    void testGetOutgoingsForSomeWeek() {
        when(starlingClient.getTransactionsBetween(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(MockData.mockTransactionsBetweenResponse());

        HttpResponse<AmountToSaveAndTransactions> response = client.toBlocking().exchange(
                HttpRequest.GET(
                                generateGetTransactionsFromWeekPath(
                                        "aaaaaaaa-aaaa-4aaa-aaaa-aaaaaaaaaaaa",
                                        "2024-01-01T00:00:00Z"
                                )
                        ).header("Authorization", "Bearer access_token"),
                AmountToSaveAndTransactions.class
        );

        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    void testGetOutgoingsForSomeWeekBadUuid() {
        when(starlingClient.getTransactionsBetween(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(MockData.mockTransactionsBetweenResponse());

        HttpClientResponseException exception = assertThrows(
            HttpClientResponseException.class,
            () -> client.toBlocking().exchange(
                HttpRequest.GET(
                        generateGetTransactionsFromWeekPath(
                        "badUuid",
                        "2024-01-01T00:00:00Z"
                    )
                ).header("Authorization", "Bearer access_token"),
                AmountToSaveAndTransactions.class
            )
        );

        String exceptionErrorMessage = exception.getResponse().getBody(String.class).orElse(null);

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(
                "Parameter error: must match \"[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}\"",
                exceptionErrorMessage
        );
    }

    @Test
    void testGetOutgoingsForSomeWeekBadTimestamp() {

        when(starlingClient.getTransactionsBetween(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(MockData.mockTransactionsBetweenResponse());

        HttpClientResponseException exception = assertThrows(
            HttpClientResponseException.class,
            () -> client.toBlocking().exchange(
                HttpRequest.GET(
                        generateGetTransactionsFromWeekPath(
                        "aaaaaaaa-aaaa-4aaa-aaaa-aaaaaaaaaaaa",
                        "2024/01-01T00:00:00Z"
                    )
                ).header("Authorization", "Bearer access_token"),
                AmountToSaveAndTransactions.class
            )
        );

        String exceptionErrorMessage = exception.getResponse().getBody(String.class).orElse(null);

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(
                "Parameter error: Timestamp value must fit ISO 8601 standard",
                exceptionErrorMessage
        );
    }

    @Test
    void testAddRoundedUpWeekOutgoingToSavingsGoal() {
        when(starlingClient.getTransactionsBetween(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(MockData.mockTransactionsBetweenResponse());

        when(
            starlingClient.addMoneyToSavingsGoal(
                    anyString(),
                anyString(),
                anyString(),
                anyString(),
                any()
            )
        ).thenReturn(
                MockData.mockAddMoneyToSavingsGoalResponse()
        );

        HttpResponse<AddRoundedUpWeekOutgoingToSavingsGoal> response =
            client.toBlocking().exchange(
                HttpRequest.PUT(
                    generateAddRoundedUpWeekOutgoingToSavingsGoalPath(
                         "aaaaaaaa-aaaa-4aaa-aaaa-aaaaaaaaaaaa",
                            "2024-01-01T00:00:00Z"
                    ),
                    null
                ).header("Authorization", "Bearer access_token"),
                    AddRoundedUpWeekOutgoingToSavingsGoal.class
            );

        assertEquals(HttpStatus.OK, response.getStatus());
    }
}
