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
  public PersonRole(){
    
  }
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

  @Override
  public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
     
      PersonRole that = (PersonRole) obj;
      
      System.out.println("Debug: PERSON ROLE id - " + id + ", that.id - " + that.id);
  
      return id == that.id;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]";
  }
}