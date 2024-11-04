package kiran.interview.annotations.validation.timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;

@Constraint(validatedBy = TimestampValidator.class)
@Schema(type = "string", format = "date-time", description = "Timestamp must follow ISO 8601 formatting (for example 2024-01-25T12:34:56.789Z")
public @interface Timestamp {
    String message() default "Timestamp value must fit ISO 8601 standard";
}

