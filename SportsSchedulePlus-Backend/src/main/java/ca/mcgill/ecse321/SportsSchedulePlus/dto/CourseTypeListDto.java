package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.List;

public class CourseTypeListDto {
    private List<CourseTypeResponseDto> courseTypes;

    public CourseTypeListDto(List<CourseTypeResponseDto> courseTypes) {
        this.courseTypes = courseTypes;
    }

    public List<CourseTypeResponseDto> getCourseTypes() {
        return courseTypes;
    }

    public void setCourseTypes(List<CourseTypeResponseDto> courseTypes) {
        this.courseTypes = courseTypes;
    }
}
