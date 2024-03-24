package ca.mcgill.ecse321.SportsSchedulePlus.service.userservice;

import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration.Key;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.*;
import ca.mcgill.ecse321.SportsSchedulePlus.service.dailyscheduleservice.DailyScheduleService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.CourseTypeService;
import ca.mcgill.ecse321.utils.EmailValidator;
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
public class UserService {

    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonRoleRepository personRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private CourseTypeService courseTypeService;
    @Autowired
    private DailyScheduleService dailyScheduleService;

    @Autowired
    private DailyScheduleRepository dailyScheduleRepository;

    /**
     * This method creates a user.
     * @param name
     * @param email
     * @param password
     * @param role
     * @return
     */
    @Transactional
    public Person createUser(String name, String email, String password, PersonRole role) {
        personRoleRepository.save(role);
        Person person = new Person(name, email, passwordEncoder.encode(password), role);
        personRepository.save(person);
        return person;
    }

    /**
     * This method creates a customer.
     * 
     * The information for the customer is validated through a helper method.
     * 
     * @param name
     * @param email
     * @param password
     * @return Person account for the customer
     */
    @Transactional
    public Person createCustomer(String name, String email, String password) {
        PersonRole personRole = new Customer();
        Helper.validateUser(personRepository, name, email, password, true);
        return createUser(name, email, password, personRole);
    }

    /**
     * this creates the owner if it does not exist
     * 
     * to check if the owner exists, we try getOwner(), which throws SportsSchedulePlusException if it doesnt exist
     * we then catch the exception and create the owner
     * 
     * if it does exist, we throw a new exception
     * @return the new Person account for the owner
     */
    @Transactional
    public Person createOwner() {
        if (Helper.toList(ownerRepository.findAll()).isEmpty()){
            PersonRole personRole = new Owner();
            personRoleRepository.save(personRole);
            Owner owner = new Owner();
            owner.setDailySchedule(dailyScheduleService.createDailySchedule());
            ownerRepository.save(owner);
            Person person = new Person("owner", "sports.schedule.plus@gmail.com", passwordEncoder.encode("admin"), personRole);
            personRepository.save(person);
            return person;
        }

        throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Owner already exists.");
    }

    /**
     * This method creates an instructor.
     * 
     * This creates an instructor account for an existing customer, when said customer is approved to become an instructor.
     * 
     * THIS METHOD IS ONLY CALLED IN UserService.approveCustomer().
     * @param email
     * @param experience
     * @return the new Person account
     */
    @Transactional
    public Person createInstructor(String email, String experience) {
        Person existingPerson = personRepository.findPersonByEmail(email);
        if (existingPerson != null) {

            String name = existingPerson.getName();
            String password = existingPerson.getPassword();

            Optional<Customer> customer = customerRepository.findById(existingPerson.getId());

            PersonRole newPersonRole = new Instructor(customer.get(), experience);

            Customer instructor = (Customer) newPersonRole;
            personRoleRepository.save(newPersonRole);

            List<Registration> customerPayments = registrationRepository.findRegistrationsByKeyCustomer(customer.get());
            for (Registration oldPayment : customerPayments) {
                Key updatedKey = new Key(instructor, oldPayment.getKey().getScheduledCourse());
                Registration newPayment = new Registration(updatedKey);
                registrationRepository.delete(oldPayment);
                registrationRepository.save(newPayment);
            }

            PersonRole oldPersonRole = existingPerson.getPersonRole();

            personRoleRepository.delete(oldPersonRole);
            customerRepository.delete(customer.get());
            personRepository.delete(existingPerson);
            personRepository.deleteByEmail(email);
            existingPerson.delete();

            return createUser(name, email, password, newPersonRole);

        } else {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Customer with email " + email + " needs to exist before they can become an Instructor.");
        }
    }

