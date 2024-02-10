/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.util.*;

import jakarta.persistence.Entity;

import java.sql.Date;
import java.sql.Time;

// line 51 "model.ump"
// line 124 "model.ump"
@Entity
public class CourseType
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, CourseType> coursetypesByDescription = new HashMap<String, CourseType>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CourseType Attributes
  private String description;
  private boolean approvedByOwner;
  private float price;

  //CourseType Associations
  private List<ScheduledCourse> scheduledCourses;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CourseType(String aDescription, boolean aApprovedByOwner, float aPrice)
  {
    approvedByOwner = aApprovedByOwner;
    price = aPrice;
    if (!setDescription(aDescription))
    {
      throw new RuntimeException("Cannot create due to duplicate description. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    scheduledCourses = new ArrayList<ScheduledCourse>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    String anOldDescription = getDescription();
    if (anOldDescription != null && anOldDescription.equals(aDescription)) {
      return true;
    }
    if (hasWithDescription(aDescription)) {
      return wasSet;
    }
    description = aDescription;
    wasSet = true;
    if (anOldDescription != null) {
      coursetypesByDescription.remove(anOldDescription);
    }
    coursetypesByDescription.put(aDescription, this);
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
  /* Code from template attribute_GetUnique */
  public static CourseType getWithDescription(String aDescription)
  {
    return coursetypesByDescription.get(aDescription);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithDescription(String aDescription)
  {
    return getWithDescription(aDescription) != null;
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
  public ScheduledCourse getScheduledCourse(int index)
  {
    ScheduledCourse aScheduledCourse = scheduledCourse.get(index);
    return aScheduledCourse;
  }

  public List<ScheduledCourse> getScheduledCourse()
  {
    List<ScheduledCourse> newScheduledCourse = Collections.unmodifiableList(scheduledCourse);
    return newScheduledCourse;
  }

  public int numberOfScheduledCourse()
  {
    int number = scheduledCourses.size();
    return number;
  }

  public boolean hasScheduledCourse()
  {
    boolean has = scheduledCourses.size() > 0;
    return has;
  }

  public int indexOfScheduledCourse(ScheduledCourse aScheduledCourse)
  {
    int index = scheduledCourses.indexOf(aScheduledCourse);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfScheduledCourse()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ScheduledCourse addScheduledCourse(int aId, Date aDate, Time aStartTime, Time aEndTime, String aLocation)
  {
    return new ScheduledCourse(aId, aDate, aStartTime, aEndTime, aLocation, this);
  }

  public boolean addScheduledCourse(ScheduledCourse aScheduledCourse)
  {
    boolean wasAdded = false;
    if (scheduledCourses.contains(aScheduledCourse)) { return false; }
    CourseType existingCourseType = aScheduledCourse.getCourseType();
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

  public boolean removeScheduledCourse(ScheduledCourse aScheduledCourse)
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
  public boolean addScheduledCourseAt(ScheduledCourse aScheduledCourse, int index)
  {  
    boolean wasAdded = false;
    if(addScheduledCourse(aScheduledCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfScheduledCourse()) { index = numberOfScheduledCourse() - 1; }
      scheduledCourses.remove(aScheduledCourse);
      scheduledCourses.add(index, aScheduledCourse);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveScheduledCourseAt(ScheduledCourse aScheduledCourse, int index)
  {
    boolean wasAdded = false;
    if(scheduledCourses.contains(aScheduledCourse))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfScheduledCourse()) { index = numberOfScheduledCourse() - 1; }
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
    coursetypesByDescription.remove(getDescription());
    while (scheduledCourses.size() > 0)
    {
      ScheduledCourse aScheduledCourse = scheduledCourses.get(scheduledCourses.size() - 1);
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