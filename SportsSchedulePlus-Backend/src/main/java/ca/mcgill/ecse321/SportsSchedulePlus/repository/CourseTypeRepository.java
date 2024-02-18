package ca.mcgill.ecse321.SportsSchedulePlus.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;

public interface CourseTypeRepository extends CrudRepository<CourseType,Integer> {

    // Find CourseType by description

    CourseType findCourseTypeByDescription(String description);

    List<CourseType> findByPriceLessThan(float maxPrice);

    List<CourseType> findByApprovedByOwnerTrue();

}