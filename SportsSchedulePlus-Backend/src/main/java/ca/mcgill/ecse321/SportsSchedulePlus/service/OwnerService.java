package ca.mcgill.ecse321.SportsSchedulePlus.service;

import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.utils.EmailValidator;
import ca.mcgill.ecse321.utils.PasswordValidator;
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
        Optional<Owner> optionalOwner = ownerRepository.findById(id);
        if (optionalOwner.isPresent()) {
            Owner owner = optionalOwner.get();
            return owner;
        } else {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "The owner does not yet exist within the system."); // Change here
        }
    }

    @Transactional
    public Person createOwner(String name, String email, String password){
            validateUser(name, email, password);
            PersonRole personRole = new Owner();
            personRoleRepository.save(personRole);
            Person person = new Person(name, email, password, personRole);
            personRepository.save(person);
            return person;
    }

    @Transactional
    public Person updateOwner(int id, String password){
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            if (person.getPersonRole() instanceof Owner) {
                Boolean validPassword = PasswordValidator.isValidPassword(password);
                if(validPassword) {
                    person.setPassword(password);
                    personRepository.save(person);
                    return person;
                }throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Password entered not a valid password"); // Change here
            } else {
                throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID " + id + " is not the Owner."); // Change here
            }
        } else {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Owner with ID "+ id + " does not exist."); // Change here
        }
    }

    // Custom query methods
    @Transactional
    public Owner getInstructorByOwnerSuggestedCourses(CourseType courseType){
        return ownerRepository.findOwnerByOwnerSuggestedCourses(courseType);
    }

    @Transactional
    public Owner getOwnerByApprovedCourses(CourseType courseType){
        return ownerRepository.findOwnerByApprovedCourses(courseType);
    }

    @Transactional
    public Owner getOwnerByDailySchedule(DailySchedule dailySchedule){
        return ownerRepository.findOwnerByDailySchedule(dailySchedule);
    }

    private void validateUser(String name, String email, String password) {
        if (personRepository.findPersonByEmail(email) != null) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Owner with email " + email + " already exists.");
        }
    
        if (name.isBlank()) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Name cannot be blank.");
        }
    
        if (!PasswordValidator.isValidPassword(password)) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Password is not valid.");
        }
    
        if (!EmailValidator.validate(email)) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Email is not valid.");
        }
    }
    
}
