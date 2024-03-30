package ca.mcgill.ecse321.SportsSchedulePlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import java.util.*;

import jakarta.persistence.Entity;

// line 25 "model.ump"
// line 100 "model.ump"
@Entity
public class Customer extends PersonRole {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  private boolean hasApplied;



  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Customer() {
    hasApplied = false;
  }
  public Customer(int aId) {
    super(aId);
    hasApplied = false;
  }


  public boolean getHasApplied() {
    return hasApplied;
  }

  public boolean setHasApplied(boolean aHasApplied) {
    boolean wasSet = false;
    hasApplied = aHasApplied;
    wasSet = true;
    return wasSet;
  }

 
  public String toString() {
    return super.toString() + "[" +
      "id" + ":" + getId() + "]";
  }

  /**
   * Overrides the equals method to compare two Customer objects for equality.
   *
   * @param object The object to compare with.
   * @return True if the objects are equal, false otherwise.
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!super.equals(object) || !(object instanceof Customer customer)) return false;

    return customer.getId() == this.getId();
  }

  /**
   * Overrides the hashCode method to generate a hash code based on
   * id, customerPayments, and coursesRegistered.
   *
   * @return The hash code for the object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(),getId());
  }

}