package ca.mcgill.ecse321.SportsSchedulePlus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.customer.CustomerRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonListResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonRoleResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CustomerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserIntegrationTests {

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private CustomerRepository customerRepo;
    @Autowired
    private InstructorRepository instructorRepo;
    @Autowired
    private OwnerRepository ownerRepo;
    @Autowired
    private PersonDTO personDTO;
    @Autowired
    private PersonListResponseDTO personListResponseDTO;
    @Autowired 
    private PersonRoleResponseDTO personRoleResponseDTO;


	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		customerRepo.deleteAll();
        instructorRepo.deleteAll();
        ownerRepo.deleteAll();
	}

	// Test creating and fetching a CourseType
    @Test
    public void testCreateAndGetCustomer() {
        int id = testCreateCustomer();
    }

    private int testCreateCustomer() {
        CustomerRequestDTO newCustomerRole = new CustomerRequestDTO(1);
        ResponseEntity<PersonDTO> response = client.postForEntity("/customer", new PersonDTO("customer", "customer@example.com", "Password#1", newCustomerRole ), PersonDTO.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("customer", response.getBody().getName(), "Name matches");
        assertEquals("customer@example.com", response.getBody().getEmail(), "Email matches");
        assertEquals("encodedPassword", response.getBody().getPassword(), "Password matches");
        assertEquals(1, response.getBody().getId(), "Id matches");

        return response.getBody().getId();
    }

    //private void testGetCustomer

}