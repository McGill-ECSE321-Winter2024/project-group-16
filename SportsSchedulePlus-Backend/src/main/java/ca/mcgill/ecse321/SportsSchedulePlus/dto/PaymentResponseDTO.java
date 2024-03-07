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
  private CustomerDto customer;
  private ScheduledCourseDto scheduledCourse;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentResponseDTO(int aConfirmationNumber, CustomerDto aCustomer, ScheduledCourseDto aScheduledCourse)
  {
    confirmationNumber = aConfirmationNumber;
    customer = aCustomer;
    scheduledCourse = aScheduledCourse;
  }

  public PaymentResponseDTO(Payment aPayment) {
    confirmationNumber = aPayment.getConfirmationNumber();
    customer = new CustomerDto(aPayment.getKey().getCustomer());
    scheduledCourse = new ScheduledCourseDto(aPayment.getKey().getScheduledCourse());
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

  public boolean setCustomer(CustomerDto aCustomer)
  {
    boolean wasSet = false;
    customer = aCustomer;
    wasSet = true;
    return wasSet;
  }

  public boolean setScheduledCourse(ScheduledCourseDto aScheduledCourse)
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

  public CustomerDto getCustomer()
  {
    return customer;
  }

  public ScheduledCourseDto getScheduledCourse()
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