package kiran.interview.annotations.validation.timestamp;

import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import jakarta.inject.Singleton;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Singleton
@Introspected
public class TimestampValidator implements ConstraintValidator<Timestamp, String> {

    @Override
    public boolean isValid(@Nullable String timestamp, @NonNull AnnotationValue<Timestamp> annotationMetadata, io.micronaut.validation.validator.constraints.@NonNull ConstraintValidatorContext context) {
        try {
            ZonedDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException error) {
            return false;
        }
        return true;
    }

}
