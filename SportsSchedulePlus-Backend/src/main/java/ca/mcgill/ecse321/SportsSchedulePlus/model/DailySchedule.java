package ca.mcgill.ecse321.SportsSchedulePlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.sql.Time;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "openingTime" + "=" + (getOpeningTime() != null ? !getOpeningTime().equals(this)  ? getOpeningTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "closingTime" + "=" + (getClosingTime() != null ? !getClosingTime().equals(this)  ? getClosingTime().toString().replaceAll("  ","    ") : "this" : "null");
  }
}
