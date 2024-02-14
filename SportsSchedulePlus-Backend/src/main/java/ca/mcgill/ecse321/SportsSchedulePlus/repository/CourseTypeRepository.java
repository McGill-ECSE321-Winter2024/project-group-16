package ca.mcgill.ecse321.SportsSchedulePlus.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;



import java.util.List;

public interface CourseTypeRepository extends CrudRepository<CourseType,Long> {
    
 // Find CourseType by description
 List<CourseType> findByDescription(String description);

 // Find CourseType by approval status
 List<CourseType> findByApprovedByOwner(boolean approvedByOwner);

 // Find CourseType by price
 List<CourseType> findByPrice(float price);
   

}