    /**
     * this method updates the user
     * it checks the PersonRole of the user based on the given id
     * @param id if id == -1, then update the owner, else update user with the given id
     * @return the updated user
     */
    @Transactional
    public Person updateUser(int id, String name, String email, String password, String experience) {
        // this checks if the owner exists
         getOwner();

        Person person;
        // this checks if the person exists
       
        person = getPersonById(id);
        
        boolean isOwner = person.getPersonRole() instanceof Owner;
        boolean isCustomer = person.getPersonRole() instanceof Customer;
        boolean isInstructor = person.getPersonRole() instanceof Instructor;

        boolean newEmail = false;
        // check if the email is different from what is already in the database
        if (isCustomer || isInstructor) {
            newEmail = !person.getEmail().equals(email);
        }
        
        if (isOwner) {
            updateOwner(person, name, password);
            return person;
        } else if (isInstructor) {
            Instructor instructor = (Instructor) person.getPersonRole();
            instructor.setExperience(experience);
            updatePerson(person, name, email, password, newEmail);
            return person;
        } else if (isCustomer) {
            updatePerson(person, name, email, password, newEmail);
            return person;
        }
        throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "User instance could not be modified.");
    }

    /**
     * Updates a Person
     * @param person
     * @param name
     * @param email
     * @param password
     * @param newEmail
     */
    private void updatePerson(Person person, String name, String email, String password, boolean newEmail) {
        Helper.validateUser(personRepository, name, email, password, newEmail);
        person.setName(name);
        person.setEmail(email);
        person.setPassword(passwordEncoder.encode(password));
        personRepository.save(person);
    }

    /**
     * 
     * @param ownerPerson
     * @param name
     * @param password
     */
    private void updateOwner(Person ownerPerson, String name, String password) {
        updatePerson(ownerPerson, name, ownerPerson.getEmail(), password, false);
    }

    /**
     * @param id
     * @return Customer with given id
     */
    @Transactional
    public Customer getCustomer(int id) {
        Customer customer = customerRepository.findCustomerById(id);
        if (customer == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "No customer with ID " + id + " found.");
        }
        return customer;
    }

    /**
     * @return List of all customers
     */
    @Transactional
    public List<Customer> getAllCustomers() {
        return Helper.toList(customerRepository.findAll());
    }

    /**
     * @param email
     * @return Instructor with given email
     */
    @Transactional
    public Instructor getInstructor(String email) {
        Person person = personRepository.findPersonByEmail(email);
        if (person == null) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with email " + email + " does not exist.");
        }
        Optional<Instructor> optionalInstructor = instructorRepository.findById(person.getId());
        if (optionalInstructor.isPresent()) {
            Instructor instructor = optionalInstructor.get();
            return instructor;
        } else {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Instructor with email " + email + " does not exist.");
        }
    }

    /**
     * @return List of all instructors
     */
    @Transactional
    public List<Instructor> getAllInstructors() {
        return Helper.toList(instructorRepository.findAll());
    }

    /**
     * @return The Owner of the system
     */
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

    /**
     * @param id
     * @return Person with given id
     */
    @Transactional
    public Person getPersonById(int id) {
        Optional<Person> person = personRepository.findById(id);
        if (!person.isPresent()) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no person with ID " + id + ".");
        }
        return person.get();
    }

    /**
     * @return List of all persons
     */
    @Transactional
    public List<Person> getAllPersons() {
        return Helper.toList(personRepository.findAll());
    }

    /**
     * @param id
     * @return the id of the deleted user
     */
    @Transactional
    public int deleteUser(int id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            if (person.getPersonRole() instanceof Customer || person.getPersonRole() instanceof Instructor || person.getPersonRole() instanceof Owner)  {
                return deleteUser(person, id);
            }
             else {
                throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID " + id + " does not have a valid role.");
            }
        } else {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID " + id + " does not exist.");

        }
    }

    /**
     * Helper Method to delete an instructor or customer
     * @param person
     * @param id
     * @return the id of the deleted user
     */
    private int deleteUser(Person person, int id) {
        System.out.println("delete user !!");
        personRepository.delete(person);
        personRoleRepository.delete(person.getPersonRole());
        if (person.getPersonRole() instanceof Customer) {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {
                customerRepository.delete(optionalCustomer.get());
            }
        }   if (person.getPersonRole() instanceof Instructor){
            Optional<Instructor> optionalInstructor = instructorRepository.findById(id);
            if (optionalInstructor.isPresent()) {
                instructorRepository.delete(optionalInstructor.get());
            }
        }
         if (person.getPersonRole() instanceof Owner){
            ownerRepository.delete(getOwner());
            dailyScheduleRepository.deleteAll();
        }

        person.delete();
        return id;
    }

    /**
     * @param customerId
     * @return returns the customer with the given id, changes the hasApplied attribute to true
     */
    @Transactional
    public Customer applyForInstructor(int customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        Optional<Instructor> instructor = instructorRepository.findById(customerId);
        if (instructor.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID " + customerId + " is already an instructor.");
        }
        if (!optionalCustomer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID " + customerId + " does not exist.");
        }
        Customer customer = optionalCustomer.get();
        if (customer.getHasApplied()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID " + customerId + " has already applied to be an instructor.");
        }
        customer.setHasApplied(true);
        customerRepository.save(customer);
        return customer;
    }

    /**
     * @param customerId
     * @return returns a new Instructor account for a customer if he has applied.
     */
    @Transactional
    public Instructor approveCustomer(int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        Optional<Instructor> instructor = instructorRepository.findById(customerId);

        if (!customer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID " + customerId + " does not exist.");
        }
        if (!customer.get().getHasApplied()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID " + customerId + " has not applied to be an instructor.");
        }
        if (instructor.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID " + customerId + " is already an instructor.");
        }

        Person person = personRepository.findPersonByPersonRole(customer.get());
        Person newPerson = createInstructor(person.getEmail(), "");
        return (Instructor) newPerson.getPersonRole();
    }

    /**
     * @param customerId
     * @return returns the customer with the given id, changes the hasApplied attribute to false
     */
    @Transactional
    public Customer rejectCustomer(int customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID " + customerId + " does not exist.");
        }
        Customer customer = optionalCustomer.get();
        customer.setHasApplied(false);
        customerRepository.save(customer);
        return customer;
    }

    /**
     * @param scheduledCourse
     * @return returns the instructor that gives a Scheduled Course
     */
    @Transactional
    public List<Instructor> getInstructorsBySupervisedCourse(ScheduledCourse scheduledCourse) {
        return Helper.toList(instructorRepository.findInstructorBySupervisedCourses(scheduledCourse));
    }

    /**
     * @param id
     * @return returns the instructor that suggested a CourseType with the given id
     */
    @Transactional
    public Instructor getInstructorBySuggestedCourseType(int id) {
        CourseType courseType = courseTypeService.getCourseType(id);
        Instructor instructor = instructorRepository.findInstructorByInstructorSuggestedCourseTypes(courseType);
        if (instructor == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "No Instructor found for the specified CourseType.");
        }
        return instructor;
    }

    /**
     * @param experience
     * @return returns the instructor with the given experience
     */
    @Transactional
    public List<Instructor> getInstructorByExperience(String experience) {
        return Helper.toList(instructorRepository.findInstructorByExperience(experience));
    }

    /**
     * @param instructor
     * @param courseType
     * @return returns a new CourseType with the given information, suggested by the given instructor
     */
    @Transactional
    public CourseType suggestCourseType(PersonRole personRole, CourseType courseType) {
        Helper.validateCourseType(courseTypeRepository,courseType.getDescription(), courseType.getPrice(), true);
        Helper.validatePersonRole(personRole);
        CourseType courseTypeCreated = courseTypeService.createCourseType(courseType.getDescription(), courseType.getApprovedByOwner(), courseType.getPrice());
        if(personRole instanceof Instructor){
            Person person = getPersonById(personRole.getId());
            Instructor instructor = getInstructor(person.getEmail());
            instructor.addInstructorSuggestedCourseType(courseTypeCreated);
            instructorRepository.save(instructor);
        }

        if (personRole instanceof Owner){
            Owner owner = getOwner();
            owner.addOwnerSuggestedCourse(courseTypeCreated);
            ownerRepository.save(owner);
        }
        return courseTypeCreated;
    }

    /**
     * @param email
     * @return returns the person with the given email
     */
    @Transactional
    public Person findPersonByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Email cannot be null or blank.");
        }
        if (!EmailValidator.validate(email)) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Email is not valid.");
        }
        Person person = personRepository.findPersonByEmail(email);
        if (person == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "No person with email " + email + " found.");
        }
        return person;
    }

    /**
     * @param personId
     * @return returns the CourseTypes suggested by the person with the given id
     */
    @Transactional
    public List<CourseType> getCourseTypesSuggestedByPersonId(int personId) {
        // this checks if the owner exists
        Owner owner = getOwner();

        Optional<Person> optionalPerson = personRepository.findById(personId);
        if (!optionalPerson.isPresent()) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "No person with ID " + personId + " found.");
        }

        Person person = optionalPerson.get();

        boolean isOwner = person.getPersonRole() instanceof Owner;
        boolean isInstructor = person.getPersonRole() instanceof Instructor;

        if (isInstructor) {
            // case where the person is an instructor
            return getInstructor(person.getEmail()).getInstructorSuggestedCourseTypes();
        } else if (isOwner) {
            // case where the person is an owner
            List<CourseType> courseTypes = owner.getOwnerSuggestedCourses();
            if (courseTypes.isEmpty()) {
                throw new SportsScheduleException(HttpStatus.NOT_FOUND, "No course types found for owner.");
            }
            return courseTypes;
        } else {
            // case where the person is a customer
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customers cannot have suggested courses.");
        } 
    }

}