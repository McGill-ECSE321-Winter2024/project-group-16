package ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse;


import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;

public class ScheduledCourseRequestDTO {
    private int id;
    private String date;
    private String startTime;
    private String endTime;
    private String location;

    private int instructorId;

    private CourseTypeRequestDTO courseType;
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public CourseTypeRequestDTO getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseTypeRequestDTO courseType) {
        this.courseType = courseType;
    }
}
