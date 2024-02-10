/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/



// line 2 "model.ump"
// line 87 "model.ump"

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public abstract class PersonRole
{

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

  public PersonRole(int aId)
  {
    id = aId;
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

  public int getId()
  {
    return id;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]";
  }
}