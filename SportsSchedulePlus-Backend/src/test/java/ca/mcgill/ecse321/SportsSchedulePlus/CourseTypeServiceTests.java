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
    private static final String DESCRIPTION = "Yoga";
    private static final boolean APPROVED_BY_OWNER = true;
    private static final float PRICE = 20.0f;
    private List < CourseType > allCourseTypesList = new ArrayList < > ();

    @BeforeEach
    public void setMockOutput() {
        lenient().when(courseTypeRepository.findById(any())).thenAnswer(invocation -> {
            if (invocation.getArgument(0).equals(COURSE_TYPE_ID)) {
                CourseType ct = new CourseType();
                ct.setDescription(DESCRIPTION);
                ct.setApprovedByOwner(APPROVED_BY_OWNER);
                ct.setPrice(PRICE);
                return Optional.of(ct);
            }
            return Optional.empty();
        });

        lenient().when(courseTypeRepository.save(any(CourseType.class))).thenAnswer(invocation -> {
            CourseType ct = invocation.getArgument(0);
            allCourseTypesList.add(ct);
            return ct;
        });

        lenient().when(courseTypeRepository.findAll()).thenReturn(allCourseTypesList);

        lenient().when(courseTypeRepository.findByPrice(anyFloat())).thenAnswer(invocation -> {
            float price = invocation.getArgument(0);
            if (price == PRICE) {
                CourseType ct = new CourseType();
                ct.setDescription(DESCRIPTION);
                ct.setApprovedByOwner(APPROVED_BY_OWNER);
                ct.setPrice(PRICE);
                List < CourseType > result = new ArrayList < > ();
                result.add(ct);
                return result;
            }
            return new ArrayList < CourseType > (); // Return an empty list if the price doesn't match
        });

        lenient().when(courseTypeRepository.findByApprovedByOwnerTrue()).thenAnswer(invocation -> {
            CourseType ct = new CourseType();
            ct.setDescription(DESCRIPTION);
            ct.setApprovedByOwner(true); // This is redundant as we're mocking findByApprovedByOwnerTrue
            ct.setPrice(PRICE);
            List < CourseType > approvedCourseTypes = new ArrayList < > ();
            approvedCourseTypes.add(ct);
            return approvedCourseTypes;
        });
        CourseType ct = new CourseType();
        ct.setDescription("Some Description");
        ct.setApprovedByOwner(true);
        ct.setPrice(20.0f);
        List < CourseType > courseTypeList = new ArrayList < > ();
        courseTypeList.add(ct);

        // Mocking findAll to return a non-empty list to simulate existing course types
        lenient().when(courseTypeRepository.findAll()).thenReturn(courseTypeList);

    }

    @Test
    public void testCreateCourseType() {
        allCourseTypesList.clear();
        Mockito.when(courseTypeRepository.findAll()).thenReturn(allCourseTypesList);
        assertEquals(0, courseTypeService.getAllCourseTypes().size());

        String description = "Pilates";
        boolean approvedByOwner = true;
        float price = 30.0f;

        try {
            courseTypeService.createCourseType(description, approvedByOwner, price);
        } catch (SportsScheduleException e) {
            fail(e.getMessage());
        }

        List < CourseType > allCourseTypes = courseTypeService.getAllCourseTypes();
        assertFalse(allCourseTypes.isEmpty());
        assertTrue(allCourseTypes.stream().anyMatch(ct -> description.equals(ct.getDescription()) && ct.getPrice() == price && ct.isApprovedByOwner() == approvedByOwner));
    }

    @Test
    public void testUpdateCourseType() {
        String newDescription = "Advanced Yoga";
        boolean newApprovedByOwner = false;
        float newPrice = 25.0f;

        CourseType updatedCourseType = courseTypeService.updateCourseType(COURSE_TYPE_ID, newDescription, newApprovedByOwner, newPrice);

        assertNotNull(updatedCourseType);
        assertEquals(newDescription, updatedCourseType.getDescription());
        assertEquals(newPrice, updatedCourseType.getPrice());
        assertEquals(newApprovedByOwner, updatedCourseType.isApprovedByOwner());
    }

    @Test
    public void testGetCourseType() {
        CourseType courseType = courseTypeService.getCourseType(COURSE_TYPE_ID);

        assertNotNull(courseType);
        assertEquals(DESCRIPTION, courseType.getDescription());
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
            courseTypeService.createCourseType("", APPROVED_BY_OWNER, PRICE);
        });

        assertEquals("Course description cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testCreateCourseTypeWithNegativePrice() {
        Exception exception = assertThrows(SportsScheduleException.class, () -> {
            courseTypeService.createCourseType(DESCRIPTION, APPROVED_BY_OWNER, -10.0f);
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