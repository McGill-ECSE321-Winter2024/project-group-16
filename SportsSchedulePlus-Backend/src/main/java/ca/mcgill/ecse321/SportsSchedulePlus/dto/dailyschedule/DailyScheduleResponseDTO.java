package ca.mcgill.ecse321.SportsSchedulePlus.dto.dailyschedule;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;

import java.sql.Time;

// line 69 "model.ump"
// line 121 "model.ump"
public class DailyScheduleResponseDTO
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //DailyScheduleDTO Attributes
  private int id;
  private Time openingTime;
  private Time closingTime;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public DailyScheduleResponseDTO(int aId, Time aOpeningTime, Time aClosingTime)
  {
    id = aId;
    openingTime = aOpeningTime;
    closingTime = aClosingTime;
  }

  public DailyScheduleResponseDTO(DailySchedule aDailySchedule) {
    this.id = aDailySchedule.getId();
    this.openingTime = aDailySchedule.getOpeningTime();
    this.closingTime = aDailySchedule.getClosingTime();
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

  public int getId()
  {
    return id;
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