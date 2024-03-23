package ca.mcgill.ecse321.SportsSchedulePlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import java.util.*;

import ca.mcgill.ecse321.utils.Helper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.sql.Date;
import java.sql.Time;

// line 50 "model.ump"
// line 126 "model.ump"
@Entity
public class CourseType {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CourseType Attributes
  @Id
  @GeneratedValue
  private Integer id;
  @Column(unique = true)
  private String description;
  private boolean approvedByOwner;
  private float price;

  //CourseType Associations
  @OneToMany(fetch = FetchType.EAGER)
  private List <ScheduledCourse> scheduledCourses;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CourseType() {
    scheduledCourses = new ArrayList < ScheduledCourse > ();
  }
  public CourseType(String aDescription, boolean aApprovedByOwner, float aPrice) {
    description = aDescription;
    approvedByOwner = aApprovedByOwner;
    price = aPrice;
    scheduledCourses = new ArrayList < ScheduledCourse > ();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDescription(String aDescription) {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setApprovedByOwner(boolean aApprovedByOwner) {
    boolean wasSet = false;
    approvedByOwner = aApprovedByOwner;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(float aPrice) {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public String getDescription() {
    return description;
  }

  public boolean getApprovedByOwner() {
    return approvedByOwner;
  }

  public float getPrice() {
    return price;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /* Code from template attribute_IsBoolean */
  public boolean isApprovedByOwner() {
    return approvedByOwner;
  }
  /* Code from template association_GetMany */
  public ScheduledCourse getScheduledCourse(int index) {
    ScheduledCourse aScheduledCourse = scheduledCourses.get(index);
    return aScheduledCourse;
  }

  public List < ScheduledCourse > getScheduledCourses() {
    List < ScheduledCourse > newScheduledCourses = Collections.unmodifiableList(scheduledCourses);
    return newScheduledCourses;
  }

  public int numberOfScheduledCourses() {
    int number = scheduledCourses.size();
    return number;
  }

  public boolean hasScheduledCourses() {
    boolean has = scheduledCourses.size() > 0;
    return has;
  }

  public int indexOfScheduledCourse(ScheduledCourse aScheduledCourse) {
    int index = scheduledCourses.indexOf(aScheduledCourse);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfScheduledCourses() {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ScheduledCourse addScheduledCourse(int aId, Date aDate, Time aStartTime, Time aEndTime, String aLocation) {
    return new ScheduledCourse(aId, aDate, aStartTime, aEndTime, aLocation, this);
  }

  public boolean addScheduledCourse(ScheduledCourse aScheduledCourse) {
    boolean wasAdded = false;
    if (scheduledCourses.contains(aScheduledCourse)) {
      return false;
    }
    CourseType existingCourseType = aScheduledCourse.getCourseType();
    boolean isNewCourseType = existingCourseType != null && !this.equals(existingCourseType);
    if (isNewCourseType) {
      aScheduledCourse.setCourseType(this);
    } else {
      scheduledCourses.add(aScheduledCourse);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeScheduledCourse(ScheduledCourse aScheduledCourse) {
    boolean wasRemoved = false;
    //Unable to remove aScheduledCourse, as it must always have a courseType
    if (!this.equals(aScheduledCourse.getCourseType())) {
      scheduledCourses.remove(aScheduledCourse);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addScheduledCourseAt(ScheduledCourse aScheduledCourse, int index) {
    boolean wasAdded = false;
    if (addScheduledCourse(aScheduledCourse)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfScheduledCourses()) {
        index = numberOfScheduledCourses() - 1;
      }
      scheduledCourses.remove(aScheduledCourse);
      scheduledCourses.add(index, aScheduledCourse);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveScheduledCourseAt(ScheduledCourse aScheduledCourse, int index) {
    boolean wasAdded = false;
    if (scheduledCourses.contains(aScheduledCourse)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfScheduledCourses()) {
        index = numberOfScheduledCourses() - 1;
      }
      scheduledCourses.remove(aScheduledCourse);
      scheduledCourses.add(index, aScheduledCourse);
      wasAdded = true;
    } else {
      wasAdded = addScheduledCourseAt(aScheduledCourse, index);
    }
    return wasAdded;
  }

  public void delete() {
    while (scheduledCourses.size() > 0) {
      ScheduledCourse aScheduledCourse = scheduledCourses.get(scheduledCourses.size() - 1);
      aScheduledCourse.delete();
      scheduledCourses.remove(aScheduledCourse);
    }

  }

  public String toString() {
    return super.toString() + "[" +
      "description" + ":" + getDescription() + "," +
      "approvedByOwner" + ":" + getApprovedByOwner() + "," +
      "price" + ":" + getPrice() + "]";
  }

  /**
   * Compares this CourseType object with the specified object for equality.
   *
   * @param object The object to compare with this CourseType.
   * @return  true if the given object is equal to this CourseType; false otherwise.
   */
  @Override
  public boolean equals(Object object) {
    // Check if the compared object is the same instance
    if (this == object) return true;

    // Check if the compared object is of the same class
    if (object == null || getClass() != object.getClass()) return false;

    // Cast the compared object to CourseType for detailed attribute comparison
    CourseType other = (CourseType) object;

    // If both Lists of scheduled courses are empty, they are considered equal
    if (getScheduledCourses().isEmpty() && other.getScheduledCourses().isEmpty()) return true;

    // If the lists do not have the same elements, they are not considered equal
    if (!Helper.compareListsElementWise(getScheduledCourses(), other.getScheduledCourses())) return false;

    // Compare individual attributes for equality
    return getId() == other.getId() &&
      getDescription().equals(other.getDescription()) &&
      getPrice() == other.getPrice();
  }

  /**
   * Generates a hash code for this CourseType object based on its attributes and references.
   *
   * @return A hash code value for this CourseType.
   */
  @Override
  public int hashCode() {
    // Using Objects.hash to generate hash code based on selected attributes
    return Objects.hash(getId(), getDescription(), getPrice(), getScheduledCourses());
  }

}