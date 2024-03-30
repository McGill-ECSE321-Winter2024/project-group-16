package ca.mcgill.ecse321.SportsSchedulePlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import java.util.Objects;

// line 2 "model.ump"
// line 87 "model.ump"

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PersonRole {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PersonRole Attributes
  @Id
  @GeneratedValue
  private int id;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public PersonRole() {

  }
  public PersonRole(int aId) {
    id = aId;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(int aId) {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public int getId() {
    return id;
  }

  public void delete() {}

  public String toString() {
    return super.toString() + "[" +
      "id" + ":" + getId() + "]";
  }

  /**
   * Compares this PersonRole object with the specified object for equality.
   *
   * @param object The object to compare with this PersonRole.
   * @return true if the given object is equal to this PersonRole; false otherwise.
   */
  @Override
  public boolean equals(Object object) {
    // Check if the compared object is the same instance
    if (this == object) return true;

    // Check if the compared object is of the same class
    if (object == null || getClass() != object.getClass()) return false;

    // Cast the compared object to PersonRole for detailed attribute comparison
    PersonRole other = (PersonRole) object;

    // Compare the unique identifier for equality
    return id == other.id;
  }

  /**
   * Generates a hash code for this PersonRole object based on its attributes.
   *
   * @return A hash code value for this PersonRole.
   */
  @Override
  public int hashCode() {
    // Using Objects.hash to generate hash code based on the unique identifier
    return Objects.hash(id);
  }

}