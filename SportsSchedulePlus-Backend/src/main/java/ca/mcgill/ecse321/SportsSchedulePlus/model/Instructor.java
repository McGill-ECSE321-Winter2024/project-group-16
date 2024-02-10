/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.util.*;

// line 29 "model.ump"
// line 92 "model.ump"
public class Instructor extends Customer
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Instructor Attributes
  private String experience;

  //Instructor Associations
  private List<CourseType> instructorSuggestedCourseType;
  private List<ScheduledCourse> supervisedCourses;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Instructor(String aExperience)
  {
    super();
    experience = aExperience;
    instructorSuggestedCourseType = new ArrayList<CourseType>();
    supervisedCourses = new ArrayList<ScheduledCourse>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setExperience(String aExperience)
  {
    boolean wasSet = false;
    experience = aExperience;
    wasSet = true;
    return wasSet;
  }

  public String getExperience()
  {
    return experience;
  }
  /* Code from template association_GetMany */
  public CourseType getInstructorSuggestedCourseType(int index)
  {
    CourseType aInstructorSuggestedCourseType = instructorSuggestedCourseType.get(index);
    return aInstructorSuggestedCourseType;
  }

  public List<CourseType> getInstructorSuggestedCourseType()
  {
    List<CourseType> newInstructorSuggestedCourseType = Collections.unmodifiableList(instructorSuggestedCourseType);
    return newInstructorSuggestedCourseType;
  }

  public int numberOfInstructorSuggestedCourseType()
  {
    int number = instructorSuggestedCourseType.size();
    return number;
  }

  public boolean hasInstructorSuggestedCourseType()
  {
    boolean has = instructorSuggestedCourseType.size() > 0;
    return has;
  }

  public int indexOfInstructorSuggestedCourseType(CourseType aInstructorSuggestedCourseType)
  {
    int index = instructorSuggestedCourseType.indexOf(aInstructorSuggestedCourseType);
    return index;
  }
  /* Code from template association_GetMany */
  public ScheduledCourse getSupervisedCourse(int index)
  {
    ScheduledCourse aSupervisedCourse = supervisedCourses.get(index);
    return aSupervisedCourse;
  }

  public List<ScheduledCourse> getSupervisedCourses()
  {
    List<ScheduledCourse> newSupervisedCourses = Collections.unmodifiableList(supervisedCourses);
    return newSupervisedCourses;
  }

  public int numberOfSupervisedCourses()
  {
    int number = supervisedCourses.size();
    return number;
  }

  public boolean hasSupervisedCourses()
  {
    boolean has = supervisedCourses.size() > 0;
    return has;
  }

  public int indexOfSupervisedCourse(ScheduledCourse aSupervisedCourse)
  {
    int index = supervisedCourses.indexOf(aSupervisedCourse);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfInstructorSuggestedCourseType()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addInstructorSuggestedCourseType(CourseType aInstructorSuggestedCourseType)
  {
    boolean wasAdded = false;
    if (instructorSuggestedCourseType.contains(aInstructorSuggestedCourseType)) { return false; }
    instructorSuggestedCourseType.add(aInstructorSuggestedCourseType);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeInstructorSuggestedCourseType(CourseType aInstructorSuggestedCourseType)
  {
    boolean wasRemoved = false;
    if (instructorSuggestedCourseType.contains(aInstructorSuggestedCourseType))
    {
      instructorSuggestedCourseType.remove(aInstructorSuggestedCourseType);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addInstructorSuggestedCourseTypeAt(CourseType aInstructorSuggestedCourseType, int index)
  {  
    boolean wasAdded = false;
    if(addInstructorSuggestedCourseType(aInstructorSuggestedCourseType))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfInstructorSuggestedCourseType()) { index = numberOfInstructorSuggestedCourseType() - 1; }
      instructorSuggestedCourseType.remove(aInstructorSuggestedCourseType);
      instructorSuggestedCourseType.add(index, aInstructorSuggestedCourseType);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveInstructorSuggestedCourseTypeAt(CourseType aInstructorSuggestedCourseType, int index)
  {
    boolean wasAdded = false;
    if(instructorSuggestedCourseType.contains(aInstructorSuggestedCourseType))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfInstructorSuggestedCourseType()) { index = numberOfInstructorSuggestedCourseType() - 1; }
      instructorSuggestedCourseType.remove(aInstructorSuggestedCourseType);
      instructorSuggestedCourseType.add(index, aInstructorSuggestedCourseType);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addInstructorSuggestedCourseTypeAt(aInstructorSuggestedCourseType, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSupervisedCourses()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addSupervisedCourse(ScheduledCourse aSupervisedCourse)
  {
    boolean wasAdded = false;
    if (supervisedCourses.contains(aSupervisedCourse)) { return false; }
    supervisedCourses.add(aSupervisedCourse);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSupervisedCourse(ScheduledCourse aSupervisedCourse)
  {
    boolean wasRemoved = false;
    if (supervisedCourses.contains(aSupervisedCourse))
    {
      supervisedCourses.remove(aSupervisedCourse);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSupervisedCourseAt(ScheduledCourse aSupervisedCourse, int index)
  {  
    boolean wasAdded = false;
    if(addSupervisedCourse(aSupervisedCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSupervisedCourses()) { index = numberOfSupervisedCourses() - 1; }
      supervisedCourses.remove(aSupervisedCourse);
      supervisedCourses.add(index, aSupervisedCourse);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSupervisedCourseAt(ScheduledCourse aSupervisedCourse, int index)
  {
    boolean wasAdded = false;
    if(supervisedCourses.contains(aSupervisedCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSupervisedCourses()) { index = numberOfSupervisedCourses() - 1; }
      supervisedCourses.remove(aSupervisedCourse);
      supervisedCourses.add(index, aSupervisedCourse);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSupervisedCourseAt(aSupervisedCourse, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    instructorSuggestedCourseType.clear();
    supervisedCourses.clear();
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "experience" + ":" + getExperience()+ "]";
  }
}