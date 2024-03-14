package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.instructor.InstructorResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.person_person_role.PersonResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.* ;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.InstructorService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.PersonService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.ScheduledCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.* ;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class InstructorController {

  @Autowired
  private InstructorService instructorService;

  @Autowired
  private ScheduledCourseService scheduledCourseService;

  @Autowired
  private PersonService personService;

  @GetMapping(value = { "/instructors"})
  public List <PersonResponseDTO> getAllInstructors() {
    return instructorService.getAllInstructors().stream().map(instructor ->convertToDTO(instructor)).collect(Collectors.toList());

  }

  @GetMapping(value = {"/instructors/{experience}"})
  public List < PersonResponseDTO > getInstructorByExperience(@PathVariable("experience") String experience) {
    return instructorService.getInstructorByExperience(experience).stream().map(instructor ->convertToDTO(instructor)).collect(Collectors.toList());
  }

  @GetMapping(value = {"/instructors/{email}"})
  public PersonResponseDTO getInstructor(@PathVariable("email") String email) {
    Instructor instructor = instructorService.getInstructor(email);
    return convertToDTO(instructor);
  }

  @GetMapping(value = { "/instructors/supervised-course/{id}"})
  public List < PersonResponseDTO > getInstructorsBySupervisedCourse(@PathVariable("id") int scheduledCourseId) {
    ScheduledCourse scheduledCourse = scheduledCourseService.getScheduledCourse(scheduledCourseId);
    return instructorService.getInstructorsBySupervisedCourse(scheduledCourse).stream().map(instructor ->convertToDTO(instructor)).collect(Collectors.toList());
  }

  @GetMapping(value = {"/instructors/suggested-courses"})
  public PersonResponseDTO getInstructorsBySuggestedCourse(@RequestBody CourseTypeRequestDTO courseTypeDTO) {
    CourseType courseType = new CourseType(courseTypeDTO.getDescription(), courseTypeDTO.isApprovedByOwner(), courseTypeDTO.getPrice());
    return convertToDTO(instructorService.getInstructorBySuggestedCourseTypes(courseType));
  }

  @DeleteMapping(value = {"/instructors/{id}"})
  public ResponseEntity<String> deleteInstructor(@PathVariable("id") int id) {
    int personId = instructorService.deleteInstructor(id);
    return ResponseEntity.ok("Instructor with id " + personId + " was successfully deleted.");
  }

  @PostMapping(value = {"/instructors/{email}"})
  public PersonResponseDTO createInstructor(@PathVariable("email") String email, @RequestBody String experience) {
    Person person = instructorService.createInstructor(email, experience);
    return convertToDTO(person);
  }

  @PutMapping(value = {"/instructors/{id}"})
  public PersonResponseDTO updateInstructor(@PathVariable("id") int id, @RequestBody Map<String, Map<String, String>> json) {
      Map<String, String> personDTO = json.get("personDTO");
      Map<String, String> instructorDTO = json.get("instructorDTO");
  
      String updatedName = personDTO.get("name");
      String updatedEmail = personDTO.get("email");
      String updatedPassword = personDTO.get("password");
      String updatedExperience = instructorDTO.get("experience");
  
      // Use the extracted values for the update
      Person person = instructorService.updateInstructor(id, updatedName, updatedEmail, updatedPassword, updatedExperience);
  
      return convertToDTO(person);
  }

  @PostMapping(value = {"/instructors/{email}/suggest-course"})
  public ResponseEntity<String> suggestCourseType(@PathVariable("email") String email, @RequestBody CourseTypeRequestDTO courseTypeRequestDTO) {
      // Get the instructor by email
      Instructor instructor = instructorService.getInstructor(email);
      if (instructor == null) {
          return ResponseEntity.notFound().build();
      }

      // Create a new course type based on the request DTO
      CourseType courseType = new CourseType(courseTypeRequestDTO.getDescription(), 
                                              courseTypeRequestDTO.isApprovedByOwner(), 
                                              courseTypeRequestDTO.getPrice());
      
      // Suggest the course type to the instructor
      instructorService.suggestCourseType(instructor, courseType);

      return ResponseEntity.ok("Course type suggested successfully.");
  }



  private PersonResponseDTO convertToDTO(Person person) {
    if (person == null) {
      throw new IllegalArgumentException("There is no such customer!");
    }
    Instructor instructor = instructorService.getInstructor(person.getEmail());
    InstructorResponseDTO instructorDTO = new InstructorResponseDTO(instructor);
    PersonResponseDTO personDTO = new PersonResponseDTO(person.getName(), person.getEmail(), person.getPassword(), instructorDTO);
    return personDTO;
  }

  private PersonResponseDTO convertToDTO(Instructor instructor) {
    if (instructor == null) {
      throw new IllegalArgumentException("There is no such instructor!");
    }
    InstructorResponseDTO instructorDTO = new InstructorResponseDTO(instructor);
    Person person = personService.getPersonById(instructor.getId());
    PersonResponseDTO personDTO = new PersonResponseDTO(person.getName(), person.getEmail(), person.getPassword(), instructorDTO);
    return personDTO;
  }
}