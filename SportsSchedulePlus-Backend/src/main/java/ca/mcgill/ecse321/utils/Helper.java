package ca.mcgill.ecse321.utils;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
/**
 * Helper class that contains methods for list comparison useful for the override of equals in the models
 * and methods to create models for testing purposes.
 */
public class Helper {

  /**
   /**
   * Compare two lists element-wise
   * @param firstList
   * @param secondList
   * @return boolean indicating equality
   */
  public static boolean compareListsElementWise(List <?> firstList, List <?> secondList) {
    if (firstList.size() != secondList.size()) {
      return false;
    }

    Iterator <?> firstIterator = firstList.iterator();
    Iterator <?> secondIterator = secondList.iterator();

    while (firstIterator.hasNext() && secondIterator.hasNext()) {
      Object firstElement = firstIterator.next();
      Object secondElement = secondIterator.next();

      if (!Objects.equals(firstElement, secondElement)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Helper method to create a ScheduledCourse with dummy data.
   */
  public static ScheduledCourse createScheduledCourse(CourseType courseType) {

    return new ScheduledCourse(
      1,
      Date.valueOf("2024-01-01"),
      Time.valueOf("12:00:00"),
      Time.valueOf("13:00:00"),
      "Test Location",
      courseType
    );
  }

  /**
   * Helper method to create a payment with dummy data.
   */
  public static Payment createPayment(Customer customer, ScheduledCourse scheduledCourse) {
    Payment.Key paymentKey = new Payment.Key(customer, scheduledCourse);
    Payment newPayment = new Payment(paymentKey);
    newPayment.setConfirmationNumber(12345);

    return newPayment;
  }

  /**
   * Helper method to create a list from an iterable.
   */
  public static <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}