package ca.mcgill.ecse321.SportsSchedulePlus.service;

import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

public class InstructorService {

    @Autowired
    InstructorRepository instructorRepository;
    @Autowired
    PersonRepository personRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PersonRoleRepository prepo;

    // Validation on instructor inputs
    @Transactional
    public Person createInstructor(String name, String email, String password, Integer id, String experience){
        Customer customer = customerRepository.findCustomerById(id);
        PersonRole personRole = new Instructor(customer.getId(), experience);
        prepo.save(personRole);
        Person person = new Person(name, email, password, personRole);
        personRepository.save(person);
        return person;
    };
/* 
    @Transactional
    public Person updateInstructor(String name, String email, String password, String experience, Integer id){
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
                throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Person with ID " + id + " is not a Instructor.");
            }
        } else {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Instructor with ID "+ id + " does not exist.");
        }

    }

    @Transactional
    public Instructor deleteInstructor(Integer id){
        Optional<Instructor> optionalInstructor = instructorRepository.findById(id);
        if (optionalInstructor.isPresent()){
            Instructor instructor = optionalInstructor.get();
            instructorRepository.delete(instructor);
            return instructor;
        }
        else{
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Instructor with ID " + id + " does not exist.");
        }
    }
    @Transactional
    public Instructor getInstructor(Integer id){
        Instructor instructor = getInstructor(id);
        return instructor;
    }

    public List<Instructor> getAllInstructors(){
        return toList(instructorRepository.findAll());

    }

    // Custom query methods
    public List<Instructor> getInstructorsBySupervisedCourse(ScheduledCourse scheduledCourse){
        return toList(instructorRepository.findInstructorBySupervisedCourses(scheduledCourse));
    }

    public Instructor getInstructorBySuggestedCourseTypes(CourseType courseType){
        return instructorRepository.findInstructorByInstructorSuggestedCourseTypes(courseType);

    }

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
    }*/

}
