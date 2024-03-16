package ca.mcgill.ecse321.SportsSchedulePlus.service.userservice;

import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration.Key;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.*;
import ca.mcgill.ecse321.SportsSchedulePlus.service.dailyscheduleservice.DailyScheduleService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.CourseTypeService;
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
    private RegistrationRepository registrationRepository;
    @Autowired
    private CourseTypeService courseTypeService;
    @Autowired
    private DailyScheduleService dailyScheduleService;

    @Transactional
    public Person createUser(String name, String email, String password, PersonRole role) {
        personRoleRepository.save(role);
        Person person = new Person(name, email, passwordEncoder.encode(password), role);
        personRepository.save(person);
        return person;
    }

    @Transactional
    public Person createCustomer(String name, String email, String password) {
        PersonRole personRole = new Customer();
        Helper.validateUser(personRepository, name, email, password, true);

        return createUser(name, email, password, personRole);
    }

    /*
     * this creates the owner if it does not exist
     * 
     * to check if the owner exists, we try getOwner(), which throws SportsSchedulePlusException if it doesnt exist
     * we then catch the exception and create the owner
     * 
     * if it does exist, we throw a new exception
     */
    @Transactional
    public Person createOwner() {
        try {
            Owner ownerRetrieved = getOwner();
        } catch (Exception e) {
            PersonRole personRole = new Owner();
            personRoleRepository.save(personRole);
            Owner owner = getOwner();
            owner.setDailySchedule(dailyScheduleService.createDailySchedule());
            Person person = new Person("owner", "sports.schedule.plus@gmail.com", passwordEncoder.encode("admin"), personRole);
            personRepository.save(person);
            return person;
        }
        throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Owner already exists.");
    }

    @Transactional
    public Person createInstructor(String email, String experience) {
        Person existingPerson = personRepository.findPersonByEmail(email);
        if (existingPerson != null) {

            String name = existingPerson.getName();
            String password = existingPerson.getPassword();

            Customer customer = customerRepository.findCustomerById(existingPerson.getId());

            PersonRole newPersonRole = new Instructor(customer, experience);

            Customer instructor = (Customer) newPersonRole;
            personRoleRepository.save(newPersonRole);

            List<Registration> customerPayments = registrationRepository.findRegistrationsByKeyCustomer(customer);
            for (Registration oldPayment : customerPayments) {
                Key updatedKey = new Key(instructor, oldPayment.getKey().getScheduledCourse());
                Registration newPayment = new Registration(updatedKey);
                registrationRepository.delete(oldPayment);
                registrationRepository.save(newPayment);
            }

            PersonRole oldPersonRole = existingPerson.getPersonRole();

            personRoleRepository.delete(oldPersonRole);
            customerRepository.delete(customer);
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
     */
    @Transactional
    public Person updateUser(int id, String name, String email, String password, String experience) {
        // this checks if the owner exists
        Owner owner = getOwner();

        Person person;
        // this checks if the person exists
        if (id == -1) {
            person = personRepository.findPersonByPersonRole(owner);
        } else {
            person = getPersonById(id);
        }
        
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

    private void updatePerson(Person person, String name, String email, String password, boolean newEmail) {
        Helper.validateUser(personRepository, name, email, password, newEmail);
        person.setName(name);
        person.setEmail(email);
        person.setPassword(passwordEncoder.encode(password));
        personRepository.save(person);
    }

    private void updateOwner(Person ownerPerson, String name, String password) {
        updatePerson(ownerPerson, name, ownerPerson.getEmail(), password, false);
    }


    @Transactional
    public Customer getCustomer(int id) {
        Customer customer = customerRepository.findCustomerById(id);
        if (customer == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "No customer with ID " + id + " found.");
        }
        return customer;
    }

    @Transactional
    public List<Customer> getAllCustomers() {
        return Helper.toList(customerRepository.findAll());
    }

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

    @Transactional
    public List<Instructor> getAllInstructors() {
        return Helper.toList(instructorRepository.findAll());
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
    public Person getPersonById(int id) {
        Optional<Person> p = personRepository.findById(id);
        if (!p.isPresent()) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "There is no person with ID " + id + ".");
        }
        return p.get();
    }

    @Transactional
    public List<Person> getAllPersons() {
        return Helper.toList(personRepository.findAll());
    }

    @Transactional
    public int deleteUser(int id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            if (person.getPersonRole() instanceof Customer || person.getPersonRole() instanceof Instructor) {
                return deleteInstructorOrCustomer(person, id);
            } else {
                throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID " + id + " does not have a valid role.");
            }
        } else {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Person with ID " + id + " does not exist.");

        }
    }

    private int deleteInstructorOrCustomer(Person person, int id) {
        personRepository.delete(person);
        personRoleRepository.delete(person.getPersonRole());
        if (person.getPersonRole() instanceof Customer) {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {
                customerRepository.delete(optionalCustomer.get());
            }
        } else {
            Optional<Instructor> optionalInstructor = instructorRepository.findById(id);
            if (optionalInstructor.isPresent()) {
                instructorRepository.delete(optionalInstructor.get());
            }
        }
        return id;
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
    public Customer applyForInstructor(int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        Optional<Instructor> instructor = instructorRepository.findById(customerId);
        if (instructor.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID " + customerId + " is already an instructor.");
        }
        if (!customer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID " + customerId + " does not exist.");
        }
        Customer c = customer.get();
        if (c.getHasApplied()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID " + customerId + " has already applied to be an instructor.");
        }
        c.setHasApplied(true);
        customerRepository.save(c);
        return c;
    }

    @Transactional
    public Instructor approveCustomer(int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        Optional<Instructor> instructor = instructorRepository.findById(customerId);
        if (instructor.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID " + customerId + " is already an instructor.");
        }
        if (!customer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID " + customerId + " does not exist.");
        }
        Person p = personRepository.findPersonByPersonRole(customer.get());
        Person newP = createInstructor(p.getEmail(), "");
        return (Instructor) newP.getPersonRole();
    }

    @Transactional
    public Customer rejectCustomer(int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Customer with ID " + customerId + " does not exist.");
        }
        Customer c = customer.get();
        c.setHasApplied(false);
        customerRepository.save(c);
        return c;
    }

    @Transactional
    public List<Instructor> getInstructorsBySupervisedCourse(ScheduledCourse scheduledCourse) {
        return Helper.toList(instructorRepository.findInstructorBySupervisedCourses(scheduledCourse));
    }

    @Transactional
    public Instructor getInstructorBySuggestedCourseType(int id) {
        CourseType courseType = courseTypeService.getCourseType(id);
        Instructor instructor = instructorRepository.findInstructorByInstructorSuggestedCourseTypes(courseType);
        if (instructor == null) {
            throw new SportsSchedulePlusException(HttpStatus.NOT_FOUND, "No Instructor found for the specified CourseType.");
        }
        return instructor;
    }

    @Transactional
    public List<Instructor> getInstructorByExperience(String experience) {
        return Helper.toList(instructorRepository.findInstructorByExperience(experience));
    }

    @Transactional
    public CourseType suggestCourseType(Instructor instructor, CourseType courseType) {
        CourseType courseTypeCreated = courseTypeService.createCourseType(courseType.getDescription(), courseType.getApprovedByOwner(), courseType.getPrice());
        instructor.addInstructorSuggestedCourseType(courseTypeCreated);
        instructorRepository.save(instructor);
        return courseTypeCreated;
    }

    @Transactional
    public Person findPersonByEmail(String email) {
        return personRepository.findPersonByEmail(email);
    }

    /**
     * this method returns the course types suggested by the user with the given id
     * it checks the PersonRole of the user based on the given id
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
