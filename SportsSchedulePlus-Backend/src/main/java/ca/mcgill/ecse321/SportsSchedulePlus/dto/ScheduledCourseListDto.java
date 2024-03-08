package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.List;

public class ScheduledCourseListDTO {
    private List<ScheduledCourseResponseDTO> scheduledCourses;

    public ScheduledCourseListDTO(List<ScheduledCourseResponseDTO> scheduledCourses) {
        this.scheduledCourses = scheduledCourses;
    }

    public List<ScheduledCourseResponseDTO> getScheduledCourses() {
        return scheduledCourses;
    }

    public void setScheduledCourses(List<ScheduledCourseResponseDTO> scheduledCourses) {
        this.scheduledCourses = scheduledCourses;
    }
}
