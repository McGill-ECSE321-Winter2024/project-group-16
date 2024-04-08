package ca.mcgill.ecse321.SportsSchedulePlus;


import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonListResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRoleRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;
import ca.mcgill.ecse321.utils.Helper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTests {




  @Autowired
  private PersonRoleRepository personRoleRepository;

  @Autowired
  private PersonRepository personRepository;



  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private InstructorRepository instructorRepository;

  @Autowired
  private OwnerRepository ownerRepository;


  @Autowired
  private UserService userService;

  @Autowired
  private TestRestTemplate restTemplate;

  @AfterEach
  public void clearDatabase() {
    personRepository.deleteAll();
    personRoleRepository.deleteAll();
    customerRepository.deleteAll();
    instructorRepository.deleteAll();
  }

  @BeforeEach
  public void setup(){
    if (Helper.toList(ownerRepository.findAll()).isEmpty()) {
        userService.createOwner();
    } 
  }


  private PersonDTO postCustomer(String name, String email, String password) {
    CustomerRequestDTO role = new CustomerRequestDTO();
    PersonDTO personDto = new PersonDTO(name, email, password, role);
    // Send a POST request to the /customers endpoint
    ResponseEntity < PersonDTO > responseEntity = restTemplate.postForEntity("/customers", personDto, PersonDTO.class);
    PersonDTO createdCustomer = responseEntity.getBody();
    return createdCustomer;
  }


  @Test
  public void testGetAllCustomers() {
      postCustomer("Test customer", "testemail@gmail.com", "tstpwdQWE123!!");
      ResponseEntity<PersonListResponseDTO> responseEntity = restTemplate.getForEntity("/customers", PersonListResponseDTO.class);
      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      assertNotNull(responseEntity.getBody());
  }

  @Test
  public void testGetCustomer() {
      PersonDTO customer = postCustomer("Test customer", "testemail@gmail.com", "tstpwdQWE123!!");
      ResponseEntity<PersonDTO> responseEntity = restTemplate.getForEntity("/customers/" + customer.getId(), PersonDTO.class);
      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      assertNotNull(responseEntity.getBody());
  }

  @Test
  public void testDeleteCustomer() {
      PersonDTO customer = postCustomer("Test customer", "testemail@gmail.com", "tstpwdQWE123!!");
      restTemplate.delete("/customers/" + customer.getId());
      // Verify deletion by attempting to retrieve the customer
      ResponseEntity<PersonDTO> responseEntity = restTemplate.getForEntity("/customers/" + customer.getId(), PersonDTO.class);
      assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  @Test
  public void testGetCustomerNotFound() {
      ResponseEntity<PersonDTO> responseEntity = restTemplate.getForEntity("/customers/" +123333 , PersonDTO.class);
      assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  @Test
  public void testCreateCustomer() {
      PersonDTO newCustomer = postCustomer("John Doe", "john@example.com", "validP1233!assword!");
      assertEquals("John Doe", newCustomer.getName());
      assertEquals("john@example.com", newCustomer.getEmail());
  }

  @Test
  public void testCreateCustomerInvalidPassword() {
      PersonDTO newCustomer = postCustomer("John Doe", "john@example.com", "invalid!");
      assertEquals(null, newCustomer.getName());
      assertEquals(null, newCustomer.getEmail());
      assertEquals(null, newCustomer.getPassword());
  }

  @Test
  public void testUpdateCustomer() {
      PersonDTO oldCustomer = postCustomer("John Doe", "john@example.com", "pasSword123441!!!");
      PersonDTO updatedCustomer = new PersonDTO("John","newemail@gmail.com","paswd124!!AWVC", new CustomerRequestDTO());
      restTemplate.put("/customers/" + oldCustomer.getId(), updatedCustomer);
      // Verify update by retrieving the updated customer
      ResponseEntity<PersonDTO> responseEntity = restTemplate.getForEntity("/customers/" + oldCustomer.getId(), PersonDTO.class);
      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      assertNotNull(responseEntity.getBody());
      assertEquals(updatedCustomer.getEmail(),responseEntity.getBody().getEmail());
      assertEquals(updatedCustomer.getName(),responseEntity.getBody().getName());
      assertEquals(oldCustomer.getId(),responseEntity.getBody().getId());
  }

  @Test
  public void testUpdateCustomerInvalidPassword() {
      PersonDTO oldCustomer = postCustomer("John Doe", "john@example.com", "pasSword123441!!!");
      PersonDTO updatedCustomer = new PersonDTO("John","newemail@gmail.com","invalid", new CustomerRequestDTO());
      restTemplate.put("/customers/" + oldCustomer.getId(), updatedCustomer);
      // Verify update by retrieving the updated customer
      ResponseEntity<PersonDTO> responseEntity = restTemplate.getForEntity("/customers/" + oldCustomer.getId(), PersonDTO.class);
      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      assertNotNull(responseEntity.getBody());
  }

  @Test
  public void testApplyForInstructor() {
      PersonDTO newCustomer = postCustomer("John Doe", "john@example.com", "validP1233!assword!");
      restTemplate.put("/customers/" + newCustomer.getId() + "/apply", null);

      // Verify by retrieving the customer after applying for instructor
      ResponseEntity<PersonDTO> responseEntity = restTemplate.getForEntity("/customers/" + newCustomer.getId(), PersonDTO.class);
      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      assertNotNull(responseEntity.getBody());
  }

  @Test
  public void testApproveCustomer() {
        PersonDTO newCustomer = postCustomer("John Doe", "john@example.com", "validP1233!assword!");
        CustomerRequestDTO role = new CustomerRequestDTO();
        Customer customer = customerRepository.findCustomerById(newCustomer.getId());
        customer.setHasApplied(true);
        customerRepository.save(customer);
        restTemplate.put("/customers/" + newCustomer.getEmail() + "/approve", null);
        PersonDTO updatedCustomer = new PersonDTO("John","newemail@gmail.com","paswd124!!AWVC",role );
        restTemplate.put("/customers/" + newCustomer.getId(), updatedCustomer);
        ResponseEntity<PersonDTO> instructorEntity = restTemplate.getForEntity("/instructors/" + newCustomer.getEmail(), PersonDTO.class);
        assertEquals(HttpStatus.OK, instructorEntity.getStatusCode());
        assertNotNull(instructorEntity.getBody());
  }

  @Test
  public void testRejectCustomer() {
      PersonDTO newCustomer = postCustomer("John Doe", "john@example.com", "validP1233!assword!");
      restTemplate.put("/customers/" + newCustomer.getId() + "/reject", null);

      // Verify by retrieving the customer after rejection
      ResponseEntity<PersonDTO> responseEntity = restTemplate.getForEntity("/customers/" + newCustomer.getId(), PersonDTO.class);
      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      assertNotNull(responseEntity.getBody());
  }

 
}
