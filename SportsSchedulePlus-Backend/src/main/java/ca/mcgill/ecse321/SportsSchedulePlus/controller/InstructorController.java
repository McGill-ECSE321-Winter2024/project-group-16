package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.*;
import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.service.InstructorService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.PersonService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.ScheduledCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping(value={"/instructors"})
    public List<PersonDTO> getAllInstructors(){
        return instructorService.getAllInstructors().stream().map
                (instructor -> convertToDTO(instructor)).collect(Collectors.toList());

    }

    @GetMapping(value={"/instructors/{experience}"})
    public List<PersonDTO> getInstructorByExperience(@PathVariable("experience") String experience){
        return instructorService.getInstructorByExperience(experience).stream().map
                (instructor -> convertToDTO(instructor)).collect(Collectors.toList());
    }

    @GetMapping(value={"/instructors/{id}"})
    public PersonDTO getInstructor(@PathVariable("id") int id){
        Instructor instructor = instructorService.getInstructor(id);
        return convertToDTO(instructor);
    }

    @GetMapping(value={"/instructors/supervised-course/{id}"})
    public List<PersonDTO> getInstructorsBySupervisedCourse(@PathVariable("id") int scheduledCourseId){
        ScheduledCourse scheduledCourse = scheduledCourseService.getScheduledCourse(scheduledCourseId);
        return instructorService.getInstructorsBySupervisedCourse(scheduledCourse).stream().map
                (instructor -> convertToDTO(instructor)).collect(Collectors.toList());
    }

    @GetMapping(value={"/instructors/suggested-courses"})
    public PersonDTO getInstructorsBySuggestedCourse(@RequestBody CourseTypeDTO courseTypeDTO){
        CourseType courseType = new CourseType(courseTypeDTO.getDescription(), courseTypeDTO.isApprovedByOwner(), courseTypeDTO.getPrice());
        return convertToDTO(instructorService.getInstructorBySuggestedCourseTypes(courseType));
    }

    @DeleteMapping(value={"/instructors/{id}"})
    public PersonDTO deleteInstructor(@PathVariable("id") int id){
        Instructor instructor = instructorService.deleteInstructor(id);
        return convertToDTO(instructor);
    }

    @PostMapping(value={"/instructors"})
    public PersonDTO createInstructor(@RequestBody PersonDTO personDTO, @RequestBody InstructorDTO instructorDTO){
        Person person = instructorService.createInstructor(personDTO.getName(),
                personDTO.getEmail(), personDTO.getPassword(), instructorDTO.getExperience(), instructorDTO.getId());
        return convertToDTO(person);
    }

    @PutMapping(value={"/instructors/{id}"})
    public PersonDTO updateInstructor(@PathVariable("id") int id, @RequestBody PersonDTO personDTO, @RequestBody InstructorDTO instructorDTO){
        Person person = instructorService.updateInstructor(personDTO.getName(), personDTO.getEmail(), personDTO.getPassword(), instructorDTO.getExperience(), instructorDTO.getId());
        return convertToDTO(person);
    }

    private PersonDTO convertToDTO(Person p){
        if (p == null){
            throw new IllegalArgumentException("There is no such customer!");
        }
        PersonRoleDTO personRoleDTO = new InstructorDTO();
        PersonDTO instructorDTO = new PersonDTO(p.getName(), p.getEmail(), p.getPassword(), personRoleDTO);
        return instructorDTO;
    }
    
    private PersonDTO convertToDTO(Instructor i){
        if (i == null){
            throw new IllegalArgumentException("There is no such instructor!");
        }
        PersonRoleDTO personRoleDTO = new InstructorDTO(i.getId(), i.getExperience());
        Person person = personService.getPersonById(i.getId());
        PersonDTO personDTO = new PersonDTO(person.getName(), person.getEmail(), person.getPassword(), personRoleDTO);
        return personDTO;
    }
}
