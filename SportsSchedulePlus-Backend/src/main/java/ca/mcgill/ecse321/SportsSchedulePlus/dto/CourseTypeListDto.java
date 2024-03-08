package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.List;

public class CourseTypeListDTO {
    private List<CourseTypeResponseDTO> courseTypes;

    public CourseTypeListDTO(List<CourseTypeResponseDTO> courseTypes) {
        this.courseTypes = courseTypes;
    }

    public List<CourseTypeResponseDTO> getCourseTypes() {
        return courseTypes;
    }

    public void setCourseTypes(List<CourseTypeResponseDTO> courseTypes) {
        this.courseTypes = courseTypes;
    }
}
