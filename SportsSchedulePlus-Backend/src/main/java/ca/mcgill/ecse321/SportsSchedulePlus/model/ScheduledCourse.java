package ca.mcgill.ecse321.SportsSchedulePlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.sql.Date;
import java.sql.Time;
import java.util.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

// line 40 "model.ump"
// line 119 "model.ump"
@Entity
public class ScheduledCourse
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ScheduledCourse Attributes
  @Id
  @GeneratedValue
  private int id;
  private Date date;
  private Time startTime;
  private Time endTime;
  private String location;

  //ScheduledCourse Associations
  @ManyToOne
  private CourseType courseType;
  @OneToMany
  private List<Payment> coursePayments;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public ScheduledCourse(){

  }
  public ScheduledCourse(int aId, Date aDate, Time aStartTime, Time aEndTime, String aLocation, CourseType aCourseType)
  {
    id = aId;
    date = aDate;
    startTime = aStartTime;
    endTime = aEndTime;
    location = aLocation;
    boolean didAddCourseType = setCourseType(aCourseType);
    if (!didAddCourseType)
    {
      throw new RuntimeException("Unable to create scheduledCourse due to courseType. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    coursePayments = new ArrayList<Payment>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartTime(Time aStartTime)
  {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndTime(Time aEndTime)
  {
    boolean wasSet = false;
    endTime = aEndTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setLocation(String aLocation)
  {
    boolean wasSet = false;
    location = aLocation;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }

  public Date getDate()
  {
    return date;
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public Time getEndTime()
  {
    return endTime;
  }

  public String getLocation()
  {
    return location;
  }
  /* Code from template association_GetOne */
  public CourseType getCourseType()
  {
    return courseType;
  }
  /* Code from template association_GetMany */
  public Payment getCoursePayment(int index)
  {
    Payment aCoursePayment = coursePayments.get(index);
    return aCoursePayment;
  }

  public List<Payment> getCoursePayments()
  {
    List<Payment> newCoursePayments = Collections.unmodifiableList(coursePayments);
    return newCoursePayments;
  }

  public int numberOfCoursePayments()
  {
    int number = coursePayments.size();
    return number;
  }

  public boolean hasCoursePayments()
  {
    boolean has = coursePayments.size() > 0;
    return has;
  }

  public int indexOfCoursePayment(Payment aCoursePayment)
  {
    int index = coursePayments.indexOf(aCoursePayment);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCourseType(CourseType aCourseType)
  {
    boolean wasSet = false;
    if (aCourseType == null)
    {
      return wasSet;
    }

    CourseType existingCourseType = courseType;
    courseType = aCourseType;
    if (existingCourseType != null && !existingCourseType.equals(aCourseType))
    {
      existingCourseType.removeScheduledCourse(this);
    }
    courseType.addScheduledCourse(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCoursePayments()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addCoursePayment(Payment aCoursePayment)
  {
    boolean wasAdded = false;
    if (coursePayments.contains(aCoursePayment)) { return false; }
    coursePayments.add(aCoursePayment);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCoursePayment(Payment aCoursePayment)
  {
    boolean wasRemoved = false;
    if (coursePayments.contains(aCoursePayment))
    {
      coursePayments.remove(aCoursePayment);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCoursePaymentAt(Payment aCoursePayment, int index)
  {  
    boolean wasAdded = false;
    if(addCoursePayment(aCoursePayment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCoursePayments()) { index = numberOfCoursePayments() - 1; }
      coursePayments.remove(aCoursePayment);
      coursePayments.add(index, aCoursePayment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCoursePaymentAt(Payment aCoursePayment, int index)
  {
    boolean wasAdded = false;
    if(coursePayments.contains(aCoursePayment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCoursePayments()) { index = numberOfCoursePayments() - 1; }
      coursePayments.remove(aCoursePayment);
      coursePayments.add(index, aCoursePayment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCoursePaymentAt(aCoursePayment, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    CourseType placeholderCourseType = courseType;
    this.courseType = null;
    if(placeholderCourseType != null)
    {
      placeholderCourseType.removeScheduledCourse(this);
    }
    coursePayments.clear();
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ScheduledCourse that = (ScheduledCourse) o;
      return Objects.equals(getLocation(), that.getLocation()) &&
              getId() == that.getId() &&
              getDate().compareTo(that.getDate()) == 0 &&
              getStartTime().compareTo(that.getStartTime()) == 0 &&
              getEndTime().compareTo(that.getEndTime()) == 0;
  }
  
  @Override
  public int hashCode() {
      return Objects.hash(getLocation(), getId(), getDate(), getStartTime(), getEndTime());
  }

  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "location" + ":" + getLocation()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endTime" + "=" + (getEndTime() != null ? !getEndTime().equals(this)  ? getEndTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "courseType = "+(getCourseType()!=null?Integer.toHexString(System.identityHashCode(getCourseType())):"null");
  }


  
}
