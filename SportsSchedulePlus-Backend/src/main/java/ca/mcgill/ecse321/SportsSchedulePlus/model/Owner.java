/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.util.*;

import jakarta.persistence.Entity;

// line 18 "model.ump"
// line 79 "model.ump"
@Entity
public class Owner extends PersonRole
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Owner Associations
  private List<ScheduledCourse> approvedCourse;
  private List<CourseType> ownerSuggestedCourse;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Owner()
  {
    super();
    approvedCourse = new ArrayList<ScheduledCourse>();
    ownerSuggestedCourse = new ArrayList<CourseType>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public ScheduledCourse getApprovedCourse(int index)
  {
    ScheduledCourse aApprovedCourse = approvedCourse.get(index);
    return aApprovedCourse;
  }

  public List<ScheduledCourse> getApprovedCourse()
  {
    List<ScheduledCourse> newApprovedCourse = Collections.unmodifiableList(approvedCourse);
    return newApprovedCourse;
  }

  public int numberOfApprovedCourse()
  {
    int number = approvedCourse.size();
    return number;
  }

  public boolean hasApprovedCourse()
  {
    boolean has = approvedCourse.size() > 0;
    return has;
  }

  public int indexOfApprovedCourse(ScheduledCourse aApprovedCourse)
  {
    int index = approvedCourse.indexOf(aApprovedCourse);
    return index;
  }
  /* Code from template association_GetMany */
  public CourseType getOwnerSuggestedCourse(int index)
  {
    CourseType aOwnerSuggestedCourse = ownerSuggestedCourse.get(index);
    return aOwnerSuggestedCourse;
  }

  public List<CourseType> getOwnerSuggestedCourse()
  {
    List<CourseType> newOwnerSuggestedCourse = Collections.unmodifiableList(ownerSuggestedCourse);
    return newOwnerSuggestedCourse;
  }

  public int numberOfOwnerSuggestedCourse()
  {
    int number = ownerSuggestedCourse.size();
    return number;
  }

  public boolean hasOwnerSuggestedCourse()
  {
    boolean has = ownerSuggestedCourse.size() > 0;
    return has;
  }

  public int indexOfOwnerSuggestedCourse(CourseType aOwnerSuggestedCourse)
  {
    int index = ownerSuggestedCourse.indexOf(aOwnerSuggestedCourse);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfApprovedCourse()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addApprovedCourse(ScheduledCourse aApprovedCourse)
  {
    boolean wasAdded = false;
    if (approvedCourse.contains(aApprovedCourse)) { return false; }
    approvedCourse.add(aApprovedCourse);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeApprovedCourse(ScheduledCourse aApprovedCourse)
  {
    boolean wasRemoved = false;
    if (approvedCourse.contains(aApprovedCourse))
    {
      approvedCourse.remove(aApprovedCourse);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addApprovedCourseAt(ScheduledCourse aApprovedCourse, int index)
  {  
    boolean wasAdded = false;
    if(addApprovedCourse(aApprovedCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfApprovedCourse()) { index = numberOfApprovedCourse() - 1; }
      approvedCourse.remove(aApprovedCourse);
      approvedCourse.add(index, aApprovedCourse);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveApprovedCourseAt(ScheduledCourse aApprovedCourse, int index)
  {
    boolean wasAdded = false;
    if(approvedCourse.contains(aApprovedCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfApprovedCourse()) { index = numberOfApprovedCourse() - 1; }
      approvedCourse.remove(aApprovedCourse);
      approvedCourse.add(index, aApprovedCourse);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addApprovedCourseAt(aApprovedCourse, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOwnerSuggestedCourse()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addOwnerSuggestedCourse(CourseType aOwnerSuggestedCourse)
  {
    boolean wasAdded = false;
    if (ownerSuggestedCourse.contains(aOwnerSuggestedCourse)) { return false; }
    ownerSuggestedCourse.add(aOwnerSuggestedCourse);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOwnerSuggestedCourse(CourseType aOwnerSuggestedCourse)
  {
    boolean wasRemoved = false;
    if (ownerSuggestedCourse.contains(aOwnerSuggestedCourse))
    {
      ownerSuggestedCourse.remove(aOwnerSuggestedCourse);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOwnerSuggestedCourseAt(CourseType aOwnerSuggestedCourse, int index)
  {  
    boolean wasAdded = false;
    if(addOwnerSuggestedCourse(aOwnerSuggestedCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOwnerSuggestedCourse()) { index = numberOfOwnerSuggestedCourse() - 1; }
      ownerSuggestedCourse.remove(aOwnerSuggestedCourse);
      ownerSuggestedCourse.add(index, aOwnerSuggestedCourse);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOwnerSuggestedCourseAt(CourseType aOwnerSuggestedCourse, int index)
  {
    boolean wasAdded = false;
    if(ownerSuggestedCourse.contains(aOwnerSuggestedCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOwnerSuggestedCourse()) { index = numberOfOwnerSuggestedCourse() - 1; }
      ownerSuggestedCourse.remove(aOwnerSuggestedCourse);
      ownerSuggestedCourse.add(index, aOwnerSuggestedCourse);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOwnerSuggestedCourseAt(aOwnerSuggestedCourse, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    approvedCourse.clear();
    ownerSuggestedCourse.clear();
    super.delete();
  }

}