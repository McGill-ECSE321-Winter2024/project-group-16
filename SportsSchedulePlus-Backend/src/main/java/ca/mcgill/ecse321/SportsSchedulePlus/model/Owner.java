/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.util.*;

import jakarta.persistence.Entity;

// line 19 "model.ump"
// line 92 "model.ump"
@Entity
public class Owner extends PersonRole
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Owner Associations
  private List<ScheduledCourse> approvedCourses;
  private List<CourseType> ownerSuggestedCourses;
  private List<DailySchedule> dailySchedules;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Owner(int aId, DailySchedule... allDailySchedules)
  {
    super(aId);
    approvedCourses = new ArrayList<ScheduledCourse>();
    ownerSuggestedCourses = new ArrayList<CourseType>();
    dailySchedules = new ArrayList<DailySchedule>();
    boolean didAddDailySchedules = setDailySchedules(allDailySchedules);
    if (!didAddDailySchedules)
    {
      throw new RuntimeException("Unable to create Owner, must have 7 dailySchedules. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public ScheduledCourse getApprovedCourse(int index)
  {
    ScheduledCourse aApprovedCourse = approvedCourses.get(index);
    return aApprovedCourse;
  }

  public List<ScheduledCourse> getApprovedCourses()
  {
    List<ScheduledCourse> newApprovedCourses = Collections.unmodifiableList(approvedCourses);
    return newApprovedCourses;
  }

  public int numberOfApprovedCourses()
  {
    int number = approvedCourses.size();
    return number;
  }

  public boolean hasApprovedCourses()
  {
    boolean has = approvedCourses.size() > 0;
    return has;
  }

  public int indexOfApprovedCourse(ScheduledCourse aApprovedCourse)
  {
    int index = approvedCourses.indexOf(aApprovedCourse);
    return index;
  }
  /* Code from template association_GetMany */
  public CourseType getOwnerSuggestedCourse(int index)
  {
    CourseType aOwnerSuggestedCourse = ownerSuggestedCourses.get(index);
    return aOwnerSuggestedCourse;
  }

  public List<CourseType> getOwnerSuggestedCourses()
  {
    List<CourseType> newOwnerSuggestedCourses = Collections.unmodifiableList(ownerSuggestedCourses);
    return newOwnerSuggestedCourses;
  }

  public int numberOfOwnerSuggestedCourses()
  {
    int number = ownerSuggestedCourses.size();
    return number;
  }

  public boolean hasOwnerSuggestedCourses()
  {
    boolean has = ownerSuggestedCourses.size() > 0;
    return has;
  }

  public int indexOfOwnerSuggestedCourse(CourseType aOwnerSuggestedCourse)
  {
    int index = ownerSuggestedCourses.indexOf(aOwnerSuggestedCourse);
    return index;
  }
  /* Code from template association_GetMany */
  public DailySchedule getDailySchedule(int index)
  {
    DailySchedule aDailySchedule = dailySchedules.get(index);
    return aDailySchedule;
  }

  public List<DailySchedule> getDailySchedules()
  {
    List<DailySchedule> newDailySchedules = Collections.unmodifiableList(dailySchedules);
    return newDailySchedules;
  }

  public int numberOfDailySchedules()
  {
    int number = dailySchedules.size();
    return number;
  }

  public boolean hasDailySchedules()
  {
    boolean has = dailySchedules.size() > 0;
    return has;
  }

  public int indexOfDailySchedule(DailySchedule aDailySchedule)
  {
    int index = dailySchedules.indexOf(aDailySchedule);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfApprovedCourses()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addApprovedCourse(ScheduledCourse aApprovedCourse)
  {
    boolean wasAdded = false;
    if (approvedCourses.contains(aApprovedCourse)) { return false; }
    approvedCourses.add(aApprovedCourse);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeApprovedCourse(ScheduledCourse aApprovedCourse)
  {
    boolean wasRemoved = false;
    if (approvedCourses.contains(aApprovedCourse))
    {
      approvedCourses.remove(aApprovedCourse);
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
      if(index > numberOfApprovedCourses()) { index = numberOfApprovedCourses() - 1; }
      approvedCourses.remove(aApprovedCourse);
      approvedCourses.add(index, aApprovedCourse);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveApprovedCourseAt(ScheduledCourse aApprovedCourse, int index)
  {
    boolean wasAdded = false;
    if(approvedCourses.contains(aApprovedCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfApprovedCourses()) { index = numberOfApprovedCourses() - 1; }
      approvedCourses.remove(aApprovedCourse);
      approvedCourses.add(index, aApprovedCourse);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addApprovedCourseAt(aApprovedCourse, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOwnerSuggestedCourses()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addOwnerSuggestedCourse(CourseType aOwnerSuggestedCourse)
  {
    boolean wasAdded = false;
    if (ownerSuggestedCourses.contains(aOwnerSuggestedCourse)) { return false; }
    ownerSuggestedCourses.add(aOwnerSuggestedCourse);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOwnerSuggestedCourse(CourseType aOwnerSuggestedCourse)
  {
    boolean wasRemoved = false;
    if (ownerSuggestedCourses.contains(aOwnerSuggestedCourse))
    {
      ownerSuggestedCourses.remove(aOwnerSuggestedCourse);
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
      if(index > numberOfOwnerSuggestedCourses()) { index = numberOfOwnerSuggestedCourses() - 1; }
      ownerSuggestedCourses.remove(aOwnerSuggestedCourse);
      ownerSuggestedCourses.add(index, aOwnerSuggestedCourse);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOwnerSuggestedCourseAt(CourseType aOwnerSuggestedCourse, int index)
  {
    boolean wasAdded = false;
    if(ownerSuggestedCourses.contains(aOwnerSuggestedCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOwnerSuggestedCourses()) { index = numberOfOwnerSuggestedCourses() - 1; }
      ownerSuggestedCourses.remove(aOwnerSuggestedCourse);
      ownerSuggestedCourses.add(index, aOwnerSuggestedCourse);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOwnerSuggestedCourseAt(aOwnerSuggestedCourse, index);
    }
    return wasAdded;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfDailySchedules()
  {
    return 7;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfDailySchedules()
  {
    return 7;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfDailySchedules()
  {
    return 7;
  }
  /* Code from template association_SetUnidirectionalN */
  public boolean setDailySchedules(DailySchedule... newDailySchedules)
  {
    boolean wasSet = false;
    ArrayList<DailySchedule> verifiedDailySchedules = new ArrayList<DailySchedule>();
    for (DailySchedule aDailySchedule : newDailySchedules)
    {
      if (verifiedDailySchedules.contains(aDailySchedule))
      {
        continue;
      }
      verifiedDailySchedules.add(aDailySchedule);
    }

    if (verifiedDailySchedules.size() != newDailySchedules.length || verifiedDailySchedules.size() != requiredNumberOfDailySchedules())
    {
      return wasSet;
    }

    dailySchedules.clear();
    dailySchedules.addAll(verifiedDailySchedules);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    approvedCourses.clear();
    ownerSuggestedCourses.clear();
    dailySchedules.clear();
    super.delete();
  }

}