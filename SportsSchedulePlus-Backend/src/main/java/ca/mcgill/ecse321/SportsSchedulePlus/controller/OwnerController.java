package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.* ;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.* ;
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
  public PersonDTO getOwner() {
    Owner owner = ownerService.getOwner();
    return convertToDTO(owner);
  }

  @GetMapping(value = {"/owner/suggested-courses"})
  public PersonDTO getOwnerByOwnerSuggestedCourses(@RequestBody CourseTypeDTO courseTypeDTO) {
    CourseType courseType = new CourseType(courseTypeDTO.getDescription(), courseTypeDTO.isApprovedByOwner(), courseTypeDTO.getPrice());
    Owner owner = ownerService.getInstructorByOwnerSuggestedCourses(courseType);
    return convertToDTO(owner);

  }

  @GetMapping(value = {"/owner/approved-courses"})
  public PersonDTO getOwnerByApprovedCourses(@RequestBody CourseTypeDTO courseTypeDTO) {
    CourseType courseType = new CourseType(courseTypeDTO.getDescription(), courseTypeDTO.isApprovedByOwner(), courseTypeDTO.getPrice());
    Owner owner = ownerService.getOwnerByApprovedCourses(courseType);
    return convertToDTO(owner);
  }

  @PostMapping(value = {"/owner", "/owner/"})
  public PersonDTO createOwner() {
    Person person = ownerService.createOwner();
    return convertToDTO(person);
  }

  @PutMapping(value = {"/owner", "/owner/"})
  public PersonDTO updateOwner(@RequestBody PersonDTO personDTO) {
    Person person = ownerService.updateOwner(personDTO.getName(), personDTO.getPassword());
    return convertToDTO(person);
  }

  private PersonDTO convertToDTO(Person p) {
    if (p == null) {
      throw new IllegalArgumentException("There is no such owner!");
    }
    Owner owner = ownerService.getOwner();
    OwnerDTO ownerDTO = new OwnerDTO(owner);
    PersonDTO personDTO = new PersonDTO(p.getName(), p.getEmail(), p.getPassword(), ownerDTO);
    return personDTO;
  }

  private PersonDTO convertToDTO(Owner owner) {
    if (owner == null) {
      throw new IllegalArgumentException("There is no such owner!");
    }
    OwnerDTO ownerDTO = new OwnerDTO(owner);
    Person person = personService.getPersonById(owner.getId());
    PersonDTO personDTO = new PersonDTO(person.getName(), person.getEmail(), person.getPassword(), ownerDTO);
    return personDTO;
  }

  
}