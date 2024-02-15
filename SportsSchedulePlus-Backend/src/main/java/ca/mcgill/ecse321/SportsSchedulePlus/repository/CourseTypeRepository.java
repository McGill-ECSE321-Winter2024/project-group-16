package ca.mcgill.ecse321.SportsSchedulePlus.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;

public interface CourseTypeRepository extends CrudRepository<CourseType, Integer> {
    
    CourseType findCourseTypeById(Integer id);

    CourseType findCourseTypeByDescription(String description);

    List<CourseType> findByApprovedByOwnerTrue();
}