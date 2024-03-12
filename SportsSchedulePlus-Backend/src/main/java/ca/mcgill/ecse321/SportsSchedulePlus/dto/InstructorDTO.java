package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

public class InstructorDTO extends CustomerDTO {

    private String experience;
 

    public InstructorDTO() {

    }

    public InstructorDTO(String experience) {
        this.experience = experience;
    }

    public InstructorDTO(Instructor instructor) {
        super(instructor.getId());
        this.experience = instructor.getExperience();
        List<PaymentDTO> payments = new ArrayList<>();
        for (Payment p : instructor.getCustomerPayments()) {
            payments.add(new PaymentDTO(p));
        }
        List<ScheduledCourseDTO> courses = new ArrayList<>();
        for (ScheduledCourse c : instructor.getCoursesRegistered()) {
            courses.add(new ScheduledCourseDTO(c));
        }
        super.setCoursesRegistered(courses);
        super.setCustomerPayments(payments);
    }


    public String getExperience() {
        return experience;
    }
}
