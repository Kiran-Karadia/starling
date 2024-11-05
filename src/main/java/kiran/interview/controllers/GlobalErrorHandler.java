package kiran.interview.controllers;

import io.micronaut.context.annotation.Any;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.validation.ConstraintViolationException;
import kiran.interview.exceptions.MissingAuthHeaderException;

import java.time.format.DateTimeParseException;

@Controller
public class GlobalErrorHandler {

    @Error(global = true)
    HttpResponse<String> handleHttpClientResponseException(
            HttpRequest<Any> request,
            HttpClientResponseException error
    ) {
        String detailedError = error.getResponse().getBody(String.class).orElse("Unknown error");

        // Pass through errors from Starling API
        return HttpResponse.status(error.getStatus()).body(detailedError);
    }

    @Error(global = true)
    HttpResponse<String> handleGeneralError(
            HttpRequest<Any> request,
            Exception error
    ) {
        String defaultErrorMessage = (error.getMessage() != null) ? error.getMessage() : "Unknown error";

        return switch (error) {
            case DateTimeParseException e ->
                    HttpResponse.badRequest(defaultErrorMessage);
            case ConstraintViolationException e ->
                    HttpResponse.badRequest("Parameter error: " + e.getConstraintViolations().stream().findFirst().get().getMessage());
            case MissingAuthHeaderException e ->
                    HttpResponse.status(HttpStatus.FORBIDDEN).body(error.getMessage());
            default ->
                    HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(defaultErrorMessage);
        };
    }

}
