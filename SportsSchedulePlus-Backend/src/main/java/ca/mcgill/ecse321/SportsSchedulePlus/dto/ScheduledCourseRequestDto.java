package ca.mcgill.ecse321.SportsSchedulePlus.dto;


public class ScheduledCourseRequestDto {
    private int id;
    private String date;
    private String startTime;
    private String endTime;
    private String location;

    private CourseTypeRequestDto courseType; // Assuming you have a CourseTypeRequestDto

    // Add any other fields or methods as needed

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

    public CourseTypeRequestDto getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseTypeRequestDto courseType) {
        this.courseType = courseType;
    }
}
