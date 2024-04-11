package ca.mcgill.ecse321.SportsSchedulePlus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


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
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CourseTypeIntegrationTests {

	@Autowired
	private TestRestTemplate client;
    @Autowired
    private InstructorRepository instructorRepo;
	@Autowired
	private CourseTypeRepository courseTypeRepo;
   


	@BeforeEach
	@AfterEach
	public void clearDatabase() {
        instructorRepo.deleteAll();
		courseTypeRepo.deleteAll();
	}

	// Test creating and fetching a CourseType
    @Test
    public void testCreateAndGetCourseType() {
        CourseTypeRequestDTO newCourseType = new CourseTypeRequestDTO();
        newCourseType.setName("Yoga");
        newCourseType.setDescription("Yoga description");
        newCourseType.setImage("Yoga image");
        newCourseType.setApprovedByOwner(true);
        newCourseType.setPrice(20.0f);

        // Create CourseType
        ResponseEntity<CourseTypeRequestDTO> createResponse = client.postForEntity("/courseTypes", newCourseType, CourseTypeRequestDTO.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        CourseTypeRequestDTO createdCourseType = createResponse.getBody();
        assertNotNull(createdCourseType);
        assertNotNull(createdCourseType.getId());

        // Get CourseType
        ResponseEntity<CourseTypeRequestDTO> getResponse = client.getForEntity("/courseTypes/" + createdCourseType.getId(), CourseTypeRequestDTO.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        CourseTypeRequestDTO retrievedCourseType = getResponse.getBody();
        assertNotNull(retrievedCourseType);
        assertEquals("Yoga", retrievedCourseType.getName());
        assertEquals("Yoga description", retrievedCourseType.getDescription());
    }

    // Test updating a CourseType
    @Test
    public void testUpdateCourseType() {
        // Assume this creates a CourseType and returns its ID
        int id = createCourseType("Yoga", "Yoga description", "Yoga image", true, 20.0f);

        CourseTypeRequestDTO updatedCourseType = new CourseTypeRequestDTO();
        updatedCourseType.setName("Advanced Yoga");
        updatedCourseType.setDescription("Yoga description");
        updatedCourseType.setImage("Yoga image");
        updatedCourseType.setApprovedByOwner(false);
        updatedCourseType.setPrice(25.0f);

        client.put("/courseTypes/" + id, updatedCourseType);

        ResponseEntity<CourseTypeRequestDTO> response = client.getForEntity("/courseTypes/" + id, CourseTypeRequestDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CourseTypeRequestDTO courseType = response.getBody();
        assertNotNull(courseType);
        assertEquals("Advanced Yoga", courseType.getName());
        assertEquals("Yoga description", courseType.getDescription());
        assertEquals("Yoga image", courseType.getImage());
    }

     // Test updating a CourseType with invalid data
    @Test
    public void testUpdateInvalidCourseType() {
        // Create a CourseType first
        int id = createCourseType("Yoga", "Yoga description", "Yoga image", true, 20.0f);

        // Attempt to update the CourseType with invalid inputs
        CourseTypeRequestDTO updatedCourseType = new CourseTypeRequestDTO();
        updatedCourseType.setName(""); // Invalid description
        updatedCourseType.setApprovedByOwner(false);
        updatedCourseType.setPrice(-10.0f); // Invalid price

        client.put("/courseTypes/" + id, updatedCourseType);
       
        // Ensure that the CourseType remains unchanged
        ResponseEntity<CourseTypeRequestDTO> getResponse = client.getForEntity("/courseTypes/" + id, CourseTypeRequestDTO.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        CourseTypeRequestDTO retrievedCourseType = getResponse.getBody();
        assertNotNull(retrievedCourseType);
        assertEquals("Yoga", retrievedCourseType.getName());
    }

    // Test creating a CourseType with invalid data
    @Test
    public void testCreateInvalidCourseType() {
        CourseTypeRequestDTO newCourseType = new CourseTypeRequestDTO();
        newCourseType.setName(""); // Invalid description
        newCourseType.setApprovedByOwner(true);
        newCourseType.setPrice(-10.0f); // Invalid price

        ResponseEntity<String> response = client.postForEntity("/courseTypes", newCourseType, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Test fetching a non-existing CourseType
    @Test
    public void testGetNonExistingCourseType() {
        ResponseEntity<String> response = client.getForEntity("/courseTypes" + Integer.MAX_VALUE, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // Helper method to create a CourseType for use in tests
    private int createCourseType(String name, String description, String image, boolean approvedByOwner, float price) {
        CourseTypeRequestDTO newCourseType = new CourseTypeRequestDTO();
        newCourseType.setName(name);
        newCourseType.setDescription(description);
        newCourseType.setImage(image);
        newCourseType.setApprovedByOwner(approvedByOwner);
        newCourseType.setPrice(price);
        ResponseEntity<CourseTypeRequestDTO> response = client.postForEntity("/courseTypes", newCourseType, CourseTypeRequestDTO.class);
        return response.getBody().getId();
    }
}


