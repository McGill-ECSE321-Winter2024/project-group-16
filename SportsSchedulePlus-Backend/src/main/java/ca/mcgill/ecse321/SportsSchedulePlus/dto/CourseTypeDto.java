package ca.mcgill.ecse321.SportsSchedulePlus.dto;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.util.*;
import java.sql.Date;
import java.sql.Time;

// line 52 "model.ump"
// line 98 "model.ump"
public class CourseTypeDTO
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CourseTypeDTO Attributes
  private String description;
  private boolean approvedByOwner;
  private float price;

  //CourseTypeDTO Associations
  private List<ScheduledCourseDTO> scheduledCourses;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CourseTypeDTO(String aDescription, boolean aApprovedByOwner, float aPrice)
  {
    description = aDescription;
    approvedByOwner = aApprovedByOwner;
    price = aPrice;
    scheduledCourses = new ArrayList<ScheduledCourseDTO>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setApprovedByOwner(boolean aApprovedByOwner)
  {
    boolean wasSet = false;
    approvedByOwner = aApprovedByOwner;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(float aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public String getDescription()
  {
    return description;
  }

  public boolean getApprovedByOwner()
  {
    return approvedByOwner;
  }

  public float getPrice()
  {
    return price;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isApprovedByOwner()
  {
    return approvedByOwner;
  }
  /* Code from template association_GetMany */
  public ScheduledCourseDTO getScheduledCourse(int index)
  {
    ScheduledCourseDTO aScheduledCourse = scheduledCourses.get(index);
    return aScheduledCourse;
  }

  public List<ScheduledCourseDTO> getScheduledCourses()
  {
    List<ScheduledCourseDTO> newScheduledCourses = Collections.unmodifiableList(scheduledCourses);
    return newScheduledCourses;
  }

  public int numberOfScheduledCourses()
  {
    int number = scheduledCourses.size();
    return number;
  }

  public boolean hasScheduledCourses()
  {
    boolean has = scheduledCourses.size() > 0;
    return has;
  }

  public int indexOfScheduledCourse(ScheduledCourseDTO aScheduledCourse)
  {
    int index = scheduledCourses.indexOf(aScheduledCourse);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfScheduledCourses()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ScheduledCourseDTO addScheduledCourse(int aId, Date aDate, Time aStartTime, Time aEndTime, String aLocation)
  {
    return new ScheduledCourseDTO(aId, aDate, aStartTime, aEndTime, aLocation, this);
  }

  public boolean addScheduledCourse(ScheduledCourseDTO aScheduledCourse)
  {
    boolean wasAdded = false;
    if (scheduledCourses.contains(aScheduledCourse)) { return false; }
    CourseTypeDTO existingCourseType = aScheduledCourse.getCourseType();
    boolean isNewCourseType = existingCourseType != null && !this.equals(existingCourseType);
    if (isNewCourseType)
    {
      aScheduledCourse.setCourseType(this);
    }
    else
    {
      scheduledCourses.add(aScheduledCourse);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeScheduledCourse(ScheduledCourseDTO aScheduledCourse)
  {
    boolean wasRemoved = false;
    //Unable to remove aScheduledCourse, as it must always have a courseType
    if (!this.equals(aScheduledCourse.getCourseType()))
    {
      scheduledCourses.remove(aScheduledCourse);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addScheduledCourseAt(ScheduledCourseDTO aScheduledCourse, int index)
  {  
    boolean wasAdded = false;
    if(addScheduledCourse(aScheduledCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfScheduledCourses()) { index = numberOfScheduledCourses() - 1; }
      scheduledCourses.remove(aScheduledCourse);
      scheduledCourses.add(index, aScheduledCourse);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveScheduledCourseAt(ScheduledCourseDTO aScheduledCourse, int index)
  {
    boolean wasAdded = false;
    if(scheduledCourses.contains(aScheduledCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfScheduledCourses()) { index = numberOfScheduledCourses() - 1; }
      scheduledCourses.remove(aScheduledCourse);
      scheduledCourses.add(index, aScheduledCourse);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addScheduledCourseAt(aScheduledCourse, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (scheduledCourses.size() > 0)
    {
      ScheduledCourseDTO aScheduledCourse = scheduledCourses.get(scheduledCourses.size() - 1);
      aScheduledCourse.delete();
      scheduledCourses.remove(aScheduledCourse);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "description" + ":" + getDescription()+ "," +
            "approvedByOwner" + ":" + getApprovedByOwner()+ "," +
            "price" + ":" + getPrice()+ "]";
  }
}