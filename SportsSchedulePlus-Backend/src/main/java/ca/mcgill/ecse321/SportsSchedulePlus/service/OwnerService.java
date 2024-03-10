package ca.mcgill.ecse321.SportsSchedulePlus.service;

import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.utils.Helper;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerService {

  @Autowired
  OwnerRepository ownerRepository;

  @Autowired
  PersonRepository personRepository;

  @Autowired
  PersonRoleRepository personRoleRepository;

  @Transactional
  public Owner getOwner(int id) {
    Optional <Owner> optionalOwner = ownerRepository.findById(id);
    if (optionalOwner.isPresent()) {
      Owner owner = optionalOwner.get();
      return owner;
    } else {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "The owner does not yet exist within the system."); // Change here
    }
  }

  @Transactional
  public Person createOwner(String name, String email, String password) {
    Helper.validateUser(personRepository, name, email, password);
    PersonRole personRole = new Owner();
    personRoleRepository.save(personRole);
    Person person = new Person(name, email, password, personRole);
    personRepository.save(person);
    return person;
  }

  @Transactional
  public Person updateOwner(int id, String name, String email, String password) {
    Optional <Person> optionalPerson = personRepository.findById(id);
    if (optionalPerson.isPresent()) {
      Person person = optionalPerson.get();
      if (person.getPersonRole() instanceof Owner) {
        Helper.validateUser(personRepository, name, email, password);
        person.setName(name);
        person.setEmail(email);
        person.setPassword(password);
        personRepository.save(person);
        return person;
      } else {
        throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Owner with ID " + id + " does not exist.");
      }
    } else {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID " + id + " is not the Owner.");
    }
  }

  // Custom query methods
  @Transactional
  public Owner getInstructorByOwnerSuggestedCourses(CourseType courseType) {
    return ownerRepository.findOwnerByOwnerSuggestedCourses(courseType);
  }

  @Transactional
  public Owner getOwnerByApprovedCourses(CourseType courseType) {
    return ownerRepository.findOwnerByApprovedCourses(courseType);
  }

  @Transactional
  public Owner getOwnerByDailySchedule(DailySchedule dailySchedule) {
    return ownerRepository.findOwnerByDailySchedule(dailySchedule);
  }

}