/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/



// line 7 "model.ump"
// line 74 "model.ump"
public class Person
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Person Attributes
  private int id;
  private String name;
  private String email;
  private String password;

  //Person Associations
  private PersonRole personRoles;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Person(int aId, String aName, String aEmail, String aPassword, PersonRole aPersonRoles)
  {
    id = aId;
    name = aName;
    email = aEmail;
    password = aPassword;
    if (!setPersonRoles(aPersonRoles))
    {
      throw new RuntimeException("Unable to create Person due to aPersonRoles. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
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

  public int getId()
  {
    return id;
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
  public PersonRole getPersonRoles()
  {
    return personRoles;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setPersonRoles(PersonRole aNewPersonRoles)
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
            "id" + ":" + getId()+ "," +
            "name" + ":" + getName()+ "," +
            "email" + ":" + getEmail()+ "," +
            "password" + ":" + getPassword()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "personRoles = "+(getPersonRoles()!=null?Integer.toHexString(System.identityHashCode(getPersonRoles())):"null");
  }
}