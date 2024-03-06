package ca.mcgill.ecse321.SportsSchedulePlus.dto;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.PersonRoleResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.controller.OwnerRestContoller;
import ca.mcgill.ecse321.SportsSchedulePlus.controller.InstructorRestController;
import ca.mcgill.ecse321.SportsSchedulePlus.controller.CustomerRestController;


// line 9 "model.ump"
// line 116 "model.ump"
public class PersonResponseDTO
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PersonDTO Attributes
  private String name;
  private String email;
  private String password;

  //PersonDTO Associations
  private PersonRoleResponseDTO personRoles;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PersonResponseDTO(String aName, String aEmail, String aPassword, PersonRoleResponseDTO aPersonRoles)
  {
    name = aName;
    email = aEmail;
    password = aPassword;
    if (!setPersonRoles(aPersonRoles))
    {
      throw new RuntimeException("Unable to create PersonDTO due to aPersonRoles. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public PersonResponseDTO(Person p) {
    name = p.getName();
    email = p.getEmail();
    password = p.getPassword();
    int personRoleID = p.getPersonRole().getId();
    
    if (OwnerRestContoller.getOwner().getId() == personRoleID) {
      setPersonRoles(OwnerRestContoller.getOwner());
    } 
    for (InstructorDTO i : InstructorRestController.getInstructors()) {
      if (i.getId() == personRoleID) {
        setPersonRoles(i);
      }
    }
    for (CustomerDTO c : CustomerRestController.getCustomers()) {
      if (c.getId() == personRoleID) {
        setPersonRoles(c);
      }
    }
    if (this.getPersonRoles() == null) {
      throw new RuntimeException("Unable to create PersonDTO due to missing Person Role.");
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
    email = aEmail;
    wasSet = true;
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

  public String getPassword()
  {
    return password;
  }
  /* Code from template association_GetOne */
  public PersonRoleResponseDTO getPersonRoles()
  {
    return personRoles;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setPersonRoles(PersonRoleResponseDTO aNewPersonRoles)
  {
    boolean wasSet = false;
    if (aNewPersonRoles != null)
    {
      personRoles = aNewPersonRoles;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    personRoles = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "email" + ":" + getEmail()+ "," +
            "password" + ":" + getPassword()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "personRoles = "+(getPersonRoles()!=null?Integer.toHexString(System.identityHashCode(getPersonRoles())):"null");
  }
}