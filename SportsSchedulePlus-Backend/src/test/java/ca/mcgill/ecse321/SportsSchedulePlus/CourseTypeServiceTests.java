package ca.mcgill.ecse321.SportsSchedulePlus;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.CourseTypeService;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;

@ExtendWith(MockitoExtension.class)
public class CourseTypeServiceTests {

    @Mock
    private CourseTypeRepository courseTypeRepository;

    @InjectMocks
    private CourseTypeService courseTypeService;

    private static final int COURSE_TYPE_ID = 1;
    private static final String NAME = "Yoga";
    private static final String DESCRIPTION = "Yoga for beginners";
    private static final String IMAGE = "yoga.jpg";
    private static final boolean APPROVED_BY_OWNER = true;
    private static final float PRICE = 20.0f;
    private List < CourseType > allCourseTypesList = new ArrayList < > ();

    @BeforeEach
    public void setMockOutput() {
        lenient().when(courseTypeRepository.findById(any())).thenAnswer(invocation -> {
            if (invocation.getArgument(0).equals(COURSE_TYPE_ID)) {
                CourseType courseType = new CourseType();
                courseType.setName(NAME);
                courseType.setApprovedByOwner(APPROVED_BY_OWNER);
                courseType.setPrice(PRICE);
                return Optional.of(courseType);
            }
            return Optional.empty();
        });

        lenient().when(courseTypeRepository.save(any(CourseType.class))).thenAnswer(invocation -> {
            CourseType courseType = invocation.getArgument(0);
            allCourseTypesList.add(courseType);
            return courseType;
        });

        lenient().when(courseTypeRepository.findAll()).thenReturn(allCourseTypesList);

        lenient().when(courseTypeRepository.findByPrice(anyFloat())).thenAnswer(invocation -> {
            float price = invocation.getArgument(0);
            if (price == PRICE) {
                CourseType courseType = new CourseType();
                courseType.setName(NAME);
                courseType.setApprovedByOwner(APPROVED_BY_OWNER);
                courseType.setPrice(PRICE);
                List < CourseType > result = new ArrayList < > ();
                result.add(courseType);
                return result;
            }
            return new ArrayList < CourseType > (); // Return an empty list if the price doesn't match
        });

        lenient().when(courseTypeRepository.findByApprovedByOwnerTrue()).thenAnswer(invocation -> {
            CourseType courseType = new CourseType();
            courseType.setName(NAME);
            courseType.setApprovedByOwner(true); // This is redundant as we're mocking findByApprovedByOwnerTrue
            courseType.setPrice(PRICE);
            List < CourseType > approvedCourseTypes = new ArrayList < > ();
            approvedCourseTypes.add(courseType);
            return approvedCourseTypes;
        });
        CourseType courseType = new CourseType();
        courseType.setName("Some Description");
        courseType.setApprovedByOwner(true);
        courseType.setPrice(20.0f);
        List < CourseType > courseTypeList = new ArrayList < > ();
        courseTypeList.add(courseType);

        // Mocking findAll to return a non-empty list to simulate existing course types
        lenient().when(courseTypeRepository.findAll()).thenReturn(courseTypeList);

    }

    @Test
    public void testCreateCourseType() {
        allCourseTypesList.clear();
        Mockito.when(courseTypeRepository.findAll()).thenReturn(allCourseTypesList);
        assertEquals(0, courseTypeService.getAllCourseTypes().size());

        String name = "Pilates";
        String description = "Pilates for beginners";
        String image = "pilates.jpg";
        boolean approvedByOwner = true;
        float price = 30.0f;

        try {
            courseTypeService.createCourseType(name, description, image, approvedByOwner, price);
        } catch (SportsScheduleException e) {
            fail(e.getMessage());
        }

        List < CourseType > allCourseTypes = courseTypeService.getAllCourseTypes();
        assertFalse(allCourseTypes.isEmpty());
        assertTrue(allCourseTypes.stream().anyMatch(courseType -> name.equals(courseType.getName()) && description.equals(courseType.getDescription()) 
                    && image.equals(courseType.getImage()) && courseType.getPrice() == price && courseType.isApprovedByOwner() == approvedByOwner));
    }

