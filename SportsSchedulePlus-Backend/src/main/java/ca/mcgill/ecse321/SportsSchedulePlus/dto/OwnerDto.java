package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwnerDto extends PersonRoleDto {

    private int id;
    private final List<CourseTypeDto> approvedCoursesDto;
    private final List <CourseTypeDto> ownerSuggestedCoursesDto;
    private DailyScheduleDto dailyScheduleDto;


    public OwnerDto(int Id, DailyScheduleDto dailyScheduleDto) {
        super(Id);
        approvedCoursesDto = new ArrayList <CourseTypeDto> ();
        ownerSuggestedCoursesDto = new ArrayList <CourseTypeDto> ();
        this.dailyScheduleDto = dailyScheduleDto;

    }
    public OwnerDto() {
        approvedCoursesDto = new ArrayList <CourseTypeDto> ();
        ownerSuggestedCoursesDto = new ArrayList <CourseTypeDto> ();
    }

    public CourseTypeDto getApprovedCourse(int index) {
        CourseTypeDto approvedCourse = approvedCoursesDto.get(index);
        return approvedCourse;
    }

    public List <CourseTypeDto> getApprovedCourses() {
        List <CourseTypeDto> newApprovedCourses = Collections.unmodifiableList(approvedCoursesDto);
        return newApprovedCourses;
    }

    public List <CourseTypeDto> getOwnerSuggestedCourses() {
        List <CourseTypeDto> newOwnerSuggestedCourses = Collections.unmodifiableList(ownerSuggestedCoursesDto);
        return newOwnerSuggestedCourses;
    }

    public DailyScheduleDto getDailySchedule() {
        return dailyScheduleDto;
    }
    /* Code from template association_MinimumNumberOfMethod */

    public boolean setDailySchedule(DailyScheduleDto aDailyScheduleDto) {
        boolean wasSet = false;
        if (aDailyScheduleDto != null) {
            dailyScheduleDto = aDailyScheduleDto;
            wasSet = true;
        }
        return wasSet;
    }
}
