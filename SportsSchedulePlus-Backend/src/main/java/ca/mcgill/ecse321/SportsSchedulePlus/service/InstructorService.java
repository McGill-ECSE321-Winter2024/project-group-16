package ca.mcgill.ecse321.SportsSchedulePlus.service;

import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.utils.Helper;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {

  @Autowired
  InstructorRepository instructorRepository;

  @Autowired
  PersonRepository personRepository;

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  PersonRoleRepository personRoleRepository;

  @Autowired
  CourseTypeService courseTypeService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Transactional
  public Person createInstructor(String email, String experience) {
    // Check if the person with the given email exists
    Person existingPerson = personRepository.findPersonByEmail(email);
    if (existingPerson != null) {

      // Retrieve existing person details
      String name = existingPerson.getName();
      String password = existingPerson.getPassword();

      // Find associated customer
      Customer customer = customerRepository.findCustomerById(existingPerson.getId());

      // Create a new instructor role
      PersonRole newPersonRole = new Instructor(customer, experience);
      PersonRole oldPersonRole = existingPerson.getPersonRole();

      personRoleRepository.delete(oldPersonRole);
      customerRepository.delete(customer);
      personRepository.delete(existingPerson);
      personRepository.deleteByEmail(email);
      existingPerson.delete();

      // Save new instructor role
      personRoleRepository.save(newPersonRole);

      // Create and save new person with the instructor role
      Person newPerson = new Person(name, email, password, newPersonRole);
      personRepository.save(newPerson);

      return newPerson;
    } else {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Customer with email " + email + " needs to exist before they can become an Instructor.");
    }
  }

  @Transactional
  public Person updateInstructor(int id, String name, String email, String password, String experience) {
    Optional <Person> optionalPerson = personRepository.findById(id);
    if (optionalPerson.isPresent()) {
      Person person = optionalPerson.get();
      if (person.getPersonRole() instanceof Instructor) {
        Instructor instructor = (Instructor) person.getPersonRole();
        boolean newEmail = !person.getEmail().equals(email);
        Helper.validateUser(personRepository, name, email, password,newEmail);
        instructor.setExperience(experience);
        person.setName(name);
        person.setEmail(email);
        person.setPassword(passwordEncoder.encode(password));
        personRepository.save(person);
        return person;
      } else {
        throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID " + id + " is not an instructor.");
      }
    } else {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID " + id + " does not exist.");
    }
  }

  @Transactional
  public int deleteInstructor(int id) {
    Optional <Instructor> optionalInstructor = instructorRepository.findById(id);
    if (optionalInstructor.isPresent()) {
      Instructor instructor = optionalInstructor.get();
      Optional <Person> optionalAssociatedPerson = personRepository.findById(instructor.getId());

      if (optionalAssociatedPerson.isPresent() && optionalAssociatedPerson.get().getPersonRole() instanceof Instructor) {
        Person associatedPerson = optionalAssociatedPerson.get();
        int personId = associatedPerson.getId();
        personRepository.delete(associatedPerson);
        personRoleRepository.delete(associatedPerson.getPersonRole());
        instructorRepository.delete(instructor);
        associatedPerson.delete();
        return personId;
      } else {
        throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID " + id + " is not an Instructor.");
      }
    } else {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Instructor with ID " + id + " does not exist.");
    }
  }

  @Transactional
  public Instructor getInstructor(String email) {
    Person person = personRepository.findPersonByEmail(email);
    Optional <Instructor> optionalInstructor = instructorRepository.findById(person.getId());
    if (optionalInstructor.isPresent()) {
      Instructor instructor = optionalInstructor.get();
      return instructor;
    } else {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Instructor with email " + email + " does not exist.");
    }
  }

  @Transactional
  public List <Instructor> getAllInstructors() {
    return Helper.toList(instructorRepository.findAll());
  }

  @Transactional
  public List <Instructor> getInstructorsBySupervisedCourse(ScheduledCourse scheduledCourse) {
    return Helper.toList(instructorRepository.findInstructorBySupervisedCourses(scheduledCourse));
  }

  @Transactional
  public Instructor getInstructorBySuggestedCourseTypes(CourseType courseType) {
    Instructor instructor = instructorRepository.findInstructorByInstructorSuggestedCourseTypes(courseType);
    if (instructor == null) {
      throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "No Instructor found for the specified CourseType.");
    }
    return instructor;
  }

  @Transactional
  public List <Instructor> getInstructorByExperience(String experience) {
    return Helper.toList(instructorRepository.findInstructorByExperience(experience));
  }

  @Transactional
  public void suggestCourseType(Instructor instructor, CourseType courseType) {
    // Add the course type to the instructor's suggested course types
    
    CourseType courseTypeCreated = courseTypeService.createCourseType(courseType.getDescription(), courseType.getApprovedByOwner(),courseType.getPrice());
    instructor.addInstructorSuggestedCourseType(courseTypeCreated);
    // Save the instructor with the updated suggested course types
    instructorRepository.save(instructor);
  }
}