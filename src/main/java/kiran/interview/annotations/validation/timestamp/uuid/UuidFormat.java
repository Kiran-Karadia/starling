package kiran.interview.annotations.validation.timestamp.uuid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;


@Pattern(regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")
@Schema(type = "string", format = "uuid", description = "Unique identifier for the account in UUID format")
public @interface UuidFormat {
    String message() default "Value must be a valid UUID";
}
