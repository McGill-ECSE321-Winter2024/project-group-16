package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.owner.OwnerResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.* ;

import ca.mcgill.ecse321.SportsSchedulePlus.model.* ;
import ca.mcgill.ecse321.SportsSchedulePlus.service.OwnerService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.PersonService;


@CrossOrigin(origins = "*")

@RestController
public class OwnerController {

  @Autowired
  private OwnerService ownerService;
  
  @Autowired
  private PersonService personService;


  @GetMapping(value = {"/owner", "/owner/"})
  public PersonResponseDTO getOwner() {
    Owner owner = ownerService.getOwner();
    return convertToDTO(owner);
  }

  @GetMapping(value = {"/owner/suggested-courses"})
  public PersonResponseDTO getOwnerByOwnerSuggestedCourses(@RequestBody CourseTypeRequestDTO courseTypeDTO) {
    CourseType courseType = new CourseType(courseTypeDTO.getDescription(), courseTypeDTO.isApprovedByOwner(), courseTypeDTO.getPrice());
    Owner owner = ownerService.getInstructorByOwnerSuggestedCourses(courseType);
    return convertToDTO(owner);

  }

  @GetMapping(value = {"/owner/approved-courses"})
  public PersonResponseDTO getOwnerByApprovedCourses(@RequestBody CourseTypeRequestDTO courseTypeDTO) {
    CourseType courseType = new CourseType(courseTypeDTO.getDescription(), courseTypeDTO.isApprovedByOwner(), courseTypeDTO.getPrice());
    Owner owner = ownerService.getOwnerByApprovedCourses(courseType);
    return convertToDTO(owner);
  }

  @PostMapping(value = {"/owner", "/owner/"})
  public PersonResponseDTO createOwner() {
    Person person = ownerService.createOwner();
    return convertToDTO(person);
  }

  @PutMapping(value = {"/owner", "/owner/"})
  public PersonResponseDTO updateOwner(@RequestBody PersonResponseDTO personDTO) {
    Person person = ownerService.updateOwner(personDTO.getName(), personDTO.getPassword());
    return convertToDTO(person);
  }

  private PersonResponseDTO convertToDTO(Person p) {
    if (p == null) {
      throw new IllegalArgumentException("There is no such owner!");
    }
    Owner owner = ownerService.getOwner();
    OwnerResponseDTO ownerDTO = new OwnerResponseDTO(owner);
    PersonResponseDTO personDTO = new PersonResponseDTO(p.getName(), p.getEmail(), p.getPassword(), ownerDTO);
    return personDTO;
  }

  private PersonResponseDTO convertToDTO(Owner owner) {
    if (owner == null) {
      throw new IllegalArgumentException("There is no such owner!");
    }
    OwnerResponseDTO ownerDTO = new OwnerResponseDTO(owner);
    Person person = personService.getPersonById(owner.getId());
    PersonResponseDTO personDTO = new PersonResponseDTO(person.getName(), person.getEmail(), person.getPassword(), ownerDTO);
    return personDTO;
  }

  
}