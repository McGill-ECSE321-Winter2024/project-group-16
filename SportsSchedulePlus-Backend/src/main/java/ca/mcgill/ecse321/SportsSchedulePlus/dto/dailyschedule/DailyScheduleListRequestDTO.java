package ca.mcgill.ecse321.SportsSchedulePlus.dto.dailyschedule;

import java.util.List;

public class DailyScheduleListRequestDTO {
    private List<DailyScheduleRequestDTO> dailySchedules;
    
    public DailyScheduleListRequestDTO(List<DailyScheduleRequestDTO> aDailySchedules) {
        dailySchedules = aDailySchedules;
    }
    
    public List<DailyScheduleRequestDTO> getDailySchedules() {
        return dailySchedules;
    }
    
    public void setDailySchedules(List<DailyScheduleRequestDTO> aDailySchedules) {
        dailySchedules = aDailySchedules;
    }
}