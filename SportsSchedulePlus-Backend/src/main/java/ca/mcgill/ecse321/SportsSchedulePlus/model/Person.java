package ca.mcgill.ecse321.SportsSchedulePlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

// line 9 "model.ump"
// line 84 "model.ump"
@Entity
public class Person {

  //------------------------
  // STATIC VARIABLES
  //------------------------

 
  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Person Attributes
  @Id
  private int id;
  private String name;
  @Column(unique = true)
  private String email;
  private String password;

  //Person Associations
  @ManyToOne
  private PersonRole personRole;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Person() {

  }
  public Person(String aName, String aEmail, String aPassword, PersonRole aPersonRole) {
    this.id = aPersonRole.getId();
    name = aName;
    password = aPassword;
    if (!setEmail(aEmail)) {
      throw new RuntimeException("Cannot create due to duplicate email. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    if (!setPersonRole(aPersonRole)) {
      throw new RuntimeException("Unable to create Person due to aPersonRoles. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName) {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail) {
    boolean wasSet = false;
    String anOldEmail = getEmail();
    if (anOldEmail != null && anOldEmail.equals(aEmail)) {
      return true;
    }
    
    email = aEmail;
    wasSet = true;
    
    return wasSet;
  }

  public boolean setPassword(String aPassword) {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }
  

  public int getId(){
    return id;
  }
  
  public String getPassword() {
    return password;
  }
  /* Code from template association_GetOne */
  public PersonRole getPersonRole() {
    return personRole;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setPersonRole(PersonRole aNewPersonRole) {
    boolean wasSet = false;
    if (aNewPersonRole != null) {
      personRole = aNewPersonRole;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete() {
    personRole = null;
  }

  public String toString() {
    return super.toString() + "[" +
      "name" + ":" + getName() + "," +
      "email" + ":" + getEmail() + "," +
      "password" + ":" + getPassword() + "]" + System.getProperties().getProperty("line.separator") +
      "  " + "personRole = " + (getPersonRole() != null ? Integer.toHexString(System.identityHashCode(getPersonRole())) : "null");
  }

  /**
   * Compares this Person object with the specified object for equality.
   *
   * @param object The object to compare with this Person.
   * @return true if the given object is equal to this Person; false otherwise.
   */
  @Override
  public boolean equals(Object object) {
    // Check if the compared object is the same instance
    if (this == object) return true;

    // Check if the compared object is of the same class
    if (object == null || getClass() != object.getClass()) return false;

    // Cast the compared object to Person for detailed attribute comparison
    Person person = (Person) object;

    // Compare individual attributes for equality
    return id == person.id &&
      name.equals(person.name) &&
      email.equals(person.email) &&
      password.equals(person.password) &&
      personRole.equals(person.personRole);
  }

  /**
   * Generates a hash code for this Person object based on its attributes.
   *
   * @return A hash code value for this Person.
   */
  @Override
  public int hashCode() {
    // Using Objects.hash to generate hash code based on selected attributes
    return Objects.hash(id, name, email, password, personRole);
  }

}