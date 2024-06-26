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
public class DailySchedule implements Comparable<DailySchedule>  {

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
  public DailySchedule() {

  }
  public DailySchedule(Time aOpeningTime, Time aClosingTime) {
    openingTime = aOpeningTime;
    closingTime = aClosingTime;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setOpeningTime(Time aOpeningTime) {
    boolean wasSet = false;
    openingTime = aOpeningTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setClosingTime(Time aClosingTime) {
    boolean wasSet = false;
    closingTime = aClosingTime;
    wasSet = true;
    return wasSet;
  }

  public int getId(){
    return id;
  }

  public Time getOpeningTime() {
    return openingTime;
  }

  public Time getClosingTime() {
    return closingTime;
  }

  public void delete() {}

  public String toString() {
    return super.toString() + "[" + "]" + System.getProperties().getProperty("line.separator") +
      "  " + "openingTime" + "=" + (getOpeningTime() != null ? !getOpeningTime().equals(this) ? getOpeningTime().toString().replaceAll("  ", "    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
      "  " + "closingTime" + "=" + (getClosingTime() != null ? !getClosingTime().equals(this) ? getClosingTime().toString().replaceAll("  ", "    ") : "this" : "null");
  }

  /**
   * Overrides the equals method to compare two DailySchedule objects for equality.
   *
   * @param object The object to compare with.
   * @return True if the objects are equal, false otherwise.
   */
  @Override
  public boolean equals(Object object) {
    // Check if comparing with the same object
    if (this == object) return true;

    // Check if the compared object is null or belongs to a different class
    if (object == null || getClass() != object.getClass()) return false;

    // Cast the compared object to DailySchedule for detailed attribute comparison
    DailySchedule other = (DailySchedule) object;

    // Check equality for the id attribute
    boolean idsEqual = id == other.id;

    // Check equality for openingTime using compareTo or handle nulls
    boolean openingTimeEqual = openingTime == null ? other.openingTime == null : openingTime.compareTo(other.openingTime) == 0;

    // Check equality for closingTime using compareTo or handle nulls
    boolean closingTimeEqual = closingTime == null ? other.closingTime == null : closingTime.compareTo(other.closingTime) == 0;

    // Return true only if all checks pass
    return idsEqual && openingTimeEqual && closingTimeEqual;
  }

  /**
   * Overrides the hashCode method to generate a hash code based on id, openingTime, and closingTime.
   *
   * @return The hash code for the object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, openingTime, closingTime);
  }
  /**
   * Override the compareTo method
   * @param schedule
   * @return comparison int
   */
  @Override
  public int compareTo(DailySchedule schedule) {
      return Integer.compare(this.getId(), schedule.getId());
  }
  
}