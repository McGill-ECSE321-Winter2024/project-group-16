package ca.mcgill.ecse321.SportsSchedulePlus.dto.payment;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;

public class RegistrationResponseDTO
{

  private int confirmationNumber;
  private CustomerResponseDTO customer;
  private ScheduledCourseDTO scheduledCourse;

  public RegistrationResponseDTO(int aConfirmationNumber, CustomerResponseDTO aCustomer, ScheduledCourseDTO aScheduledCourse)
  {
    confirmationNumber = aConfirmationNumber;
    customer = aCustomer;
    scheduledCourse = aScheduledCourse;
  }

  public RegistrationResponseDTO(Registration aPayment) {
    confirmationNumber = aPayment.getConfirmationNumber();
    customer = new CustomerResponseDTO(aPayment.getKey().getCustomer());
    scheduledCourse = new ScheduledCourseDTO(aPayment.getKey().getScheduledCourse());
  }


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