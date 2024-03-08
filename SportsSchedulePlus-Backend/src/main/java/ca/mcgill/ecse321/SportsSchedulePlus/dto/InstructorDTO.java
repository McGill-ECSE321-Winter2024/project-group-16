package ca.mcgill.ecse321.SportsSchedulePlus.dto;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.util.*;

// line 30 "model.ump"
// line 103 "model.ump"
public class InstructorDTO extends CustomerDTO
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //InstructorDTO Attributes
  private String experience;

  //InstructorDTO Associations
  private List<CourseTypeDTO> instructorSuggestedCourseTypes;
  private List<ScheduledCourseDTO> supervisedCourses;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public InstructorDTO(int aId, String aExperience)
  {
  
    experience = aExperience;
    instructorSuggestedCourseTypes = new ArrayList<CourseTypeDTO>();
    supervisedCourses = new ArrayList<ScheduledCourseDTO>();
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
  public CourseTypeDTO getInstructorSuggestedCourseType(int index)
  {
    CourseTypeDTO aInstructorSuggestedCourseType = instructorSuggestedCourseTypes.get(index);
    return aInstructorSuggestedCourseType;
  }

  public List<CourseTypeDTO> getInstructorSuggestedCourseTypes()
  {
    List<CourseTypeDTO> newInstructorSuggestedCourseTypes = Collections.unmodifiableList(instructorSuggestedCourseTypes);
    return newInstructorSuggestedCourseTypes;
  }

  public int numberOfInstructorSuggestedCourseTypes()
  {
    int number = instructorSuggestedCourseTypes.size();
    return number;
  }

  public boolean hasInstructorSuggestedCourseTypes()
  {
    boolean has = instructorSuggestedCourseTypes.size() > 0;
    return has;
  }

  public int indexOfInstructorSuggestedCourseType(CourseTypeDTO aInstructorSuggestedCourseType)
  {
    int index = instructorSuggestedCourseTypes.indexOf(aInstructorSuggestedCourseType);
    return index;
  }
  /* Code from template association_GetMany */
  public ScheduledCourseDTO getSupervisedCourse(int index)
  {
    ScheduledCourseDTO aSupervisedCourse = supervisedCourses.get(index);
    return aSupervisedCourse;
  }

  public List<ScheduledCourseDTO> getSupervisedCourses()
  {
    List<ScheduledCourseDTO> newSupervisedCourses = Collections.unmodifiableList(supervisedCourses);
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

  public int indexOfSupervisedCourse(ScheduledCourseDTO aSupervisedCourse)
  {
    int index = supervisedCourses.indexOf(aSupervisedCourse);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfInstructorSuggestedCourseTypes()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addInstructorSuggestedCourseType(CourseTypeDTO aInstructorSuggestedCourseType)
  {
    boolean wasAdded = false;
    if (instructorSuggestedCourseTypes.contains(aInstructorSuggestedCourseType)) { return false; }
    instructorSuggestedCourseTypes.add(aInstructorSuggestedCourseType);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeInstructorSuggestedCourseType(CourseTypeDTO aInstructorSuggestedCourseType)
  {
    boolean wasRemoved = false;
    if (instructorSuggestedCourseTypes.contains(aInstructorSuggestedCourseType))
    {
      instructorSuggestedCourseTypes.remove(aInstructorSuggestedCourseType);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addInstructorSuggestedCourseTypeAt(CourseTypeDTO aInstructorSuggestedCourseType, int index)
  {  
    boolean wasAdded = false;
    if(addInstructorSuggestedCourseType(aInstructorSuggestedCourseType))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfInstructorSuggestedCourseTypes()) { index = numberOfInstructorSuggestedCourseTypes() - 1; }
      instructorSuggestedCourseTypes.remove(aInstructorSuggestedCourseType);
      instructorSuggestedCourseTypes.add(index, aInstructorSuggestedCourseType);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveInstructorSuggestedCourseTypeAt(CourseTypeDTO aInstructorSuggestedCourseType, int index)
  {
    boolean wasAdded = false;
    if(instructorSuggestedCourseTypes.contains(aInstructorSuggestedCourseType))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfInstructorSuggestedCourseTypes()) { index = numberOfInstructorSuggestedCourseTypes() - 1; }
      instructorSuggestedCourseTypes.remove(aInstructorSuggestedCourseType);
      instructorSuggestedCourseTypes.add(index, aInstructorSuggestedCourseType);
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
  public boolean addSupervisedCourse(ScheduledCourseDTO aSupervisedCourse)
  {
    boolean wasAdded = false;
    if (supervisedCourses.contains(aSupervisedCourse)) { return false; }
    supervisedCourses.add(aSupervisedCourse);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSupervisedCourse(ScheduledCourseDTO aSupervisedCourse)
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
  public boolean addSupervisedCourseAt(ScheduledCourseDTO aSupervisedCourse, int index)
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

  public boolean addOrMoveSupervisedCourseAt(ScheduledCourseDTO aSupervisedCourse, int index)
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




  public String toString()
  {
    return super.toString() + "["+
            "experience" + ":" + getExperience()+ "]";
  }
}