/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// line 36 "model.ump"
// line 120 "model.ump"
@Entity
public class Payment
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Payment Attributes
  @Id
  private int confirmationNumber;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Payment(int aConfirmationNumber)
  {
    confirmationNumber = aConfirmationNumber;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setConfirmationNumber(int aConfirmationNumber)
  {
    boolean wasSet = false;
    confirmationNumber = aConfirmationNumber;
    wasSet = true;
    return wasSet;
  }

  public int getConfirmationNumber()
  {
    return confirmationNumber;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "confirmationNumber" + ":" + getConfirmationNumber()+ "]";
  }
}