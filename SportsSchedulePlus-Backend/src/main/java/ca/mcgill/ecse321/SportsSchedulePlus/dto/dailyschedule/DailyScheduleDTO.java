package ca.mcgill.ecse321.SportsSchedulePlus.dto.dailyschedule;

import java.sql.Time;

public class DailyScheduleDTO {

    private int id;
    private Time openingTime;
    private Time closingTime;

    public DailyScheduleDTO() {
        // Default constructor
    }

    public DailyScheduleDTO(int id, Time openingTime, Time closingTime) {
        this.id = id;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    // Getters and setters for each attribute

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Time getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Time openingTime) {
        this.openingTime = openingTime;
    }

    public Time getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Time closingTime) {
        this.closingTime = closingTime;
    }
}
