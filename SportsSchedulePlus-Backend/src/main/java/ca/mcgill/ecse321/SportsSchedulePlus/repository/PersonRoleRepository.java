package ca.mcgill.ecse321.SportsSchedulePlus.repository;


import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
public interface PersonRoleRepository extends CrudRepository<PersonRole, Integer>{


    PersonRole findPersonRoleById(int id);
}