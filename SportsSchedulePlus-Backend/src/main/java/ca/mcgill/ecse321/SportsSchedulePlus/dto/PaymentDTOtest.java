// package ca.mcgill.ecse321.SportsSchedulePlus.dto;
// /*PLEASE DO NOT EDIT THIS CODE*/
// /*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

// // line 2 "model.ump"
// // line 10 "model.ump"
// public class PaymentDTO
// {

//   //------------------------
//   // MEMBER VARIABLES
//   //------------------------

//   //PaymentDTO Attributes
//   private int confirmationNumber;
//   private String customerID;
//   private String scheduledCourseID;

//   //------------------------
//   // CONSTRUCTOR
//   //------------------------

//   public PaymentDTO(int aConfirmationNumber, String aCustomerID, String aScheduledCourseID)
//   {
//     confirmationNumber = aConfirmationNumber;
//     customerID = aCustomerID;
//     scheduledCourseID = aScheduledCourseID;
//   }

//   //------------------------
//   // INTERFACE
//   //------------------------

//   public boolean setConfirmationNumber(int aConfirmationNumber)
//   {
//     boolean wasSet = false;
//     confirmationNumber = aConfirmationNumber;
//     wasSet = true;
//     return wasSet;
//   }

//   public boolean setCustomerID(String aCustomerID)
//   {
//     boolean wasSet = false;
//     customerID = aCustomerID;
//     wasSet = true;
//     return wasSet;
//   }

//   public boolean setScheduledCourseID(String aScheduledCourseID)
//   {
//     boolean wasSet = false;
//     scheduledCourseID = aScheduledCourseID;
//     wasSet = true;
//     return wasSet;
//   }

//   public int getConfirmationNumber()
//   {
//     return confirmationNumber;
//   }

//   public String getCustomerID()
//   {
//     return customerID;
//   }

//   public String getScheduledCourseID()
//   {
//     return scheduledCourseID;
//   }

//   public void delete()
//   {}


//   public String toString()
//   {
//     return super.toString() + "["+
//             "confirmationNumber" + ":" + getConfirmationNumber()+ "," +
//             "customerID" + ":" + getCustomerID()+ "," +
//             "scheduledCourseID" + ":" + getScheduledCourseID()+ "]";
//   }
// }