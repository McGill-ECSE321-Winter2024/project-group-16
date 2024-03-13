package ca.mcgill.ecse321.SportsSchedulePlus.dto;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;

// line 37 "model.ump"
// line 93 "model.ump"
public class PaymentResponseDTO
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PaymentDTO Attributes
  private int confirmationNumber;
  private CustomerResponseDTO customer;
  private ScheduledCourseDTO scheduledCourse;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentResponseDTO(int aConfirmationNumber, CustomerResponseDTO aCustomer, ScheduledCourseDTO aScheduledCourse)
  {
    confirmationNumber = aConfirmationNumber;
    customer = aCustomer;
    scheduledCourse = aScheduledCourse;
  }

  public PaymentResponseDTO(Payment aPayment) {
    confirmationNumber = aPayment.getConfirmationNumber();
    customer = new CustomerResponseDTO(aPayment.getKey().getCustomer());
    scheduledCourse = new ScheduledCourseDTO(aPayment.getKey().getScheduledCourse());
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

  public boolean setCustomer(CustomerResponseDTO aCustomer)
  {
    boolean wasSet = false;
    customer = aCustomer;
    wasSet = true;
    return wasSet;
  }

  public boolean setScheduledCourse(ScheduledCourseDTO aScheduledCourse)
  {
    boolean wasSet = false;
    scheduledCourse = aScheduledCourse;
    wasSet = true;
    return wasSet;
  }

  public int getConfirmationNumber()
  {
    return confirmationNumber;
  }

  public CustomerResponseDTO getCustomer()
  {
    return customer;
  }

  public ScheduledCourseDTO getScheduledCourse()
  {
    return scheduledCourse;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "confirmationNumber" + ":" + getConfirmationNumber()+ "]";
  }
}