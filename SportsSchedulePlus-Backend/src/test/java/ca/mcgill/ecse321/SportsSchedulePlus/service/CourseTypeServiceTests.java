package ca.mcgill.ecse321.SportsSchedulePlus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;

@ExtendWith(MockitoExtension.class)
public class CourseTypeServiceTests {

    @Mock
    CourseTypeRepository courseTypeRepository;

    @InjectMocks
    CourseTypeService courseTypeService;

    @Test
    public void testCreateCourseTypeSuccess() {
        // Arrange
        String description = "Yoga";
        boolean approvedByOwner = true;
        float price = 20.0f;
        CourseType mockCourseType = new CourseType();
        mockCourseType.setDescription(description);
        mockCourseType.setApprovedByOwner(approvedByOwner);
        mockCourseType.setPrice(price);
        when(courseTypeRepository.save(any(CourseType.class))).thenReturn(mockCourseType);
        
        // Act
        CourseType createdCourseType = courseTypeService.createCourseType(description, approvedByOwner, price);
        
        // Assert
        assertNotNull(createdCourseType);
        assertEquals(description, createdCourseType.getDescription());
        assertEquals(approvedByOwner, createdCourseType.getApprovedByOwner());
        assertEquals(price, createdCourseType.getPrice());
    }
}
/* 
    @Test
    public void testCreateCourseType() {
        // Arrange
        String description = "Yoga";
        boolean approvedByOwner = true;
        float price = 20.0f;

        // Act
        CourseType courseType = null;
        try {
            courseType = courseTypeService.createCourseType(description, approvedByOwner, price);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }

        // Assert
        assertNotNull(courseType);
        assertEquals(description, courseType.getDescription());
        assertEquals(approvedByOwner, courseType.getApprovedByOwner());
        assertEquals(price, courseType.getPrice());
    }

    @Test
    public void testUpdateCourseType() {
        // Arrange
        String newDescription = "Pilates";
        boolean newApprovedByOwner = false;
        float newPrice = 25.0f;

        // Act
        CourseType courseType = null;
        try {
            courseType = courseTypeService.updateCourseType(COURSE_TYPE_ID, newDescription, newApprovedByOwner, newPrice);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }

        // Assert
        assertNotNull(courseType);
        assertEquals(newDescription, courseType.getDescription());
        assertEquals(newApprovedByOwner, courseType.getApprovedByOwner());
        assertEquals(newPrice, courseType.getPrice());
    }

    @Test
    public void testGetCourseType() {
        // Arrange (setup in @BeforeEach)

        // Act
        CourseType courseType = courseTypeService.getCourseType(COURSE_TYPE_ID);

        // Assert
        assertNotNull(courseType);
        assertEquals(DESCRIPTION, courseType.getDescription());
    }

    @Test
    public void testGetCourseTypeNotFound() {
        // Arrange
        when(courseTypeRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SportsScheduleException.class, () -> {
            courseTypeService.getCourseType(COURSE_TYPE_ID);
        });
    }

    @Test
    public void testGetAllCourseTypes() {
        // Arrange (additional mock setups might be required)

        // Act
        List<CourseType> courseTypes = courseTypeService.getAllCourseTypes();

        // Assert
        assertNotNull(courseTypes);
        // Perform additional assertions as necessary
    }

    @Test
    public void testDeleteCourseType() {
        // Arrange (additional mock setups might be required)

        // Act
        courseTypeService.deleteCourseType(COURSE_TYPE_ID);

        // Assert
        verify(courseTypeRepository).deleteById(COURSE_TYPE_ID);
    }

    @Test
    public void testDeleteCourseTypeNotFound() {
        // Arrange
        when(courseTypeRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SportsScheduleException.class, () -> {
            courseTypeService.deleteCourseType(COURSE_TYPE_ID);
        });
    }

    @Test
    public void testGetCourseTypeByPrice() {
        // Arrange (additional mock setups might be required)

        // Act
        List<CourseType> courseTypes = courseTypeService.getCourseTypeByPrice(PRICE);

        // Assert
        assertNotNull(courseTypes);
        // Perform additional assertions as necessary
    }

    @Test
    public void testGetCourseTypeByPriceNotFound() {
        // Arrange
        when(courseTypeRepository.findByPrice(any(Float.class))).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(SportsScheduleException.class, () -> {
            courseTypeService.getCourseTypeByPrice(PRICE);
        });
    }

    @Test
    public void testGetByApprovedByOwner() {
        // Arrange (additional mock setups might be required)

        // Act
        List<CourseType> courseTypes = courseTypeService.getByApprovedByOwner(APPROVED_BY_OWNER);

        // Assert
        assertNotNull(courseTypes);
        // Perform additional assertions as necessary
    }

    @Test
    public void testGetByApprovedByOwnerNotFound() {
        // Arrange
        when(courseTypeRepository.findByApprovedByOwnerTrue()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(SportsScheduleException.class, () -> {
            courseTypeService.getByApprovedByOwner(APPROVED_BY_OWNER);
        });
    }

    @Test
    public void testUpdateCourseTypeApproval() {
        // Arrange (additional mock setups might be required)

        // Act
        CourseType courseType = courseTypeService.updateCourseTypeApproval(COURSE_TYPE_ID, APPROVED_BY_OWNER);

        // Assert
        assertNotNull(courseType);
        assertEquals(APPROVED_BY_OWNER, courseType.getApprovedByOwner());
    }

    // ... any additional tests for other methods
}
*/

    

