package antifraud.exception;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ExceptionResponse(HttpStatus status, LocalDateTime timestamp, List<String> errors) {

    public ExceptionResponse(HttpStatus status, List<String> errors) {
        this(status, LocalDateTime.now(), errors);
    }

    @JsonGetter("status")
    public String formattedStatus() {
        return "%s".formatted(status);
    }
}