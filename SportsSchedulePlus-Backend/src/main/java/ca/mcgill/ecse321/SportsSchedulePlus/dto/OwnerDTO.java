package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import ca.mcgill.ecse321.SportsSchedulePlus.model.*;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwnerDTO extends PersonRoleDTO {

    private final List<CourseTypeDTO> approvedCoursesDTO;
    private final List<CourseTypeDTO> ownerSuggestedCoursesDTO;
    private DailyScheduleDTO dailyScheduleDTO;

    public OwnerDTO(int Id, DailyScheduleDTO dailyScheduleDTO) {
        super(Id);
        approvedCoursesDTO = new ArrayList<>();
        ownerSuggestedCoursesDTO = new ArrayList<>();
        this.dailyScheduleDTO = dailyScheduleDTO;
    }

    public OwnerDTO() {
        approvedCoursesDTO = new ArrayList<>();
        ownerSuggestedCoursesDTO = new ArrayList<>();
    }

    public OwnerDTO(Owner owner) {
        super(owner.getId());

        this.approvedCoursesDTO = new ArrayList<>();
        for (CourseType courseType : owner.getApprovedCourses()) {
            this.approvedCoursesDTO.add(new CourseTypeDTO(courseType));
        }

        this.ownerSuggestedCoursesDTO = new ArrayList<>();
        for (CourseType courseType : owner.getApprovedCourses()) {
            this.ownerSuggestedCoursesDTO.add(new CourseTypeDTO(courseType));
        }

        if (owner.getDailySchedule() != null) {
            DailySchedule dailySchedule = owner.getDailySchedule();
            int id = dailySchedule.getId();
            Time openingTime = dailySchedule.getOpeningTime();
            Time closingTime = dailySchedule.getClosingTime();
            DailyScheduleDTO dailyScheduleDTO = new DailyScheduleDTO(id, openingTime, closingTime);
            this.dailyScheduleDTO = dailyScheduleDTO;
        } else {
            this.dailyScheduleDTO = null;
        }
    }

    public CourseTypeDTO getApprovedCourse(int index) {
        return approvedCoursesDTO.get(index);
    }

    public List<CourseTypeDTO> getApprovedCourses() {
        return Collections.unmodifiableList(approvedCoursesDTO);
    }

    public List<CourseTypeDTO> getOwnerSuggestedCourses() {
        return Collections.unmodifiableList(ownerSuggestedCoursesDTO);
    }

    public DailyScheduleDTO getDailySchedule() {
        return dailyScheduleDTO;
    }

    public boolean setDailySchedule(DailyScheduleDTO aDailyScheduleDTO) {
        boolean wasSet = false;
        if (aDailyScheduleDTO != null) {
            dailyScheduleDTO = aDailyScheduleDTO;
            wasSet = true;
        }
        return wasSet;
    }
}
