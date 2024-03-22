package ca.mcgill.ecse321.SportsSchedulePlus.dto.dailyschedule;

public class DailyScheduleRequestDTO {
    private String openingTime;
    private String closingTime;

    public DailyScheduleRequestDTO(String aOpeningTime, String aClosingTime) {
        openingTime = aOpeningTime;
        closingTime = aClosingTime;
    }

    public boolean setOpeningTime(String aOpeningTime) {
        boolean wasSet = false;
        openingTime = aOpeningTime;
        wasSet = true;
        return wasSet;
    }

    public boolean setClosingTime(String aClosingTime) {
        boolean wasSet = false;
        closingTime = aClosingTime;
        wasSet = true;
        return wasSet;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }
}