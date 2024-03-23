package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
/**
 *  Interface for managing data related to CourseTypes in the application
 */
public interface CourseTypeRepository extends CrudRepository<CourseType, Integer> {

    // Find CourseType by description
    CourseType findCourseTypeByDescription(String description);

    // Find CourseType by price less than
    List<CourseType> findByPriceLessThan(float maxPrice);

    // Find CourseType by price
    List<CourseType> findByPrice(float price);

    // Find CourseType that is approved by the owner
    List<CourseType> findByApprovedByOwnerTrue();

    // Find CourseType that is not approved by the owner
    List<CourseType> findByApprovedByOwnerFalse();



    
}
