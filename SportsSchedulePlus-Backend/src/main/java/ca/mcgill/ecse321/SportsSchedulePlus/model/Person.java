package ca.mcgill.ecse321.SportsSchedulePlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

// line 9 "model.ump"
// line 84 "model.ump"
@Entity
public class Person
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Person> personsByEmail = new HashMap<String, Person>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Person Attributes
  @Id
  @GeneratedValue
  private int id;
  private String name;
  @Column(unique=true)
  private String email;
  private String password;

  //Person Associations
  @ManyToOne
  private PersonRole personRole;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Person(){
    
  }
  public Person(String aName, String aEmail, String aPassword, PersonRole aPersonRole)
  {
    name = aName;
    password = aPassword;
    if (!setEmail(aEmail))
    {
      throw new RuntimeException("Cannot create due to duplicate email. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    if (!setPersonRole(aPersonRole))
    {
      throw new RuntimeException("Unable to create Person due to aPersonRoles. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    String anOldEmail = getEmail();
    if (anOldEmail != null && anOldEmail.equals(aEmail)) {
      return true;
    }
    if (hasWithEmail(aEmail)) {
      return wasSet;
    }
    email = aEmail;
    wasSet = true;
    if (anOldEmail != null) {
      personsByEmail.remove(anOldEmail);
    }
    personsByEmail.put(aEmail, this);
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getEmail()
  {
    return email;
  }
  /* Code from template attribute_GetUnique */
  public static Person getWithEmail(String aEmail)
  {
    return personsByEmail.get(aEmail);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithEmail(String aEmail)
  {
    return getWithEmail(aEmail) != null;
  }

  public String getPassword()
  {
    return password;
  }
  /* Code from template association_GetOne */
  public PersonRole getPersonRole()
  {
    return personRole;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setPersonRole(PersonRole aNewPersonRole)
  {
    boolean wasSet = false;
    if (aNewPersonRole != null)
    {
      personRole = aNewPersonRole;
      wasSet = true;
    }
    return wasSet;
  }

  

  public void delete()
  {
    personsByEmail.remove(getEmail());
    personRole = null;
  }

  @Override
  public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      
      Person person = (Person) obj;

      return id == person.id &&
             name.equals(person.name) &&
             email.equals(person.email) &&
             password.equals(person.password) &&
             personRole.equals(person.personRole);
  }
  

  @Override
  public int hashCode() {
      return Objects.hash(id, name, email, password, personRole);
  }

  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "email" + ":" + getEmail()+ "," +
            "password" + ":" + getPassword()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "personRole = "+(getPersonRole()!=null?Integer.toHexString(System.identityHashCode(getPersonRole())):"null");
  }
}