package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.* ;
import ca.mcgill.ecse321.SportsSchedulePlus.model.* ;
import ca.mcgill.ecse321.SportsSchedulePlus.service.InstructorService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.PersonService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.ScheduledCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.* ;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")@RestController
public class InstructorController {

  @Autowired
  private InstructorService instructorService;

  @Autowired
  private ScheduledCourseService scheduledCourseService;

  @Autowired
  private PersonService personService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping(value = { "/instructors"})
  public List < PersonDTO > getAllInstructors() {
    return instructorService.getAllInstructors().stream().map(instructor ->convertToDTO(instructor)).collect(Collectors.toList());

  }

  @GetMapping(value = {"/instructors/{experience}"})
  public List < PersonDTO > getInstructorByExperience(@PathVariable("experience") String experience) {
    return instructorService.getInstructorByExperience(experience).stream().map(instructor ->convertToDTO(instructor)).collect(Collectors.toList());
  }

  @GetMapping(value = {"/instructors/{email}"})
  public PersonDTO getInstructor(@PathVariable("email") String email) {
    Instructor instructor = instructorService.getInstructor(email);
    return convertToDTO(instructor);
  }

  @GetMapping(value = { "/instructors/supervised-course/{id}"})
  public List < PersonDTO > getInstructorsBySupervisedCourse(@PathVariable("id") int scheduledCourseId) {
    ScheduledCourse scheduledCourse = scheduledCourseService.getScheduledCourse(scheduledCourseId);
    return instructorService.getInstructorsBySupervisedCourse(scheduledCourse).stream().map(instructor ->convertToDTO(instructor)).collect(Collectors.toList());
  }

  @GetMapping(value = {"/instructors/suggested-courses"})
  public PersonDTO getInstructorsBySuggestedCourse(@RequestBody CourseTypeDTO courseTypeDTO) {
    CourseType courseType = new CourseType(courseTypeDTO.getDescription(), courseTypeDTO.isApprovedByOwner(), courseTypeDTO.getPrice());
    return convertToDTO(instructorService.getInstructorBySuggestedCourseTypes(courseType));
  }

  @DeleteMapping(value = {"/instructors/{id}"})
  public String deleteInstructor(@PathVariable("id") int id) {
    int personId = instructorService.deleteInstructor(id);
    return ("Instructor with id " + personId + " was successfully deleted.");
  }

  @PostMapping(value = {"/instructors/{email}"})
  public PersonDTO createInstructor(@PathVariable("email") String email, @RequestBody String experience) {
    Person person = instructorService.createInstructor(email, experience);
    return convertToDTO(person);
  }

  @PutMapping(value = {"/instructors/{id}"})
  public PersonDTO updateInstructor(@PathVariable("id") int id, @RequestBody PersonDTO personDTO, @RequestBody InstructorDTO instructorDTO) {
    Person person = instructorService.updateInstructor(id, personDTO.getName(), personDTO.getEmail(), passwordEncoder.encode(personDTO.getPassword()), instructorDTO.getExperience());
    return convertToDTO(person);
  }

  private PersonDTO convertToDTO(Person p) {
    if (p == null) {
      throw new IllegalArgumentException("There is no such customer!");
    }
    Instructor instructor = instructorService.getInstructor(p.getEmail());
    InstructorDTO instructorDTO = new InstructorDTO(instructor);
    PersonDTO personDTO = new PersonDTO(p.getName(), p.getEmail(), p.getPassword(), instructorDTO);
    return personDTO;
  }

  private PersonDTO convertToDTO(Instructor i) {
    if (i == null) {
      throw new IllegalArgumentException("There is no such instructor!");
    }
    InstructorDTO instructorDTO = new InstructorDTO(i);
    Person person = personService.getPersonById(i.getId());
    PersonDTO personDTO = new PersonDTO(person.getName(), person.getEmail(), person.getPassword(), instructorDTO);
    return personDTO;
  }
}