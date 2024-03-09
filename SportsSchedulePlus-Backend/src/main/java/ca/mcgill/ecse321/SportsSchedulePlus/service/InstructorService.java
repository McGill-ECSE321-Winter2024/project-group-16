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

    @Transactional
    public Person createInstructor(String email, String experience){
        Person person = personRepository.findPersonByEmail(email);
        if (person != null){
            String name = person.getName();
            String password = person.getPassword();
            PersonRole personRole = person.getPersonRole();
    
            Customer customer = customerRepository.findCustomerById(person.getId());

            PersonRole personRole1 = new Instructor(customer,experience);
    
            personRepository.delete(person);
            person.delete();
            customerRepository.delete(customer);
            personRoleRepository.delete(personRole);
            
            
            personRoleRepository.save(personRole1);
            Person newPerson = new Person(name, email, password, personRole1);
            personRepository.save(newPerson);
    
            return person;
        } else {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Customer with email " + email + " needs to exist before they can become an Instructor.");
        }
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
    public Instructor getInstructor(String email){
        Person person = personRepository.findPersonByEmail(email);
        Optional<Instructor> instructor = instructorRepository.findById(person.getId());
        if (instructor.isPresent()){
            Instructor instructor1 = instructor.get();
            return instructor1;
        }else{
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Instructor with email " + email + " does not exist.");
        }

    }

    @Transactional
    public List<Instructor> getAllInstructors(){
        return Helper.toList(instructorRepository.findAll());

    }

    @Transactional
    // Custom query methods
    public List<Instructor> getInstructorsBySupervisedCourse(ScheduledCourse scheduledCourse){
        return Helper.toList(instructorRepository.findInstructorBySupervisedCourses(scheduledCourse));
    }
    @Transactional
    public Instructor getInstructorBySuggestedCourseTypes(CourseType courseType){
        return instructorRepository.findInstructorByInstructorSuggestedCourseTypes(courseType);

    }
    @Transactional
    public List<Instructor> getInstructorByExperience(String experience){
      return Helper.toList(instructorRepository.findInstructorByExperience(experience));
    }

   


}