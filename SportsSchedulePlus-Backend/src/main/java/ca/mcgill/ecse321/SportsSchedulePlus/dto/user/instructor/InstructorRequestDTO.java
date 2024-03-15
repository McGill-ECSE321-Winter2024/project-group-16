package ca.mcgill.ecse321.SportsSchedulePlus.dto.user.instructor;

import java.util.List;

public class  InstructorRequestDTO {

    private String name;
    private String email;
    private String password;
    private Integer id;
    private String experience;
    private List<Integer> instructorSuggestedCourseTypes;
    private List<Integer> supervisedCourses;

    // Constructors, getters, and setters

    public InstructorRequestDTO() {
        // Default constructor for JSON serialization
    }

    public InstructorRequestDTO(
            String name, String email, String password, Integer id,
            String experience, List<Integer> instructorSuggestedCourseTypes, List<Integer> supervisedCourses) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;
        this.experience = experience;
        this.instructorSuggestedCourseTypes = instructorSuggestedCourseTypes;
        this.supervisedCourses = supervisedCourses;
    }

    // Add getters and setters here

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public List<Integer> getInstructorSuggestedCourseTypes() {
        return instructorSuggestedCourseTypes;
    }

    public void setInstructorSuggestedCourseTypes(List<Integer> instructorSuggestedCourseTypes) {
        this.instructorSuggestedCourseTypes = instructorSuggestedCourseTypes;
    }

    public List<Integer> getSupervisedCourses() {
        return supervisedCourses;
    }

    public void setSupervisedCourses(List<Integer> supervisedCourses) {
        this.supervisedCourses = supervisedCourses;
    }
}
