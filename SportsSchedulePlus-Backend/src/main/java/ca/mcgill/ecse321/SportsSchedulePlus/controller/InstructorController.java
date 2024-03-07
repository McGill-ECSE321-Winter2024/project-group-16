package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.*;
import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.service.InstructorService;
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

    @GetMapping(value={"/instructors"})
    public List<InstructorDto> getAllInstructors(){
        return instructorService.getAllInstructors().stream().map
                (instructor -> convertToDto(instructor)).collect(Collectors.toList());

    }

    @GetMapping(value={"/instructors/{experience}"})
    public List<InstructorDto> getInstructorByExperience(@PathVariable("experience") String experience){
        return instructorService.getInstructorByExperience(experience).stream().map
                (instructor -> convertToDto(instructor)).collect(Collectors.toList());
    }

    @GetMapping(value={"/instructors/{id}"})
    public InstructorDto getInstructor(@PathVariable("id") int id){
        Instructor instructor = instructorService.getInstructor(id);
        return convertToDto(instructor);
    }

    @GetMapping(value={"/instructors/supervised-course/{id}"})
    public List<InstructorDto> getInstructorsBySupervisedCourse(@PathVariable("id") int scheduledCourseId){
        ScheduledCourse scheduledCourse = scheduledCourseService.getScheduledCourse(scheduledCourseId);
        return instructorService.getInstructorsBySupervisedCourse(scheduledCourse).stream().map
                (instructor -> convertToDto(instructor)).collect(Collectors.toList());
    }

    @GetMapping(value={"/instructors/suggested-courses"})
    public InstructorDto getInstructorsBySuggestedCourse(@RequestParam CourseTypeDto courseTypeDto){
        CourseType courseType = new CourseType(courseTypeDto.getDescription(), courseTypeDto.isApprovedByOwner(), courseTypeDto.getPrice());
        return convertToDto(instructorService.getInstructorBySuggestedCourseTypes(courseType));
    }

    @DeleteMapping(value={"/instructors/{id}"})
    public InstructorDto deleteInstructor(@PathVariable("id") int id){
        Instructor instructor = instructorService.deleteInstructor(id);
        return convertToDto(instructor);
    }

    @PostMapping(value={"/instructors"})
    public PersonDto createInstructor(@RequestParam PersonDto personDto, @RequestParam InstructorDto instructorDto){
        Person person = instructorService.createInstructor(personDto.getName(),
                personDto.getEmail(), personDto.getPassword(), instructorDto.getExperience(), instructorDto.getId());
        return convertToDto(person);
    }

    @PutMapping(value={"/instructors/{id}"})
    public PersonDto updateInstructor(@PathVariable("id") int id, @RequestParam PersonDto personDto, @RequestParam InstructorDto instructorDto){
        Person person = instructorService.updateInstructor(personDto.getName(), personDto.getEmail(), personDto.getPassword(), instructorDto.getExperience(), instructorDto.getId());
        return convertToDto(person);
    }

    private PersonDto convertToDto(Person p){
        if (p == null){
            throw new IllegalArgumentException("There is no such customer!");
        }
        PersonRoleDto personRoleDto = new InstructorDto();
        PersonDto instructorDto = new PersonDto(p.getName(), p.getEmail(), p.getPassword(),personRoleDto);
        return instructorDto;
    }
    private InstructorDto convertToDto(Instructor i){
        if (i == null){
            throw new IllegalArgumentException("There is no such instructor!");
        }
        InstructorDto instructorDto = new InstructorDto(i.getId(), i.getExperience());
        return instructorDto;
    }



}
