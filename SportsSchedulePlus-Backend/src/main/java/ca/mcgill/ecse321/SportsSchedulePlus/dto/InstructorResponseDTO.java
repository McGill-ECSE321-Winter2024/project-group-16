package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.List;
import java.util.ArrayList;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

public class InstructorResponseDTO extends CustomerResponseDTO {

    private int id;
    private String experience;
    private List<Integer> instructorSuggestedCourseTypes;  
    private List<Integer> supervisedCourses;  

    public InstructorResponseDTO(Instructor instructor) {
        this.id = instructor.getId();
        this.experience = instructor.getExperience();
        List<PaymentResponseDTO> payments = new ArrayList<>();
        for (Payment p : instructor.getCustomerPayments()) {
            payments.add(new PaymentResponseDTO(p));
        }
        List<ScheduledCourseDTO> courses = new ArrayList<>();
        for (ScheduledCourse c : instructor.getCoursesRegistered()) {
            courses.add(new ScheduledCourseDTO(c));
        }
        super.setCoursesRegistered(courses);
        super.setCustomerPayments(payments);
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
