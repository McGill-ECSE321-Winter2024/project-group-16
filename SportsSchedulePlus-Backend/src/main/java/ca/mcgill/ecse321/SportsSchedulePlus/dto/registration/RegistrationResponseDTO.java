package ca.mcgill.ecse321.SportsSchedulePlus.dto.payment;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;

public class RegistrationResponseDTO
{

  private int confirmationNumber;
  private CustomerRequestDTO customer;
  private ScheduledCourseResponseDTO scheduledCourse;

  public RegistrationResponseDTO(int aConfirmationNumber, CustomerRequestDTO aCustomer, ScheduledCourseResponseDTO aScheduledCourse)
  {
    confirmationNumber = aConfirmationNumber;
    customer = aCustomer;
    scheduledCourse = aScheduledCourse;
  }

  public RegistrationResponseDTO(Registration aPayment) {
    confirmationNumber = aPayment.getConfirmationNumber();
    customer = new CustomerRequestDTO(aPayment.getKey().getCustomer());
    scheduledCourse = new ScheduledCourseResponseDTO(aPayment.getKey().getScheduledCourse());
  }


  public boolean setConfirmationNumber(int aConfirmationNumber)
  {
    boolean wasSet = false;
    confirmationNumber = aConfirmationNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setCustomer(CustomerRequestDTO aCustomer)
  {
    boolean wasSet = false;
    customer = aCustomer;
    wasSet = true;
    return wasSet;
  }

  public boolean setScheduledCourse(ScheduledCourseResponseDTO aScheduledCourse)
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

  public CustomerRequestDTO getCustomer()
  {
    return customer;
  }

  public ScheduledCourseResponseDTO getScheduledCourse()
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