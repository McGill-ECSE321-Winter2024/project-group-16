package ca.mcgill.ecse321.SportsSchedulePlus.dto.user.instructor;

import java.util.List;
import java.util.ArrayList;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

public class InstructorResponseDTO extends CustomerRequestDTO {

    private int id;
    private String experience;
    private List<Integer> instructorSuggestedCourseTypes;  
    private List<Integer> supervisedCourses;  

    public InstructorResponseDTO(Instructor instructor) {
        this.id = instructor.getId();
        this.experience = instructor.getExperience();
        this.instructorSuggestedCourseTypes = new ArrayList<>();
        for (CourseType courseType : instructor.getInstructorSuggestedCourseTypes()) {
            this.instructorSuggestedCourseTypes.add(courseType.getId());
        }

        this.supervisedCourses = new ArrayList<>();
        for (ScheduledCourse scheduledCourse : instructor.getSupervisedCourses()) {
            this.supervisedCourses.add(scheduledCourse.getId());
        }
    }

    public int getId() {
        return id;
    }

    public String getExperience() {
        return experience;
    }

    public List<Integer> getInstructorSuggestedCourseTypes() {
        return instructorSuggestedCourseTypes;
    }

    public List<Integer> getSupervisedCourses() {
        return supervisedCourses;
    }
}
