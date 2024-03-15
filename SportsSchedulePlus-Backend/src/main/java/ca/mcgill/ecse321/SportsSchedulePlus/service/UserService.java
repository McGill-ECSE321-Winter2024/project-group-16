package ca.mcgill.ecse321.SportsSchedulePlus.service;

import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.*;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.CourseTypeService;
import ca.mcgill.ecse321.utils.Helper;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    InstructorRepository instructorRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    PersonRoleRepository personRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    CourseTypeService courseTypeService;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    private DailyScheduleRepository dailyScheduleRepository;



    @Transactional
    public Person createCustomer(String name, String email, String password) {
        Helper.validateUser(personRepository, name, email, password,true);
        PersonRole personRole = new Customer();
        personRoleRepository.save(personRole);
        Person person = new Person(name, email, passwordEncoder.encode(password), personRole);
        personRepository.save(person);
        return person;
    }

    @Transactional
    public Person updateCustomer(int id, String name, String email, String password) {
        Optional <Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            if (person.getPersonRole() instanceof Customer) {
                boolean newEmail = !person.getEmail().equals(email);
                Helper.validateUser(personRepository, name, email, password,newEmail);
                person.setName(name);
                person.setEmail(email);
                person.setPassword(passwordEncoder.encode(password));
                personRepository.save(person);
                return person;
            } else {
                throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Customer with ID " + id + " does not exist.");
            }
        } else {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID " + id + " does not exist.");
        }
    }

    @Transactional
    public Customer getCustomer(int id) {
        Customer customer = customerRepository.findCustomerById(id);
        if (customer == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "No customer with ID" + id + " found.");
        }
        return customer;
    }

    @Transactional
    public int deleteCustomer(int id) {
        Customer customer = customerRepository.findCustomerById(id);
        if (customer == null) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID" + id + " is not a Customer.");
        } else {
            Optional <Person> optionalPerson = personRepository.findById(id);
            if (optionalPerson.isPresent()) {
                Person person = optionalPerson.get();
                if (person.getPersonRole() instanceof Customer) {
                    int personId = person.getId();
                    personRepository.delete(person);
                    personRoleRepository.delete(person.getPersonRole());
                    customerRepository.delete(customer);
                    person.delete();
                    return personId;
                } else {
                    throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID" + id + " is not a Customer.");
                }
            } else {
                throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Customer with ID " + id + " does not exist.");
            }
        }
    }

    @Transactional
    public List <Customer> getAllCustomers() {
        return Helper.toList(customerRepository.findAll());
    }

    @Transactional
    public void applyForInstructor(int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID "+ customerId + " does not exist.");
        }
        Customer c = customer.get();
        c.setHasApplied(true);
        customerRepository.save(c);
    }

    @Transactional
    public Instructor approveCustomer(int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID "+ customerId + " does not exist.");
        }
        Person p = personRepository.findPersonByPersonRole(customer.get());
        Person newP = createInstructor(p.getEmail(), "");
        return (Instructor) newP.getPersonRole();
    }

    @Transactional
    public void rejectCustomer(int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID "+ customerId + " does not exist.");
        }
        Customer c = customer.get();
        c.setHasApplied(false);
        customerRepository.save(c);
    }

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
        owner.setDailySchedule(createDailySchedule());
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

    @Transactional
    public List<Person> getAllPersons() {
        return Helper.toList(personRepository.findAll());
    }

    @Transactional
    public Person getPersonById(int id) {
        Optional<Person> p = personRepository.findById(id);
        if (!p.isPresent()) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no person with ID " + id + ".");
        }
        return p.get();
    }



    @Transactional
    public void deletePersonById(int id) {
        System.out.println("Deleting person with ID: " + id);

        Optional<Person> existingPerson = personRepository.findById(id);

        if (!existingPerson.isPresent()) {
            System.out.println("Person with ID " + id + " not found. Unable to delete.");
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no person with ID " + id + " to delete.");
        }

        personRepository.deleteById(id);
        System.out.println("Person with ID " + id + " successfully deleted.");
    }

    @Transactional
    public Person findPersonByEmail(String email) {
        return personRepository.findPersonByEmail(email);
    }

    @Transactional
    public List<DailySchedule> createDailySchedule() {
        List<DailySchedule> dsList = new ArrayList<DailySchedule>();
        for (int i = 0; i < 7; i++) {
            DailySchedule ds = new DailySchedule();
            ds.setOpeningTime(Time.valueOf("08:00:00"));
            ds.setClosingTime(Time.valueOf("22:00:00"));
            dailyScheduleRepository.save(ds);
            dsList.add(ds);
        }
        return dsList;
    }

    /*
     * get all the daily schedules
     */
    @Transactional
    public List<DailySchedule> getAllDailySchedules() {
        return Helper.toList(dailyScheduleRepository.findAll());
    }

    /*
     * get daily schedule by id
     */
    @Transactional
    public DailySchedule getDailyScheduleById(int id) {
        return dailyScheduleRepository.findById(id).get();
    }

    /*
     * update the opening hours for a day by its id
     */
    @Transactional
    public DailySchedule updateDailyScheduleByID(int id, Time openingTime, Time closingTime) {
        Optional<DailySchedule> aOptionalDailySchedule = dailyScheduleRepository.findById(id);
        if (!aOptionalDailySchedule.isPresent()) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no schedule with ID " + id + ".");
        }
        if (openingTime == null) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Opening time must be provided.");
        }
        if (closingTime == null) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Closing time must be provided.");
        }
        if (openingTime.after(closingTime)) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Opening time must be before closing time.");
        }
        DailySchedule ds = dailyScheduleRepository.findById(id).get();
        ds.setOpeningTime(openingTime);
        ds.setClosingTime(closingTime);
        dailyScheduleRepository.save(ds);
        return ds;
    }


}
