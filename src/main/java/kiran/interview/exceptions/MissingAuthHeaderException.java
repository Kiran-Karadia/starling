package kiran.interview.exceptions;

public class MissingAuthHeaderException extends RuntimeException {
    public MissingAuthHeaderException(String errorMessage) {
        super(errorMessage);
    }
}
