package ca.mcgill.ecse321.SportsSchedulePlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


// line 40 "model.ump"
// line 119 "model.ump"
@Entity
public class ScheduledCourse {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ScheduledCourse Attributes
  @Id
  @GeneratedValue
  private int id;
  private Date date;
  private Time startTime;
  private Time endTime;
  private String location;

  //ScheduledCourse Associations
  @ManyToOne
  private CourseType courseType;
 

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public ScheduledCourse() {

  }

  // Constructor to copy values from a Course
  public ScheduledCourse(ScheduledCourse sourceCourse) {
    this.id = sourceCourse.getId();
    this.date = sourceCourse.getDate();
    this.startTime = sourceCourse.getStartTime();
    this.endTime = sourceCourse.getEndTime();
    this.location = sourceCourse.getLocation();
    this.courseType = sourceCourse.getCourseType();
  }
  
  public ScheduledCourse(int aId, Date aDate, Time aStartTime, Time aEndTime, String aLocation, CourseType aCourseType) {
    id = aId;
    date = aDate;
    startTime = aStartTime;
    endTime = aEndTime;
    location = aLocation;
    boolean didAddCourseType = setCourseType(aCourseType);
    if (!didAddCourseType) {
      throw new RuntimeException("Unable to create scheduledCourse due to courseType. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(int aId) {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(Date aDate) {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartTime(Time aStartTime) {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndTime(Time aEndTime) {
    boolean wasSet = false;
    endTime = aEndTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setLocation(String aLocation) {
    boolean wasSet = false;
    location = aLocation;
    wasSet = true;
    return wasSet;
  }

  public int getId() {
    return id;
  }

  public Date getDate() {
    return date;
  }

  public Time getStartTime() {
    return startTime;
  }

  public Time getEndTime() {
    return endTime;
  }

  public String getLocation() {
    return location;
  }
  /* Code from template association_GetOne */
  public CourseType getCourseType() {
    return courseType;
  }
 

  /* Code from template association_SetOneToMany */
  public boolean setCourseType(CourseType aCourseType) {
    boolean wasSet = false;
    if (aCourseType == null) {
      return wasSet;
    }
    this.courseType = aCourseType;
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCoursePayments() {
    return 0;
  }
 

  public void delete() {
    this.courseType = null;
  }

  public String toString() {
    return super.toString() + "[" +
      "id" + ":" + getId() + "," +
      "location" + ":" + getLocation() + "]" + System.getProperties().getProperty("line.separator") +
      "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this) ? getDate().toString().replaceAll("  ", "    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
      "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this) ? getStartTime().toString().replaceAll("  ", "    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
      "  " + "endTime" + "=" + (getEndTime() != null ? !getEndTime().equals(this) ? getEndTime().toString().replaceAll("  ", "    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
      "  " + "courseType = " + (getCourseType() != null ? Integer.toHexString(System.identityHashCode(getCourseType())) : "null");
  }

  /**
   * Compares this ScheduledCourse object with the specified object for equality.
   *
   * @param object The object to compare with this ScheduledCourse.
   * @return true if the given object is equal to this ScheduledCourse; false otherwise.
   */
  @Override
  public boolean equals(Object object) {
    // Check if the compared object is the same instance
    if (this == object) return true;

    // Check if the compared object is of the same class
    if (object == null || getClass() != object.getClass()) return false;

    // Cast the compared object to ScheduledCourse for detailed attribute comparison
    ScheduledCourse other = (ScheduledCourse) object;

    // Compare individual attributes for equality
    return getLocation().equals(other.getLocation()) &&
      getId() == other.getId() &&
      getDate().compareTo(other.getDate()) == 0 &&
      getStartTime().compareTo(other.getStartTime()) == 0 &&
      getEndTime().compareTo(other.getEndTime()) == 0;
  }

  /**
   * Generates a hash code for this ScheduledCourse object based on its attributes.
   *
   * @return A hash code value for this ScheduledCourse.
   */
  @Override
  public int hashCode() {
    // Using Objects.hash to generate hash code based on selected attributes
    return Objects.hash(getLocation(), getId(), getDate(), getStartTime(), getEndTime());
  }

}