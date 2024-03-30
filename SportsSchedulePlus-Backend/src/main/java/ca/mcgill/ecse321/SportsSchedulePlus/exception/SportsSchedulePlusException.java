package ca.mcgill.ecse321.SportsSchedulePlus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

// Make this exception unchecked. Maybe it's usually better to use checked exceptions, but almost every service method
// could throw one of these exceptions and the controller should almost never handle it, so I don't see any benefit to
// making it checked in this case.
public class SportsSchedulePlusException extends RuntimeException {
    @NonNull
    private HttpStatus status;

    public SportsSchedulePlusException(@NonNull HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    @NonNull
    public HttpStatus getStatus() {
        return status;
    }
}
