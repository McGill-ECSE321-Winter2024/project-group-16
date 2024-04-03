package ca.mcgill.ecse321.SportsSchedulePlus;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.owner.OwnerResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
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
public class OwnerIntegrationTests {


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
  private CourseTypeRepository courseTypeRepository;
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
    ownerRepository.deleteAll();
    courseTypeRepository.deleteAll();
  }

   @BeforeEach
    public void setup(){
        if (Helper.toList(ownerRepository.findAll()).isEmpty()) {
           userService.createOwner();
        }
    }



    private PersonDTO postOwner(String name, String password) {
        PersonDTO personDto = new PersonDTO(name, "owner@example.com", password,new OwnerResponseDTO());

        ResponseEntity<PersonDTO> responseEntity = restTemplate.postForEntity("/owner", personDto, PersonDTO.class);
        return responseEntity.getBody();
    }

    @Test
    public void testGetOwner() {
        postOwner("owner", "password");

        ResponseEntity<PersonDTO> responseEntity = restTemplate.getForEntity("/owner", PersonDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("owner",responseEntity.getBody().getName());
    }

    @Test
    public void testCreateOwner() {
        userService.deleteUser(userService.getOwner().getId());
        PersonDTO personDto = new PersonDTO("owner", "owner@example.com", "Passworxd1234!!",new OwnerResponseDTO());
        
        ResponseEntity<PersonDTO> responseEntity = restTemplate.postForEntity("/owner", personDto, PersonDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("owner",responseEntity.getBody().getName());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testUpdateOwner() {
        postOwner("owner", "password");
        ResponseEntity<PersonDTO> responseEntity =  restTemplate.getForEntity("/owner", PersonDTO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }


    @Test
    public void testSuggestCourseTypeInvalid() {
        postOwner("owner", "password");
        CourseTypeRequestDTO newCourseType = new CourseTypeRequestDTO();
        newCourseType.setDescription("");
        newCourseType.setApprovedByOwner(true);
        newCourseType.setPrice(-30f);

        // Suggest CourseType

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/owner/suggest-course", newCourseType, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testSuggestCourseType() {
        postOwner("John Doe", "password");
        CourseTypeRequestDTO newCourseType = new CourseTypeRequestDTO();
        newCourseType.setDescription("Yoga Basics");
        newCourseType.setApprovedByOwner(true);
        newCourseType.setPrice(20.0f);

        // Suggest CourseType

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/owner/suggest-course", newCourseType, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course type suggested successfully.", responseEntity.getBody());
    }
}
