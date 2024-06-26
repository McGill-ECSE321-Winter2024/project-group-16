package ca.mcgill.ecse321.SportsSchedulePlus.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.error.ErrorDTO;

@ControllerAdvice
public class SportsScheduleExceptionHandler {
    @ExceptionHandler(SportsScheduleException.class)
    public ResponseEntity<ErrorDTO> handleEventRegistrationException(SportsScheduleException e) {
        return new ResponseEntity<ErrorDTO>(new ErrorDTO(e.getMessage()), e.getStatus());
    }
}