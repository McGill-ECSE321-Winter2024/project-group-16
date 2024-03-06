package ca.mcgill.ecse321.SportsSchedulePlus.dto;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;

import java.util.*;

// line 26 "model.ump"
// line 126 "model.ump"
public class CustomerDTO extends PersonRole
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CustomerDTO Associations
  private List<PaymentDTO> customerPayments;
  private List<ScheduledCourseDTO> coursesRegistered;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CustomerDTO(int aId)
  {
    super(aId);
    customerPayments = new ArrayList<PaymentDTO>();
    coursesRegistered = new ArrayList<ScheduledCourseDTO>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public PaymentDTO getCustomerPayment(int index)
  {
    PaymentDTO aCustomerPayment = customerPayments.get(index);
    return aCustomerPayment;
  }

  public List<PaymentDTO> getCustomerPayments()
  {
    List<PaymentDTO> newCustomerPayments = Collections.unmodifiableList(customerPayments);
    return newCustomerPayments;
  }

  public int numberOfCustomerPayments()
  {
    int number = customerPayments.size();
    return number;
  }

  public boolean hasCustomerPayments()
  {
    boolean has = customerPayments.size() > 0;
    return has;
  }

  public int indexOfCustomerPayment(PaymentDTO aCustomerPayment)
  {
    int index = customerPayments.indexOf(aCustomerPayment);
    return index;
  }
  /* Code from template association_GetMany */
  public ScheduledCourseDTO getCoursesRegistered(int index)
  {
    ScheduledCourseDTO aCoursesRegistered = coursesRegistered.get(index);
    return aCoursesRegistered;
  }

  public List<ScheduledCourseDTO> getCoursesRegistered()
  {
    List<ScheduledCourseDTO> newCoursesRegistered = Collections.unmodifiableList(coursesRegistered);
    return newCoursesRegistered;
  }

  public int numberOfCoursesRegistered()
  {
    int number = coursesRegistered.size();
    return number;
  }

  public boolean hasCoursesRegistered()
  {
    boolean has = coursesRegistered.size() > 0;
    return has;
  }

  public int indexOfCoursesRegistered(ScheduledCourseDTO aCoursesRegistered)
  {
    int index = coursesRegistered.indexOf(aCoursesRegistered);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomerPayments()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addCustomerPayment(PaymentDTO aCustomerPayment)
  {
    boolean wasAdded = false;
    if (customerPayments.contains(aCustomerPayment)) { return false; }
    customerPayments.add(aCustomerPayment);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCustomerPayment(PaymentDTO aCustomerPayment)
  {
    boolean wasRemoved = false;
    if (customerPayments.contains(aCustomerPayment))
    {
      customerPayments.remove(aCustomerPayment);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCustomerPaymentAt(PaymentDTO aCustomerPayment, int index)
  {  
    boolean wasAdded = false;
    if(addCustomerPayment(aCustomerPayment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomerPayments()) { index = numberOfCustomerPayments() - 1; }
      customerPayments.remove(aCustomerPayment);
      customerPayments.add(index, aCustomerPayment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCustomerPaymentAt(PaymentDTO aCustomerPayment, int index)
  {
    boolean wasAdded = false;
    if(customerPayments.contains(aCustomerPayment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomerPayments()) { index = numberOfCustomerPayments() - 1; }
      customerPayments.remove(aCustomerPayment);
      customerPayments.add(index, aCustomerPayment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCustomerPaymentAt(aCustomerPayment, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCoursesRegistered()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addCoursesRegistered(ScheduledCourseDTO aCoursesRegistered)
  {
    boolean wasAdded = false;
    if (coursesRegistered.contains(aCoursesRegistered)) { return false; }
    coursesRegistered.add(aCoursesRegistered);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCoursesRegistered(ScheduledCourseDTO aCoursesRegistered)
  {
    boolean wasRemoved = false;
    if (coursesRegistered.contains(aCoursesRegistered))
    {
      coursesRegistered.remove(aCoursesRegistered);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCoursesRegisteredAt(ScheduledCourseDTO aCoursesRegistered, int index)
  {  
    boolean wasAdded = false;
    if(addCoursesRegistered(aCoursesRegistered))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCoursesRegistered()) { index = numberOfCoursesRegistered() - 1; }
      coursesRegistered.remove(aCoursesRegistered);
      coursesRegistered.add(index, aCoursesRegistered);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCoursesRegisteredAt(ScheduledCourseDTO aCoursesRegistered, int index)
  {
    boolean wasAdded = false;
    if(coursesRegistered.contains(aCoursesRegistered))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCoursesRegistered()) { index = numberOfCoursesRegistered() - 1; }
      coursesRegistered.remove(aCoursesRegistered);
      coursesRegistered.add(index, aCoursesRegistered);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCoursesRegisteredAt(aCoursesRegistered, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    customerPayments.clear();
    coursesRegistered.clear();
    super.delete();
  }

}