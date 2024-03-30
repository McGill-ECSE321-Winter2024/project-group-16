package ca.mcgill.ecse321.SportsSchedulePlus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

public class SportsScheduleException extends RuntimeException {
    
    @NonNull
    private HttpStatus status;

    public SportsScheduleException(@NonNull HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    @NonNull
    public HttpStatus getStatus() {
        return status;
    }
}