    @Test
    public void testUpdateCourseType() {
        String newName = "Advanced Yoga";
        String newDescription = "Advanced Yoga. Prerequisite: Yoga for beginners";
        String newImage = "advanced_yoga.jpg";
        boolean newApprovedByOwner = false;
        float newPrice = 25.0f;

        CourseType updatedCourseType = courseTypeService.updateCourseType(COURSE_TYPE_ID, newName, newDescription, newImage, newApprovedByOwner, newPrice);

        assertNotNull(updatedCourseType);
        assertEquals(newName, updatedCourseType.getName());
        assertEquals(newPrice, updatedCourseType.getPrice());
        assertEquals(newApprovedByOwner, updatedCourseType.isApprovedByOwner());
    }

    @Test
    public void testGetCourseType() {
        CourseType courseType = courseTypeService.getCourseType(COURSE_TYPE_ID);

        assertNotNull(courseType);
        assertEquals(NAME, courseType.getName());
        assertEquals(PRICE, courseType.getPrice());
        assertEquals(APPROVED_BY_OWNER, courseType.isApprovedByOwner());
    }

    @Test
    public void testCourseTypeNotFound() {
        assertThrows(SportsScheduleException.class, () -> {
            courseTypeService.getCourseType(999); // Assuming ID 999 does not exist
        });
    }



    @Test
    public void testCreateCourseTypeWithEmptyDescription() {
        Exception exception = assertThrows(SportsScheduleException.class, () -> {
            courseTypeService.createCourseType("", "", "", APPROVED_BY_OWNER, PRICE);
        });

        assertEquals("Course name cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testCreateCourseTypeWithNegativePrice() {
        Exception exception = assertThrows(SportsScheduleException.class, () -> {
            courseTypeService.createCourseType(NAME, DESCRIPTION, IMAGE, APPROVED_BY_OWNER, -10.0f);
        });

        assertEquals("Course price must be greater than zero.", exception.getMessage());
    }

    @Test
    public void testGetCourseTypeByPrice() {
        List < CourseType > found = courseTypeService.getCourseTypeByPrice(PRICE);

        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertEquals(PRICE, found.get(0).getPrice());
    }

    @Test
    public void testGetCourseTypeByPriceNotFound() {
        when(courseTypeRepository.findByPrice(PRICE)).thenReturn(new ArrayList < > ());

        Exception exception = assertThrows(SportsScheduleException.class, () -> {
            courseTypeService.getCourseTypeByPrice(PRICE);
        });

        assertEquals("No course types found with price: " + PRICE, exception.getMessage());
    }

    @Test
    public void testGetByApprovedByOwnerTrue() {
        List < CourseType > found = courseTypeService.getByApprovedByOwner("true");

        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertTrue(found.get(0).getApprovedByOwner());
    }

    @Test
    public void testGetByApprovedByOwnerFalse() {
        when(courseTypeRepository.findByApprovedByOwnerFalse()).thenReturn(List.of());

        Exception exception = assertThrows(SportsScheduleException.class, () -> {
            courseTypeService.getByApprovedByOwner("false");
        });

        assertEquals("No course types found with approvedByOwner: false", exception.getMessage());
    }


    @Test
    public void testDeleteAllCourseTypes() {
        courseTypeService.deleteAllCourseTypes();

        verify(courseTypeRepository, times(1)).deleteAll();
    }

    @Test
    public void testDeleteCourseTypeNotFound() {
        int courseTypeID = 900;
        Exception exception = assertThrows(SportsScheduleException.class, () -> {
            courseTypeService.deleteCourseType(900);
        });

        assertEquals("There is no course type with ID " + courseTypeID + ".", exception.getMessage());
    }



}