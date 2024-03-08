package ca.mcgill.ecse321.SportsSchedulePlus.service;

import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.utils.EmailValidator;
import ca.mcgill.ecse321.utils.PasswordValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // Validation on instructor inputs
    @Transactional
    public Person createInstructor(String name, String email, String password, String experience, int id){
        validateUser(name, email, password);
        Customer customer = customerRepository.findCustomerById(id);
        PersonRole personRole = new Instructor(customer.getId(), experience);
        Person person = new Person(name, email, password, personRole);
        personRepository.save(person);
        return person;
    }

    @Transactional
    public Person updateInstructor(String name, String email, String password, String experience, int id){
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            if (person.getPersonRole() instanceof Instructor) {
                ((Instructor) person.getPersonRole()).setExperience(experience);
                person.setEmail(email);
                person.setPassword(password);
                person.setName(name);
                personRepository.save(person);
                return person;
            } else{
                throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID " + id + " is not a Instructor.");
            }
        } else {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Instructor with ID "+ id + " does not exist.");
        }

    }

    @Transactional
    public Instructor deleteInstructor(int id){
        Optional<Instructor> optionalInstructor = instructorRepository.findById(id);
        if (optionalInstructor.isPresent()){
            Instructor instructor = optionalInstructor.get();
            instructorRepository.delete(instructor);
            return instructor;
        }
        else{
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Instructor with ID " + id + " does not exist.");
        }
    }
    @Transactional
    public Instructor getInstructor(int id){
        Optional<Instructor> instructor = instructorRepository.findById(id);
        if (instructor.isPresent()){
            Instructor instructor1 = instructor.get();
            return instructor1;
        }else{
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Instructor with ID " + id + " does not exist.");
        }

    }

    @Transactional
    public List<Instructor> getAllInstructors(){
        return toList(instructorRepository.findAll());

    }

    @Transactional
    // Custom query methods
    public List<Instructor> getInstructorsBySupervisedCourse(ScheduledCourse scheduledCourse){
        return toList(instructorRepository.findInstructorBySupervisedCourses(scheduledCourse));
    }
    @Transactional
    public Instructor getInstructorBySuggestedCourseTypes(CourseType courseType){
        return instructorRepository.findInstructorByInstructorSuggestedCourseTypes(courseType);

    }
    @Transactional
    public List<Instructor> getInstructorByExperience(String experience){
        return toList(instructorRepository.findInstructorByExperience(experience));
    }

    // To be defined in a separate helper class
    private <T> List<T> toList(Iterable<T> iterable){
        List<T> resultList = new ArrayList<T>();
        for (T t: iterable){
            resultList.add(t);
        }
        return resultList;
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