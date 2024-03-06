package ca.mcgill.ecse321.SportsSchedulePlus.dto;

import java.util.List;

public class CourseTypeDto {

    private Integer id;
    private String description;
    private boolean approvedByOwner;
    private float price;
    private List<ScheduledCourseDto> scheduledCourses;

    public CourseTypeDto() {
        // Default constructor
    }

    public CourseTypeDto(Integer id, String description, boolean approvedByOwner, float price,
                         List<ScheduledCourseDto> scheduledCourses) {
        this.id = id;
        this.description = description;
        this.approvedByOwner = approvedByOwner;
        this.price = price;
        this.scheduledCourses = scheduledCourses;
    }

    // Getters and setters for each attribute

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApprovedByOwner() {
        return approvedByOwner;
    }

    public void setApprovedByOwner(boolean approvedByOwner) {
        this.approvedByOwner = approvedByOwner;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<ScheduledCourseDto> getScheduledCourses() {
        return scheduledCourses;
    }

    public void setScheduledCourses(List<ScheduledCourseDto> scheduledCourses) {
        this.scheduledCourses = scheduledCourses;
    }
}
