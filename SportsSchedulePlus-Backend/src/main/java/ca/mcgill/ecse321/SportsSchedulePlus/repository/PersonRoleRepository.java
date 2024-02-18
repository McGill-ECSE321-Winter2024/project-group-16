package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;

/**
 * Interface for managing data related to PersonRoles in the application
 */
public interface PersonRoleRepository extends CrudRepository<PersonRole, Integer>{

    // Find person role by ID
    PersonRole findPersonRoleById(int id);
}
