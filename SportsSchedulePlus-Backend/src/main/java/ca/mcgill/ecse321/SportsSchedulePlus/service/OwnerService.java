package ca.mcgill.ecse321.SportsSchedulePlus.service;

import java.util.Optional;
import java.util.List;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.utils.Helper;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;

@Service
public class OwnerService {

  @Autowired
  OwnerRepository ownerRepository;

  @Autowired
  PersonRepository personRepository;

  @Autowired
  PersonRoleRepository personRoleRepository;

  @Autowired
  DailyScheduleService dailyScheduleService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Transactional
  public Owner getOwner() {
    Iterable<Owner> ownerList = ownerRepository.findAll();
    if (ownerList.iterator().hasNext()) {
      Owner owner = ownerList.iterator().next();
      return owner;
    } else {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "The owner does not yet exist within the system."); 
    }
  }

  @Transactional
  public Person createOwner() {
    Helper.validateUser(personRepository, "owner", "sports.schedule.plus@gmail.com", "admin",true);
    PersonRole personRole = new Owner();
    personRoleRepository.save(personRole);
    Owner owner = getOwner();
    owner.setDailySchedule(dailyScheduleService.createDailySchedule());
    Person person = new Person("owner", "owner@ssplus.com", passwordEncoder.encode("admin"), personRole);
    personRepository.save(person);
    return person;
  }

  @Transactional
  public Person updateOwner(String name, String password) {
    Owner owner = getOwner();
    Optional<Person> optionalPerson = personRepository.findById(owner.getId());
    if (optionalPerson.isPresent()) {
      Person person = optionalPerson.get();
      if (person.getPersonRole() instanceof Owner) {
        Helper.validateUser(personRepository, name, person.getEmail(), password, false);
        person.setName(name);
        person.setPassword(passwordEncoder.encode(password));
        personRepository.save(person);
        return person;
      } else {
        throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "The owner does not yet exist within the system.");
      }
    } else {
      throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "The owner does not yet exist within the system.");
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

  // @Transactional
  // public Owner getOwnerByDailySchedule(List<DailySchedule> dailySchedule) {
  //   return ownerRepository.findOwnerByDailySchedule(dailySchedule);
  // }

}