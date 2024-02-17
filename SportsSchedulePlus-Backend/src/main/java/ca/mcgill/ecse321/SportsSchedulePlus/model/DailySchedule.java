package ca.mcgill.ecse321.SportsSchedulePlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.sql.Time;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

// line 66 "model.ump"
// line 131 "model.ump"
@Entity
public class DailySchedule
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //DailySchedule Attributes
  @Id
  @GeneratedValue
  private int id;
  private Time openingTime;
  private Time closingTime;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public DailySchedule(){
    
  }
  public DailySchedule(Time aOpeningTime, Time aClosingTime)
  {
    openingTime = aOpeningTime;
    closingTime = aClosingTime;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setOpeningTime(Time aOpeningTime)
  {
    boolean wasSet = false;
    openingTime = aOpeningTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setClosingTime(Time aClosingTime)
  {
    boolean wasSet = false;
    closingTime = aClosingTime;
    wasSet = true;
    return wasSet;
  }

  public Time getOpeningTime()
  {
    return openingTime;
  }

  public Time getClosingTime()
  {
    return closingTime;
  }

  public void delete()
  {}

  @Override
  public boolean equals(Object obj) {
    System.out.println("check daily schedule");

    if (this == obj) {
      System.out.println("Objects are the same instance");
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      System.out.println("Objects are of different classes or null");
      return false;
    }

    DailySchedule that = (DailySchedule) obj;

    // Check equality for id
    if (id != that.id) {
      System.out.println("id mismatch: " + id + " != " + that.id);
      return false;
    }

    // Check equality for openingTime
    if (openingTime.compareTo(that.openingTime) != 0) {
      System.out.println("openingTime mismatch: " + openingTime + " != " + that.openingTime);
      return false;
    }

    // Check equality for closingTime
    if (closingTime.compareTo(that.closingTime) != 0) {
      System.out.println("closingTime mismatch: " + closingTime + " != " + that.closingTime);
      return false;
    }

    System.out.println("Objects are equal");
    return true;
  }

  private boolean compareTime(Time time1, Time time2) {
    if (time1 == null && time2 == null) return true;
    if (time1 == null || time2 == null) return false;

    return time1.compareTo(time2) == 0;
  }


  @Override
  public int hashCode() {
    return Objects.hash(id, openingTime, closingTime);
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "openingTime" + "=" + (getOpeningTime() != null ? !getOpeningTime().equals(this)  ? getOpeningTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "closingTime" + "=" + (getClosingTime() != null ? !getClosingTime().equals(this)  ? getClosingTime().toString().replaceAll("  ","    ") : "this" : "null");
  }
}