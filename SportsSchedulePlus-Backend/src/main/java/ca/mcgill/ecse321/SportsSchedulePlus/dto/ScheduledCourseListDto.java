package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.List;

public class ScheduledCourseListDto {
    private List<ScheduledCourseResponseDto> scheduledCourses;

    public ScheduledCourseListDto(List<ScheduledCourseResponseDto> scheduledCourses) {
        this.scheduledCourses = scheduledCourses;
    }

    public List<ScheduledCourseResponseDto> getScheduledCourses() {
        return scheduledCourses;
    }

    public void setScheduledCourses(List<ScheduledCourseResponseDto> scheduledCourses) {
        this.scheduledCourses = scheduledCourses;
    }
}
