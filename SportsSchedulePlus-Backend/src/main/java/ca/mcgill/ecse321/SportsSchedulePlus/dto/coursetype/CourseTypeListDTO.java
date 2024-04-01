package ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype;

import java.util.List;
import java.util.Collections;

public class CourseTypeListDTO {
    private List<CourseTypeResponseDTO> courseTypes;

    public CourseTypeListDTO(List<CourseTypeResponseDTO> courseTypes) {
        this.courseTypes = courseTypes;
    }

    public List<CourseTypeResponseDTO> getCourseTypes() {
        Collections.sort(courseTypes);
        return courseTypes;
    }

    public void setCourseTypes(List<CourseTypeResponseDTO> courseTypes) {
        this.courseTypes = courseTypes;
    }
}
