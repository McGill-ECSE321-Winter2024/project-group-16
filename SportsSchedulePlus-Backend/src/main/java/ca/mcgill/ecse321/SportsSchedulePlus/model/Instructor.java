package ca.mcgill.ecse321.SportsSchedulePlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import java.util.*;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import ca.mcgill.ecse321.utils.Helper;

// line 29 "model.ump"
// line 105 "model.ump"
@Entity
public class Instructor extends Customer {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Instructor Attributes
  private String experience;

  //Instructor Associations
  @OneToMany(fetch = FetchType.EAGER)
  private List <CourseType> instructorSuggestedCourseTypes;
  @ManyToMany(fetch = FetchType.EAGER)
  private List <ScheduledCourse> supervisedCourses;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public Instructor() {

  }
  public Instructor(int aId, String aExperience) {
    super(aId);
    experience = aExperience;
    instructorSuggestedCourseTypes = new ArrayList <CourseType> ();
    supervisedCourses = new ArrayList <ScheduledCourse> ();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public Instructor(String experience) {
  this.experience = experience;
}

public Instructor(Customer customer, String experience) {
    // Copy applied boolean from the customer
    super.setHasApplied(customer.getHasApplied());
    // Copy arrays from the customer
    super.setCoursesRegistered(customer.getCoursesRegistered());
    super.setCustomerPayments(customer.getCustomerPayments());
    // Set fields specific to the Instructor
    this.experience = experience;
    this.instructorSuggestedCourseTypes = new ArrayList<>();
    this.supervisedCourses = new ArrayList<>();
}

public boolean setExperience(String aExperience) {
    boolean wasSet = false;
    experience = aExperience;
    wasSet = true;
    return wasSet;
  }

  public String getExperience() {
    return experience;
  }
  /* Code from template association_GetMany */
  public CourseType getInstructorSuggestedCourseType(int index) {
    CourseType aInstructorSuggestedCourseType = instructorSuggestedCourseTypes.get(index);
    return aInstructorSuggestedCourseType;
  }

  public List <CourseType> getInstructorSuggestedCourseTypes() {
    List <CourseType> newInstructorSuggestedCourseTypes = Collections.unmodifiableList(instructorSuggestedCourseTypes);
    return newInstructorSuggestedCourseTypes;
  }

  public int numberOfInstructorSuggestedCourseTypes() {
    int number = instructorSuggestedCourseTypes.size();
    return number;
  }

  public boolean hasInstructorSuggestedCourseTypes() {
    boolean has = instructorSuggestedCourseTypes.size() > 0;
    return has;
  }

  public int indexOfInstructorSuggestedCourseType(CourseType aInstructorSuggestedCourseType) {
    int index = instructorSuggestedCourseTypes.indexOf(aInstructorSuggestedCourseType);
    return index;
  }
  /* Code from template association_GetMany */
  public ScheduledCourse getSupervisedCourse(int index) {
    ScheduledCourse aSupervisedCourse = supervisedCourses.get(index);
    return aSupervisedCourse;
  }

  public List <ScheduledCourse> getSupervisedCourses() {
    List <ScheduledCourse> newSupervisedCourses = Collections.unmodifiableList(supervisedCourses);
    return newSupervisedCourses;
  }

  public int numberOfSupervisedCourses() {
    int number = supervisedCourses.size();
    return number;
  }

  public boolean hasSupervisedCourses() {
    boolean has = supervisedCourses.size() > 0;
    return has;
  }

  public int indexOfSupervisedCourse(ScheduledCourse aSupervisedCourse) {
    int index = supervisedCourses.indexOf(aSupervisedCourse);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfInstructorSuggestedCourseTypes() {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addInstructorSuggestedCourseType(CourseType aInstructorSuggestedCourseType) {
    boolean wasAdded = false;
    if (instructorSuggestedCourseTypes.contains(aInstructorSuggestedCourseType)) {
      return false;
    }
    instructorSuggestedCourseTypes.add(aInstructorSuggestedCourseType);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeInstructorSuggestedCourseType(CourseType aInstructorSuggestedCourseType) {
    boolean wasRemoved = false;
    if (instructorSuggestedCourseTypes.contains(aInstructorSuggestedCourseType)) {
      instructorSuggestedCourseTypes.remove(aInstructorSuggestedCourseType);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addInstructorSuggestedCourseTypeAt(CourseType aInstructorSuggestedCourseType, int index) {
    boolean wasAdded = false;
    if (addInstructorSuggestedCourseType(aInstructorSuggestedCourseType)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfInstructorSuggestedCourseTypes()) {
        index = numberOfInstructorSuggestedCourseTypes() - 1;
      }
      instructorSuggestedCourseTypes.remove(aInstructorSuggestedCourseType);
      instructorSuggestedCourseTypes.add(index, aInstructorSuggestedCourseType);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveInstructorSuggestedCourseTypeAt(CourseType aInstructorSuggestedCourseType, int index) {
    boolean wasAdded = false;
    if (instructorSuggestedCourseTypes.contains(aInstructorSuggestedCourseType)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfInstructorSuggestedCourseTypes()) {
        index = numberOfInstructorSuggestedCourseTypes() - 1;
      }
      instructorSuggestedCourseTypes.remove(aInstructorSuggestedCourseType);
      instructorSuggestedCourseTypes.add(index, aInstructorSuggestedCourseType);
      wasAdded = true;
    } else {
      wasAdded = addInstructorSuggestedCourseTypeAt(aInstructorSuggestedCourseType, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSupervisedCourses() {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addSupervisedCourse(ScheduledCourse aSupervisedCourse) {
    boolean wasAdded = false;
    if (supervisedCourses.contains(aSupervisedCourse)) {
      return false;
    }
    supervisedCourses.add(aSupervisedCourse);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSupervisedCourse(ScheduledCourse aSupervisedCourse) {
    boolean wasRemoved = false;
    if (supervisedCourses.contains(aSupervisedCourse)) {
      supervisedCourses.remove(aSupervisedCourse);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSupervisedCourseAt(ScheduledCourse aSupervisedCourse, int index) {
    boolean wasAdded = false;
    if (addSupervisedCourse(aSupervisedCourse)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfSupervisedCourses()) {
        index = numberOfSupervisedCourses() - 1;
      }
      supervisedCourses.remove(aSupervisedCourse);
      supervisedCourses.add(index, aSupervisedCourse);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSupervisedCourseAt(ScheduledCourse aSupervisedCourse, int index) {
    boolean wasAdded = false;
    if (supervisedCourses.contains(aSupervisedCourse)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfSupervisedCourses()) {
        index = numberOfSupervisedCourses() - 1;
      }
      supervisedCourses.remove(aSupervisedCourse);
      supervisedCourses.add(index, aSupervisedCourse);
      wasAdded = true;
    } else {
      wasAdded = addSupervisedCourseAt(aSupervisedCourse, index);
    }
    return wasAdded;
  }

  public void delete() {
    instructorSuggestedCourseTypes.clear();
    supervisedCourses.clear();
    super.delete();
  }

  public String toString() {
    return super.toString() + "[" +
      "experience" + ":" + getExperience() + "]";
  }

  /**
   * Compares this Instructor object with the specified object for equality.
   *
   * @param object The object to compare with this Instructor.
   * @return true if the given object is equal to this Instructor; false otherwise.
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof Instructor other) || !super.equals(object)) return false;

    return getId() == other.getId() &&
      (Objects.equals(experience, other.experience)) &&
      Helper.compareListsElementWise(instructorSuggestedCourseTypes, other.instructorSuggestedCourseTypes) &&
      Helper.compareListsElementWise(supervisedCourses, other.supervisedCourses);
  }

  /**
   * Generates a hash code for this Instructor object based on its attributes.
   *
   * @return A hash code value for this Instructor.
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getId(), experience, instructorSuggestedCourseTypes, supervisedCourses);
  }

}