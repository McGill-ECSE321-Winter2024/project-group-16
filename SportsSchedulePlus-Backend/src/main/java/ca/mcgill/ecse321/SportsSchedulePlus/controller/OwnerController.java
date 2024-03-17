package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.owner.OwnerResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.SportsSchedulePlus.model.*;


@CrossOrigin(origins = "*")

@RestController
public class OwnerController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseTypeService courseTypeService;


    @GetMapping(value = {"/owner", "/owner/"})
    public PersonResponseDTO getOwner() {
        Owner owner = userService.getOwner();
        return convertToDTO(owner);
    }
    
    @PostMapping(value = {"/owner", "/owner/"})
    public PersonResponseDTO createOwner() {
        Person person = userService.createOwner();
        return convertToDTO(person);
    }

    @PutMapping(value = {"/owner", "/owner/"})
    public PersonResponseDTO updateOwner(@RequestBody PersonResponseDTO personDTO) {
        Person person = userService.updateUser(-1, personDTO.getName(), "sports.schedule.plus@gmail.com", personDTO.getPassword(), "");
        return convertToDTO(person);
    }

    @PostMapping("/owner/courseTypes")
    public CourseTypeResponseDTO createCourseType(@RequestBody CourseTypeRequestDTO request) {
        CourseType createdCourseType = courseTypeService.createCourseType(request.getDescription(), request.isApprovedByOwner(), request.getPrice());
        return new CourseTypeResponseDTO(createdCourseType);
    }

    private PersonResponseDTO convertToDTO(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("There is no such owner!");
        }
        Owner owner = userService.getOwner();
        OwnerResponseDTO ownerDTO = new OwnerResponseDTO(owner);
        PersonResponseDTO personDTO = new PersonResponseDTO(person.getName(), person.getEmail(), person.getPassword(), ownerDTO);
        return personDTO;
    }

    private PersonResponseDTO convertToDTO(Owner owner) {
        if (owner == null) {
            throw new IllegalArgumentException("There is no such owner!");
        }
        OwnerResponseDTO ownerDTO = new OwnerResponseDTO(owner);
        Person person = userService.getPersonById(owner.getId());
        PersonResponseDTO personDTO = new PersonResponseDTO(person.getName(), person.getEmail(), person.getPassword(), ownerDTO);
        return personDTO;
    }
}