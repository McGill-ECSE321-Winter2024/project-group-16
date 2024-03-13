package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import ca.mcgill.ecse321.SportsSchedulePlus.model.*;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwnerResponseDTO extends PersonRoleResponseDTO {

    private final List<CourseTypeResponseDTO> approvedCoursesDTO;
    private final List<CourseTypeResponseDTO> ownerSuggestedCoursesDTO;
    private DailyScheduleListResponseDTO dailyScheduleListDTO;

    public OwnerResponseDTO(int Id, List<DailyScheduleResponseDTO> dailyScheduleDTO) {
        super(Id);
        approvedCoursesDTO = new ArrayList<>();
        ownerSuggestedCoursesDTO = new ArrayList<>();
        this.dailyScheduleListDTO = new DailyScheduleListResponseDTO(dailyScheduleDTO);
    }

    public OwnerResponseDTO() {
        approvedCoursesDTO = new ArrayList<>();
        ownerSuggestedCoursesDTO = new ArrayList<>();
    }

    public OwnerResponseDTO(Owner owner) {
        super(owner.getId());

        this.approvedCoursesDTO = new ArrayList<>();
        for (CourseType courseType : owner.getApprovedCourses()) {
            this.approvedCoursesDTO.add(new CourseTypeResponseDTO(courseType));
        }

        this.ownerSuggestedCoursesDTO = new ArrayList<>();
        for (CourseType courseType : owner.getApprovedCourses()) {
            this.ownerSuggestedCoursesDTO.add(new CourseTypeResponseDTO(courseType));
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

    public CourseTypeResponseDTO getApprovedCourse(int index) {
        return approvedCoursesDTO.get(index);
    }

    public List<CourseTypeResponseDTO> getApprovedCourses() {
        return Collections.unmodifiableList(approvedCoursesDTO);
    }

    public List<CourseTypeResponseDTO> getOwnerSuggestedCourses() {
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
