package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.List;

public class ErrorDTO {
    private List<String> errors;

    // Jackson needs a no-args constructor, but it doesn't need to be public
    @SuppressWarnings("unused")
    private ErrorDTO() {
    }

    public ErrorDTO(String error) {
        this.errors = List.of(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}