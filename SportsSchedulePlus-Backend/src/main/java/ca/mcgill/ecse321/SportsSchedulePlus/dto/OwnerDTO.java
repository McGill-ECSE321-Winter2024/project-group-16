package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import ca.mcgill.ecse321.SportsSchedulePlus.model.*;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwnerDTO extends PersonRoleDTO {

    private final List<CourseTypeDTO> approvedCoursesDTO;
    private final List<CourseTypeDTO> ownerSuggestedCoursesDTO;
    private DailyScheduleListResponseDTO dailyScheduleListDTO;

    public OwnerDTO(int Id, List<DailyScheduleResponseDTO> dailyScheduleDTO) {
        super(Id);
        approvedCoursesDTO = new ArrayList<>();
        ownerSuggestedCoursesDTO = new ArrayList<>();
        this.dailyScheduleListDTO = new DailyScheduleListResponseDTO(dailyScheduleDTO);
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
            List<DailyScheduleResponseDTO> aDailyScheduleDTO = new ArrayList<>();
            for (DailySchedule dailySchedule : owner.getDailySchedule()) {
                int id = dailySchedule.getId();
                Time openingTime = dailySchedule.getOpeningTime();
                Time closingTime = dailySchedule.getClosingTime();
                aDailyScheduleDTO.add(new DailyScheduleResponseDTO(id, openingTime, closingTime));
            }
            this.dailyScheduleListDTO = new DailyScheduleListResponseDTO(aDailyScheduleDTO);
        } else {
            this.dailyScheduleListDTO = null;
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

    public DailyScheduleListResponseDTO getDailySchedule() {
        return dailyScheduleListDTO;
    }

    public boolean setDailySchedule(DailyScheduleListResponseDTO aDailyScheduleDTO) {
        boolean wasSet = false;
        if (aDailyScheduleDTO != null) {
            dailyScheduleListDTO = aDailyScheduleDTO;
            wasSet = true;
        }
        return wasSet;
    }
}
