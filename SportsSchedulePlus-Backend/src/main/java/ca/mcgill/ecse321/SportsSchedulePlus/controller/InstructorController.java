package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.instructor.InstructorResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.ScheduledCourseService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class InstructorController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduledCourseService scheduledCourseService;



    @GetMapping(value = {"/instructors"})
    public List<PersonResponseDTO> getAllInstructors() {
        return userService.getAllInstructors().stream().map(instructor -> convertToDTO(instructor)).collect(Collectors.toList());

    }

    @GetMapping(value = {"/instructors/{experience}"})
    public List<PersonResponseDTO> getInstructorByExperience(@PathVariable("experience") String experience) {
        return userService.getInstructorByExperience(experience).stream().map(instructor -> convertToDTO(instructor)).collect(Collectors.toList());
    }

    @GetMapping(value = {"/instructors/{email}"})
    public PersonResponseDTO getInstructor(@PathVariable("email") String email) {
        Instructor instructor = userService.getInstructor(email);
        return convertToDTO(instructor);
    }

    @GetMapping(value = {"/instructors/supervised-course/{id}"})
    public List<PersonResponseDTO> getInstructorsBySupervisedCourse(@PathVariable("id") int scheduledCourseId) {
        ScheduledCourse scheduledCourse = scheduledCourseService.getScheduledCourse(scheduledCourseId);
        return userService.getInstructorsBySupervisedCourse(scheduledCourse).stream().map(instructor -> convertToDTO(instructor)).collect(Collectors.toList());
    }

    @GetMapping(value = {"/instructors/suggested-courses"})
    public PersonResponseDTO getInstructorsBySuggestedCourse(@RequestBody CourseTypeRequestDTO courseTypeDTO) {
        CourseType courseType = new CourseType(courseTypeDTO.getDescription(), courseTypeDTO.isApprovedByOwner(), courseTypeDTO.getPrice());
        return convertToDTO(userService.getInstructorBySuggestedCourseTypes(courseType));
    }

    @DeleteMapping(value = {"/instructors/{id}"})
    public ResponseEntity<String> deleteInstructor(@PathVariable("id") int id) {
        int personId = userService.deleteInstructor(id);
        return ResponseEntity.ok("Instructor with id " + personId + " was successfully deleted.");
    }

    @PostMapping(value = {"/instructors/{email}"})
    public PersonResponseDTO createInstructor(@PathVariable("email") String email, @RequestBody String experience) {
        Person person = userService.createInstructor(email, experience);
        return convertToDTO(person);
    }

    @PutMapping(value = {"/instructors/{id}"})
    public PersonResponseDTO updateInstructor(@PathVariable("id") int id, @RequestBody Map<String, String> json) {
       

        String updatedName = json.get("name");
        String updatedEmail = json.get("email");
        String updatedPassword = json.get("password");
        String updatedExperience = json.get("experience");

        // Use the extracted values for the update
        Person person = userService.updateUser(id, updatedName, updatedEmail, updatedPassword, updatedExperience);

        return convertToDTO(person);
    }

    @PostMapping(value = {"/instructors/{email}/suggest-course"})
    public ResponseEntity<String> suggestCourseType(@PathVariable("email") String email, @RequestBody CourseTypeRequestDTO courseTypeRequestDTO) {
        // Get the instructor by email
        Instructor instructor = userService.getInstructor(email);
        if (instructor == null) {
            return ResponseEntity.notFound().build();
        }

        CourseType courseType = new CourseType(courseTypeRequestDTO.getDescription(), courseTypeRequestDTO.isApprovedByOwner(), courseTypeRequestDTO.getPrice());
        userService.suggestCourseType(instructor, courseType);

        return ResponseEntity.ok("Course type suggested successfully.");
    }


    private PersonResponseDTO convertToDTO(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("There is no such customer!");
        }
        Instructor instructor = userService.getInstructor(person.getEmail());
        InstructorResponseDTO instructorDTO = new InstructorResponseDTO(instructor);
        PersonResponseDTO personDTO = new PersonResponseDTO(person.getName(), person.getEmail(), person.getPassword(), instructorDTO);
        return personDTO;
    }

    private PersonResponseDTO convertToDTO(Instructor instructor) {
        if (instructor == null) {
            throw new IllegalArgumentException("There is no such instructor!");
        }
        InstructorResponseDTO instructorDTO = new InstructorResponseDTO(instructor);
        Person person = userService.getPersonById(instructor.getId());
        PersonResponseDTO personDTO = new PersonResponseDTO(person.getName(), person.getEmail(), person.getPassword(), instructorDTO);
        return personDTO;
    }
}