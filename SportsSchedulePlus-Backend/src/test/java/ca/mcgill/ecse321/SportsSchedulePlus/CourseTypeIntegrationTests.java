package ca;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CourseTypeIntegrationTests {

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private CourseTypeRepository courseTypeRepo;
   


	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		courseTypeRepo.deleteAll();
        

	}

	// Test creating and fetching a CourseType
    @Test
    public void testCreateAndGetCourseType() {
        CourseTypeDto newCourseType = new CourseTypeDto();
        newCourseType.setDescription("Yoga");
        newCourseType.setApprovedByOwner(true);
        newCourseType.setPrice(20.0f);

        // Create CourseType
        ResponseEntity<CourseTypeDto> createResponse = client.postForEntity("/coursetypes", newCourseType, CourseTypeDto.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        CourseTypeDto createdCourseType = createResponse.getBody();
        assertNotNull(createdCourseType);
        assertNotNull(createdCourseType.getId());

        // Get CourseType
        ResponseEntity<CourseTypeDto> getResponse = client.getForEntity("/coursetypes/" + createdCourseType.getId(), CourseTypeDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        CourseTypeDto retrievedCourseType = getResponse.getBody();
        assertNotNull(retrievedCourseType);
        assertEquals("Yoga", retrievedCourseType.getDescription());
    }

    // Test updating a CourseType
    @Test
    public void testUpdateCourseType() {
        // Assume this creates a CourseType and returns its ID
        int id = createCourseType("Yoga", true, 20.0f);

        CourseTypeDto updatedCourseType = new CourseTypeDto();
        updatedCourseType.setDescription("Advanced Yoga");
        updatedCourseType.setApprovedByOwner(false);
        updatedCourseType.setPrice(25.0f);

        client.put("/coursetypes/" + id, updatedCourseType);

        ResponseEntity<CourseTypeDto> response = client.getForEntity("/coursetypes/" + id, CourseTypeDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CourseTypeDto courseType = response.getBody();
        assertNotNull(courseType);
        assertEquals("Advanced Yoga", courseType.getDescription());
    }

    // Test creating a CourseType with invalid data
    @Test
    public void testCreateInvalidCourseType() {
        CourseTypeDto newCourseType = new CourseTypeDto();
        newCourseType.setDescription(""); // Invalid description
        newCourseType.setApprovedByOwner(true);
        newCourseType.setPrice(-10.0f); // Invalid price

        ResponseEntity<String> response = client.postForEntity("/coursetypes", newCourseType, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Test fetching a non-existing CourseType
    @Test
    public void testGetNonExistingCourseType() {
        ResponseEntity<String> response = client.getForEntity("/coursetypes/" + Integer.MAX_VALUE, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // Helper method to create a CourseType for use in tests
    private int createCourseType(String description, boolean approvedByOwner, float price) {
        CourseTypeDto newCourseType = new CourseTypeDto();
        newCourseType.setDescription(description);
        newCourseType.setApprovedByOwner(approvedByOwner);
        newCourseType.setPrice(price);
        ResponseEntity<CourseTypeDto> response = client.postForEntity("/coursetypes", newCourseType, CourseTypeDto.class);
        return response.getBody().getId();
    }
}

class CourseTypeDto {
    private int id;
    private String description;
    private boolean approvedByOwner;
    private float price;

    // Default constructor for JSON deserialization
    public CourseTypeDto() {}

    // Getters and setters for all fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApprovedByOwner() {
        return approvedByOwner;
    }

    public void setApprovedByOwner(boolean approvedByOwner) {
        this.approvedByOwner = approvedByOwner;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
