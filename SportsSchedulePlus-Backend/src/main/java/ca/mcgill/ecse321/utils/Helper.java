package ca.mcgill.ecse321.utils;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
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
   * HelperMethods method to create a ScheduledCourse with dummy data.
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
   * HelperMethods method to create a payment with dummy data.
   */
  public static Registration createRegistration(Customer customer, ScheduledCourse scheduledCourse) {
    Registration.Key registrationKey = new Registration.Key(customer, scheduledCourse);
    Registration newRegistration= new Registration(registrationKey);
    newRegistration.setConfirmationNumber(12345);
    return newRegistration;
  }

  /**
   * HelperMethods method to create a list from an iterable.
   */
  public static <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
  /**
   * HelperMethods method to validate a user
   * @param personRepository
   * @param name
   * @param email
   * @param password
   * @param newEmail (boolean to indicate whether the user is creating a new email or keeping the same one
   */
  public static void validateUser(PersonRepository personRepository,String name, String email, String password, boolean newEmail) {
    if(newEmail){
      if (personRepository.findPersonByEmail(email) != null) {
        throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "User with email " + email + " already exists.");
      }
    }

    if (name.isBlank()) {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Name cannot be blank.");
    }

    if (!Pattern.matches("[a-zA-Z\\s]+", name)) {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Name cannot contain special characters.");
    }

    if (!PasswordValidator.isValidPassword(password)) {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Password is not valid.");
    }

    if (!EmailValidator.validate(email)) {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Email is not valid.");
    }

  }

  /**
   * Helper method to create course type request
   * @param description
   * @param approvedByOwner
   * @param price
   * @return
   */
  public static CourseTypeRequestDTO createCourseTypeRequest(String description, boolean approvedByOwner, float price) {
    CourseTypeRequestDTO courseTypeRequest = new CourseTypeRequestDTO();
    courseTypeRequest.setDescription(description);
    courseTypeRequest.setApprovedByOwner(approvedByOwner);
    courseTypeRequest.setPrice(price);
    return courseTypeRequest;
  }
  /**
   * Helper method to create a course request
   * @param location
   * @param date
   * @param startTime
   * @param endTime
   * @param courseType
   * @return ScheduledCourseRequestDTO
   */
  private static ScheduledCourseRequestDTO createCourseRequest (String location, String date, String startTime, String endTime, CourseTypeRequestDTO courseType){
    ScheduledCourseRequestDTO scheduledCourseRequest = new ScheduledCourseRequestDTO();
    scheduledCourseRequest.setLocation(location);
    scheduledCourseRequest.setDate(date);
    scheduledCourseRequest.setStartTime(startTime);
    scheduledCourseRequest.setEndTime(endTime);
    scheduledCourseRequest.setCourseType(courseType);
    return scheduledCourseRequest;
  }
  /**
   * Helper method to create a scheduled course with REST
   * @param restTemplate
   * @param location
   * @param date
   * @param startTime
   * @param endTime
   * @return int
   */
  public static int createScheduledCourse(RestTemplate restTemplate,String location, String date, String startTime, String endTime) {
    ResponseEntity < CourseTypeRequestDTO > courseTypeResponse = restTemplate.postForEntity("/courseTypes",
    createCourseTypeRequest("Yoga", true, 20.0f), CourseTypeRequestDTO.class);

    ScheduledCourseRequestDTO courseRequest = createCourseRequest(location,date,startTime,endTime,courseTypeResponse.getBody());

    ResponseEntity < ScheduledCourseRequestDTO > scheduledCourseResponse = restTemplate.postForEntity(
      "/scheduledCourses", courseRequest, ScheduledCourseRequestDTO.class);

    return scheduledCourseResponse.getBody().getId();
  }

      /**
     * Validates the course type before saving or updating it.
     * @param description
     * @param price
     * @param newDescription
     */
    public static void validateCourseType(CourseTypeRepository courseTypeRepository, String description, float price, boolean newDescription) {
        if (description == null || description.trim().isEmpty()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course description cannot be null or empty.");
        }
        if (price <= 0) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course price must be greater than zero.");
        }
        if (newDescription) {
            if (courseTypeRepository.findCourseTypeByDescription(description) != null) { 
              throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course description must be unique");
            }
        }
        if (!description.matches(".*[a-zA-Z].*")) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Description must contain letters.");
        }
        if (description.length() > 60) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course description cannot exceed 60 characters.");
        }
    }

  public static void validatePersonRole(PersonRole personRole) {
    if (personRole == null) {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person role cannot be null.");
    }
  }
  
}