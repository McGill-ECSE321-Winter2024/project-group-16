package ca.mcgill.ecse321.SportsSchedulePlus.dto;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.util.*;

// line 19 "model.ump"
// line 86 "model.ump"
public class OwnerDTO extends PersonRole
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //OwnerDTO Associations
  private List<ScheduledCourseDTO> approvedCourses;
  private List<CourseTypeDTO> ownerSuggestedCourses;
  private List<DailyScheduleResponseDTO> dailySchedules;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public OwnerDTO(int aId, DailyScheduleResponseDTO... allDailySchedules)
  {
    super(aId);
    approvedCourses = new ArrayList<ScheduledCourseDTO>();
    ownerSuggestedCourses = new ArrayList<CourseTypeDTO>();
    dailySchedules = new ArrayList<DailyScheduleResponseDTO>();
    boolean didAddDailySchedules = setDailySchedules(allDailySchedules);
    if (!didAddDailySchedules)
    {
      throw new RuntimeException("Unable to create OwnerDTO, must have 7 dailySchedules. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public ScheduledCourseDTO getApprovedCourse(int index)
  {
    ScheduledCourseDTO aApprovedCourse = approvedCourses.get(index);
    return aApprovedCourse;
  }

  public List<ScheduledCourseDTO> getApprovedCourses()
  {
    List<ScheduledCourseDTO> newApprovedCourses = Collections.unmodifiableList(approvedCourses);
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

  public int indexOfApprovedCourse(ScheduledCourseDTO aApprovedCourse)
  {
    int index = approvedCourses.indexOf(aApprovedCourse);
    return index;
  }
  /* Code from template association_GetMany */
  public CourseTypeDTO getOwnerSuggestedCourse(int index)
  {
    CourseTypeDTO aOwnerSuggestedCourse = ownerSuggestedCourses.get(index);
    return aOwnerSuggestedCourse;
  }

  public List<CourseTypeDTO> getOwnerSuggestedCourses()
  {
    List<CourseTypeDTO> newOwnerSuggestedCourses = Collections.unmodifiableList(ownerSuggestedCourses);
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

  public int indexOfOwnerSuggestedCourse(CourseTypeDTO aOwnerSuggestedCourse)
  {
    int index = ownerSuggestedCourses.indexOf(aOwnerSuggestedCourse);
    return index;
  }
  /* Code from template association_GetMany */
  public DailyScheduleResponseDTO getDailySchedule(int index)
  {
    DailyScheduleResponseDTO aDailySchedule = dailySchedules.get(index);
    return aDailySchedule;
  }

  public List<DailyScheduleResponseDTO> getDailySchedules()
  {
    List<DailyScheduleResponseDTO> newDailySchedules = Collections.unmodifiableList(dailySchedules);
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

  public int indexOfDailySchedule(DailyScheduleResponseDTO aDailySchedule)
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
  public boolean addApprovedCourse(ScheduledCourseDTO aApprovedCourse)
  {
    boolean wasAdded = false;
    if (approvedCourses.contains(aApprovedCourse)) { return false; }
    approvedCourses.add(aApprovedCourse);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeApprovedCourse(ScheduledCourseDTO aApprovedCourse)
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
  public boolean addApprovedCourseAt(ScheduledCourseDTO aApprovedCourse, int index)
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

  public boolean addOrMoveApprovedCourseAt(ScheduledCourseDTO aApprovedCourse, int index)
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
  public boolean addOwnerSuggestedCourse(CourseTypeDTO aOwnerSuggestedCourse)
  {
    boolean wasAdded = false;
    if (ownerSuggestedCourses.contains(aOwnerSuggestedCourse)) { return false; }
    ownerSuggestedCourses.add(aOwnerSuggestedCourse);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOwnerSuggestedCourse(CourseTypeDTO aOwnerSuggestedCourse)
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
  public boolean addOwnerSuggestedCourseAt(CourseTypeDTO aOwnerSuggestedCourse, int index)
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

  public boolean addOrMoveOwnerSuggestedCourseAt(CourseTypeDTO aOwnerSuggestedCourse, int index)
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
  public boolean setDailySchedules(DailyScheduleResponseDTO... newDailySchedules)
  {
    boolean wasSet = false;
    ArrayList<DailyScheduleResponseDTO> verifiedDailySchedules = new ArrayList<DailyScheduleResponseDTO>();
    for (DailyScheduleResponseDTO aDailySchedule : newDailySchedules)
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