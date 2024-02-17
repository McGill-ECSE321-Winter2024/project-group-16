package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Payment;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

@SpringBootTest
public class CustomerRepositoryTests {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private PaymentRepository payRepo;

    @AfterEach
    @BeforeEach
    public void clearDatabase() {
        payRepo.deleteAll();
        customerRepository.deleteAll();
        
        scheduledCourseRepository.deleteAll();
        courseTypeRepository.deleteAll();
    }

    // Test for finding customer by id
    @Test
    public void findCustomerById() {
        // Create and save Customer
        Customer customer = new Customer();
        customerRepository.save(customer);

        // Load Customer from database
        Customer  loadedCustomer =  customerRepository.findCustomerById(customer.getId());

        // Asserts
        assertNotNull(loadedCustomer);
  
        assertEquals(customer,loadedCustomer);
    }

     // Helper method to create a ScheduledCourse with dummy data
    private ScheduledCourse createScheduledCourse(int i) {
        CourseType courseType = new CourseType("Sample Description", true, 99.99f);
        courseTypeRepository.save(courseType);

        return new ScheduledCourse(
            i,
            Date.valueOf("2024-01-01"),
            Time.valueOf("12:00:00"),
            Time.valueOf("13:00:00"),
            "Test Location",
            courseType
        );
    }

      // Helper method to create a payment with dummy data
    private Payment createPayment() {
        //Customer customer = new Customer();
        //customerRepository.save(customer);
       
        ScheduledCourse scheduledCourse = createScheduledCourse(1);
        scheduledCourseRepository.save(scheduledCourse);

       // Payment.Key paymentKey = new Payment.Key(customer, scheduledCourse);
        Payment newPayment = new Payment();
        
        newPayment.setConfirmationNumber(12345);

        return newPayment;
    }

    @Test
    public void saveCustomerWithScheduledCourses() {
        // Create and save Customer
        Customer customer = new Customer();
       
        System.out.println("DEBUG - Saved Customer: " + customer);
    
        // Create ScheduledCourse and save it
        ScheduledCourse scheduledCourse1 = createScheduledCourse(1);
        // Save the scheduled courses
        scheduledCourseRepository.save(scheduledCourse1);
        ScheduledCourse scheduledCourse2 = createScheduledCourse(2);
        // Save the scheduled courses
        scheduledCourseRepository.save(scheduledCourse2);
       
       
        // Associate ScheduledCourses with the Customer
        customer.addCoursesRegistered(scheduledCourse1);
        customer.addCoursesRegistered(scheduledCourse2);
        
        // Save the customer
        customerRepository.save(customer);
    
      
        // Load Customer from the database
        Customer loadedCustomer = customerRepository.findCustomerById(customer.getId());
    
        // Debug statements
        System.out.println("DEBUG - Loaded Customer: " + loadedCustomer);
        System.out.println("DEBUG - Loaded Courses Registered: " + loadedCustomer.getCoursesRegistered());
    
        // Asserts
        assertEquals(customer, loadedCustomer);
    }

    @Test
    public void saveCustomerWithPayments() {
        // Create and save Customer
        Customer customer = new Customer();
        customerRepository.save(customer);
    
        System.out.println("DEBUG - Saved Customer: " + customer);


          // Create ScheduledCourse and save it
          ScheduledCourse scheduledCourse1 = createScheduledCourse(1);
          // Save the scheduled courses
          scheduledCourseRepository.save(scheduledCourse1);
          ScheduledCourse scheduledCourse2 = createScheduledCourse(2);
          // Save the scheduled courses
          scheduledCourseRepository.save(scheduledCourse2);
         
         
          // Associate ScheduledCourses with the Customer
          customer.addCoursesRegistered(scheduledCourse1);
          customer.addCoursesRegistered(scheduledCourse2);

    
        // Create Payments
        Payment payment1 = createPayment();
        Payment.Key paymentKey1 = new Payment.Key(customer, scheduledCourse1); // Assuming null for ScheduledCourse
        payment1.setKey(paymentKey1);
        payRepo.save(payment1);
    
        Payment payment2 = createPayment();
        Payment.Key paymentKey2 = new Payment.Key(customer,scheduledCourse2); // Assuming null for ScheduledCourse
        payment2.setKey(paymentKey2);
        payRepo.save(payment2);
    
        // Associate Payments with the Customer
        customer.addCustomerPayment(payment1);
        customer.addCustomerPayment(payment2);
    
        // Save the customer
        customerRepository.save(customer);
    
        // Load Customer from the database
        Customer loadedCustomer = customerRepository.findCustomerById(customer.getId());
    
        // Debug statements
        System.out.println("DEBUG - Loaded Customer: " + loadedCustomer);
    
        // Asserts
        assertEquals(customer, loadedCustomer);
    }
    

    
    
    
}