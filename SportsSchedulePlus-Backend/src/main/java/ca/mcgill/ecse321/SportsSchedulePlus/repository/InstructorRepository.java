package ca.mcgill.ecse321.SportsSchedulePlus.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;


public interface InstructorRepository extends CrudRepository<Instructor,Integer> {
    
 
   Instructor findInstructorById(int id);

}