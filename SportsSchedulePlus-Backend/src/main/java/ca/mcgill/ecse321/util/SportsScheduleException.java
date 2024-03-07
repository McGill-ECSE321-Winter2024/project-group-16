package ca.mcgill.ecse321.util;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SportsScheduleException extends ResponseStatusException {
    public SportsScheduleException(HttpStatus status, String message) {
        super(status, message);
    }
}
