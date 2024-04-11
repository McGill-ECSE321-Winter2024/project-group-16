package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeListDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseListDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonListResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.ScheduledCourseService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Rest Controller that handles CRUD on Instructor
 */
@CrossOrigin(origins = "http://localhost:8087")
@RestController
public class InstructorController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduledCourseService scheduledCourseService;


    /**
     * Retrieves all instructors
     * @return PersonListResponseDTO
     */
    @GetMapping(value = {"/instructors"})
    public PersonListResponseDTO getAllInstructors() {
        List<PersonDTO> instructorDTOs = new ArrayList<>();
        for (Instructor instructor: userService.getAllInstructors()) {
            Person person = userService.getPersonById(instructor.getId());
            instructorDTOs.add(new PersonDTO(person));
        }
        return new PersonListResponseDTO(instructorDTOs);
    }
    
    /**
     * Retrives instructors by their experience
     * @param experience
     * @return PersonListResponseDTO
     */
    @GetMapping(value = {"/instructors/experience/{experience}"})
    public PersonListResponseDTO getInstructorByExperience(@PathVariable("experience") String experience) {
        List<PersonDTO> instructorDTOs = new ArrayList<>();
        for (Instructor instructor: userService.getInstructorByExperience(experience)) {
            Person person = userService.getPersonById(instructor.getId());
            instructorDTOs.add(new PersonDTO(person));
        }
        return new PersonListResponseDTO(instructorDTOs);
    }
    
    /**
     * Retrieves an instructor by their email
     * @param email
     * @return PersonDTO 
     */
    @GetMapping(value = {"/instructors/{email}"})
    public PersonDTO getInstructor(@PathVariable("email") String email) {
        Instructor instructor = userService.getInstructor(email);
        Person person =  userService.getPersonById(instructor.getId());
        return new PersonDTO(person);
    }
    
    /**
     * Retrieves instructors by their supervised course
     * @param scheduledCourseId
     * @return PersonListResponseDTO
     */
    @GetMapping(value = {"/instructors/supervised-course/{id}"})
    public PersonListResponseDTO getInstructorsBySupervisedCourse(@PathVariable("id") int scheduledCourseId) {
        ScheduledCourse scheduledCourse = scheduledCourseService.getScheduledCourse(scheduledCourseId);
        List<PersonDTO> instructorDTOs = new ArrayList<>();
        for (Instructor instructor: userService.getInstructorsBySupervisedCourse(scheduledCourse)) {
            Person person = userService.getPersonById(instructor.getId());
            instructorDTOs.add(new PersonDTO(person));
        }
        return new PersonListResponseDTO(instructorDTOs);
    }
    
    /**
     * Retrieves the supervised courses by the instructor with the path variable instructorEmail
     * @param instructorEmail
     * @return
     */
    @GetMapping(value = {"/instructors/{instructorEmail}/supervised-courses"})
    public ScheduledCourseListDTO getSupervisedCoursesByInstructor(@PathVariable("instructorEmail") String instructorEmail) {
        Instructor instructor = userService.getInstructor(instructorEmail);
        List<ScheduledCourse> scheduledCourses = userService.getSupervisedCourses(instructor);
        List<ScheduledCourseResponseDTO> scheduledCourseDTOs = new ArrayList<>();
        for (ScheduledCourse scheduledCourse : scheduledCourses) {
            scheduledCourseDTOs.add(new ScheduledCourseResponseDTO(scheduledCourse));
        }
        return new ScheduledCourseListDTO(scheduledCourseDTOs);
    }


    /**
     * Retrieves instructor by their suggested course type
     * @param courseTypeId
     * @return PersonDTO
     */
    @GetMapping(value = {"/instructors/suggestedCourses/{id}"})
    public PersonDTO getInstructorsBySuggestedCourse(@PathVariable("id") int courseTypeId) {
        Instructor instructor = userService.getInstructorBySuggestedCourseType(courseTypeId);
        Person person =  userService.getPersonById(instructor.getId());
        return new PersonDTO(person);
    }
    
    @GetMapping(value = {"/instructors/{instructorEmail}/suggested-course-types"})
    public CourseTypeListDTO getSuggestedCourseTypesByInstructor(@PathVariable("instructorEmail") String instructorEmail) {
        Instructor instructor = userService.getInstructor(instructorEmail);
        List<CourseType> courseTypes = userService.getInstructorSuggestedCourseTypes(instructor);
        List<CourseTypeResponseDTO> courseTypeDTOs = new ArrayList<>();
        for (CourseType courseType : courseTypes) {
            courseTypeDTOs.add(new CourseTypeResponseDTO(courseType));
        }
        return new CourseTypeListDTO(courseTypeDTOs);
    }
    
    /**
     * Deletes the instructor with the path variable id
     * @param id
     * @return String response entity
     */
    @DeleteMapping(value = {"/instructors/{id}"})
    public ResponseEntity<String> deleteInstructor(@PathVariable("id") int id) {
        int personId = userService.deleteUser(id);
        return ResponseEntity.ok("Instructor with id " + personId + " was successfully deleted.");
    }

    @PutMapping(value = {"/instructors/{id}"})
    public PersonDTO updateInstructor(@PathVariable("id") int id, @RequestBody Map<String, String> json) {
        String updatedName = json.get("name");
        String updatedEmail = json.get("email");
        String updatedPassword = json.get("password");
        String updatedExperience = json.get("experience");
        // Use the extracted values for the update
        Person person = userService.updateUser(id, updatedName, updatedEmail, updatedPassword, updatedExperience);
        return new PersonDTO(person);
    }
    /**
     * Allows an instructor to suggest a course type
     * @param email
     * @param courseTypeRequestDTO
     * @return String response entity
     */
    @PostMapping(value = {"/instructors/{email}/suggest-course"})
    public ResponseEntity<String> suggestCourseType(@PathVariable("email") String email, @RequestBody CourseTypeRequestDTO courseTypeRequestDTO) {
        // Get the instructor by email
        Instructor instructor = userService.getInstructor(email);
        if (instructor == null) {
            return ResponseEntity.notFound().build();
        }
        CourseType courseType = new CourseType(courseTypeRequestDTO.getName(), courseTypeRequestDTO.getDescription(), courseTypeRequestDTO.getImage(), false, courseTypeRequestDTO.getPrice());
        userService.suggestCourseType(instructor, courseType);
        return ResponseEntity.ok("Course type suggested successfully.");
    }
}