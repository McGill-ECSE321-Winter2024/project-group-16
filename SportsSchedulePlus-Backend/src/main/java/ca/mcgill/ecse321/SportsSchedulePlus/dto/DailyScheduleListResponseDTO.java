package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.List;

public class DailyScheduleListResponseDTO {
    private List<DailyScheduleResponseDTO> dailySchedules;
    
    public DailyScheduleListResponseDTO(List<DailyScheduleResponseDTO> aDailySchedules) {
        dailySchedules = aDailySchedules;
    }
    
    public List<DailyScheduleResponseDTO> getDailySchedules() {
        return dailySchedules;
    }
    
    public void setDailySchedules(List<DailyScheduleResponseDTO> aDailySchedules) {
        dailySchedules = aDailySchedules;
    }
}