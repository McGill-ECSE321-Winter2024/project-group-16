package ca.mcgill.ecse321.SportsSchedulePlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import java.util.*;

import ca.mcgill.ecse321.utils.Helper;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

// line 19 "model.ump"
// line 88 "model.ump"
@Entity
public class Owner extends PersonRole {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Owner Associations
  @OneToMany(fetch = FetchType.EAGER)
  private List <CourseType> approvedCourses;
  @OneToMany(fetch = FetchType.EAGER)
  private List <CourseType> ownerSuggestedCourses;
  @OneToMany(fetch = FetchType.EAGER)
  private List <DailySchedule> dailySchedule;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Owner() {
    approvedCourses = new ArrayList <CourseType> ();
    ownerSuggestedCourses = new ArrayList <CourseType> ();
  }
  public Owner(int aId, List <DailySchedule> aDailySchedule) {
    super(aId);
    approvedCourses = new ArrayList <CourseType> ();
    ownerSuggestedCourses = new ArrayList <CourseType> ();
    if (!setDailySchedule(aDailySchedule)) {
      throw new RuntimeException("Unable to create Owner due to aDailySchedule. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public CourseType getApprovedCourse(int index) {
    CourseType aApprovedCourse = approvedCourses.get(index);
    return aApprovedCourse;
  }

  public List <CourseType> getApprovedCourses() {
    List <CourseType> newApprovedCourses = Collections.unmodifiableList(approvedCourses);
    return newApprovedCourses;
  }

  public int numberOfApprovedCourses() {
    int number = approvedCourses.size();
    return number;
  }

  public boolean hasApprovedCourses() {
    boolean has = approvedCourses.size() > 0;
    return has;
  }

  public int indexOfApprovedCourse(CourseType aApprovedCourse) {
    int index = approvedCourses.indexOf(aApprovedCourse);
    return index;
  }
  /* Code from template association_GetMany */
  public CourseType getOwnerSuggestedCourse(int index) {
    CourseType aOwnerSuggestedCourse = ownerSuggestedCourses.get(index);
    return aOwnerSuggestedCourse;
  }

  public List <CourseType> getOwnerSuggestedCourses() {
    List <CourseType> newOwnerSuggestedCourses = Collections.unmodifiableList(ownerSuggestedCourses);
    return newOwnerSuggestedCourses;
  }

  public int numberOfOwnerSuggestedCourses() {
    int number = ownerSuggestedCourses.size();
    return number;
  }

  public boolean hasOwnerSuggestedCourses() {
    boolean has = ownerSuggestedCourses.size() > 0;
    return has;
  }

  public int indexOfOwnerSuggestedCourse(CourseType aOwnerSuggestedCourse) {
    int index = ownerSuggestedCourses.indexOf(aOwnerSuggestedCourse);
    return index;
  }
  /* Code from template association_GetOne */
  public List<DailySchedule> getDailySchedule() {
    return dailySchedule;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfApprovedCourses() {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addApprovedCourse(CourseType aApprovedCourse) {
    boolean wasAdded = false;
    if (approvedCourses.contains(aApprovedCourse)) {
      return false;
    }
    approvedCourses.add(aApprovedCourse);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeApprovedCourse(CourseType aApprovedCourse) {
    boolean wasRemoved = false;
    if (approvedCourses.contains(aApprovedCourse)) {
      approvedCourses.remove(aApprovedCourse);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addApprovedCourseAt(CourseType aApprovedCourse, int index) {
    boolean wasAdded = false;
    if (addApprovedCourse(aApprovedCourse)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfApprovedCourses()) {
        index = numberOfApprovedCourses() - 1;
      }
      approvedCourses.remove(aApprovedCourse);
      approvedCourses.add(index, aApprovedCourse);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveApprovedCourseAt(CourseType aApprovedCourse, int index) {
    boolean wasAdded = false;
    if (approvedCourses.contains(aApprovedCourse)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfApprovedCourses()) {
        index = numberOfApprovedCourses() - 1;
      }
      approvedCourses.remove(aApprovedCourse);
      approvedCourses.add(index, aApprovedCourse);
      wasAdded = true;
    } else {
      wasAdded = addApprovedCourseAt(aApprovedCourse, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOwnerSuggestedCourses() {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addOwnerSuggestedCourse(CourseType aOwnerSuggestedCourse) {
    boolean wasAdded = false;
    if (ownerSuggestedCourses.contains(aOwnerSuggestedCourse)) {
      return false;
    }
    ownerSuggestedCourses.add(aOwnerSuggestedCourse);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOwnerSuggestedCourse(CourseType aOwnerSuggestedCourse) {
    boolean wasRemoved = false;
    if (ownerSuggestedCourses.contains(aOwnerSuggestedCourse)) {
      ownerSuggestedCourses.remove(aOwnerSuggestedCourse);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOwnerSuggestedCourseAt(CourseType aOwnerSuggestedCourse, int index) {
    boolean wasAdded = false;
    if (addOwnerSuggestedCourse(aOwnerSuggestedCourse)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfOwnerSuggestedCourses()) {
        index = numberOfOwnerSuggestedCourses() - 1;
      }
      ownerSuggestedCourses.remove(aOwnerSuggestedCourse);
      ownerSuggestedCourses.add(index, aOwnerSuggestedCourse);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOwnerSuggestedCourseAt(CourseType aOwnerSuggestedCourse, int index) {
    boolean wasAdded = false;
    if (ownerSuggestedCourses.contains(aOwnerSuggestedCourse)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfOwnerSuggestedCourses()) {
        index = numberOfOwnerSuggestedCourses() - 1;
      }
      ownerSuggestedCourses.remove(aOwnerSuggestedCourse);
      ownerSuggestedCourses.add(index, aOwnerSuggestedCourse);
      wasAdded = true;
    } else {
      wasAdded = addOwnerSuggestedCourseAt(aOwnerSuggestedCourse, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setDailySchedule(List<DailySchedule> aNewDailySchedule) {
    boolean wasSet = false;
    if (aNewDailySchedule != null && aNewDailySchedule.size() == 7) {
      dailySchedule = aNewDailySchedule;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete() {
    approvedCourses.clear();
    ownerSuggestedCourses.clear();
    dailySchedule = null;
    super.delete();
  }

  public String toString() {
    return super.toString() + "[" +
      "id" + ":" + getId() + "]";
  }

  /**
   * Overrides the equals method to compare two Owner objects for equality.
   *
   * @param object The object to compare with.
   * @return True if the objects are equal, false otherwise.
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;

    Owner other = (Owner) object;

    return getId() == other.getId() &&
      Helper.compareListsElementWise(approvedCourses, other.approvedCourses) &&
      Helper.compareListsElementWise(ownerSuggestedCourses, other.ownerSuggestedCourses) &&
      dailySchedule.equals(other.dailySchedule);
  }

  /**
   * Overrides the hashCode method to generate a hash code based on
   * id, approvedCourses, ownerSuggestedCourses, and dailySchedule.
   *
   * @return The hash code for the object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(),getId(), approvedCourses, ownerSuggestedCourses, dailySchedule);
  }

}