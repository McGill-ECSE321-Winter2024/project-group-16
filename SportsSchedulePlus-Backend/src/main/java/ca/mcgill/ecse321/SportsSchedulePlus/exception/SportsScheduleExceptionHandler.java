package ca.mcgill.ecse321.SportsSchedulePlus.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.ErrorDto;

@ControllerAdvice
public class SportsScheduleExceptionHandler {
    @ExceptionHandler(SportsScheduleException.class)
    public ResponseEntity<ErrorDto> handleEventRegistrationException(SportsScheduleException e) {
        return new ResponseEntity<ErrorDto>(new ErrorDto(e.getMessage()), e.getStatus());
    }
}