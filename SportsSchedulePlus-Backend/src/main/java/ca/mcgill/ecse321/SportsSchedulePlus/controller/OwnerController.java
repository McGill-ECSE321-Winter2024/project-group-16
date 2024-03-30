package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.SportsSchedulePlus.model.*;

/**
 * Rest Controller that handles CRUD on Owner
 */
@CrossOrigin(origins = "http://localhost:8087")

@RestController
public class OwnerController {

    @Autowired
    private UserService userService;


    /**
     * Retrieves the owner
     * @return PersonDTO
     */
    @GetMapping(value = {"/owner", "/owner/"})
    public PersonDTO getOwner() {
        Owner owner = userService.getOwner();
        Person person = userService.getPersonById(owner.getId());
        PersonDTO responseDTO = new PersonDTO(person,owner);
        return responseDTO;
    }
    
    /**
     * Creates the owner
     * @return PersonDTO
     */
    @PostMapping(value = {"/owner", "/owner/"})
    public PersonDTO createOwner() {
        Person person = userService.createOwner();
        Owner owner = userService.getOwner();
        PersonDTO responseDTO = new PersonDTO(person,owner);
        return responseDTO;
    }
    /**
     * Updates the owner with the information in the request body
     * @param personDTO
     * @return PersonDTO
     */
    @PutMapping(value = {"/owner", "/owner/"})
    public PersonDTO updateOwner(@RequestBody PersonDTO personDTO) {
        Owner owner = userService.getOwner();
        Person person = userService.updateUser(owner.getId(), personDTO.getName(), "sports.schedule.plus@gmail.com", personDTO.getPassword(), "");
        PersonDTO responseDTO = new PersonDTO(person,owner);
        return responseDTO;
    }
    
  
    /**
     * Allows the owner to suggest a course type using the information in the request body
     * @param request
     * @return  CourseTypeResponseDTO
     */
    @PostMapping(value = {"/owner/suggest-course"})
    public ResponseEntity<String> suggestCourseType(@RequestBody CourseTypeRequestDTO request) {
        PersonRole owner = userService.getPersonById(userService.getOwner().getId()).getPersonRole();
        CourseType courseType = new CourseType(request.getDescription(), false,request.getPrice());
        userService.suggestCourseType(owner, courseType);
        return ResponseEntity.ok("Course type suggested successfully.");
    }


}