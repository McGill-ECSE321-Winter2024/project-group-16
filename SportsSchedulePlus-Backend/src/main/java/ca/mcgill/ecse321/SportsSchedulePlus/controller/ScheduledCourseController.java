package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.InstructorListDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.InstructorResponseDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.ScheduledCourseListDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.ScheduledCourseRequestDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.ScheduledCourseResponseDto;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.service.ScheduledCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ScheduledCourseController {

    @Autowired
    private ScheduledCourseService service;

    @PostMapping("/scheduledCourses")
    public ScheduledCourseResponseDto createScheduledCourse(@RequestBody ScheduledCourseRequestDto request) {
        // Assuming you have a createScheduledCourse method in your service
        ScheduledCourse createdScheduledCourse = service.createScheduledCourse(request.getId(),
                request.getDate(), request.getStartTime(), request.getEndTime(), request.getLocation(),
                request.getCourseType().getId());
        return new ScheduledCourseResponseDto(createdScheduledCourse);
    }

    @PutMapping("/scheduledCourses/{id}")
    public ScheduledCourseResponseDto updateScheduledCourse(@PathVariable(name = "id") int id,
                                                           @RequestBody ScheduledCourseRequestDto request) {
        ScheduledCourse updatedScheduledCourse = service.updateScheduledCourse(
                id,
                request.getDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getLocation(),
                request.getCourseType().getId()
        );
        return new ScheduledCourseResponseDto(updatedScheduledCourse);
    }

    @GetMapping("/scheduledCourses/instructors/{id}")
    public InstructorListDto getInstructorsBySupervisedCourse(@PathVariable(name = "id") int scheduledCourseId) {
        List<InstructorResponseDto> instructorDtos = new ArrayList<>();
        List<Instructor> instructors = service.getInstructorsBySupervisedCourse(scheduledCourseId);

        for (Instructor instructor : instructors) {
            instructorDtos.add(new InstructorResponseDto(instructor));
        }

        return new InstructorListDto(instructorDtos);
    }

    @GetMapping("/scheduledCourses/{id}")
    public ScheduledCourseResponseDto findScheduledCourseById(@PathVariable(name="id") int id) {
        ScheduledCourse scheduledCourse = service.getScheduledCourse(id);
        return new ScheduledCourseResponseDto(scheduledCourse);
    }

    @GetMapping("/scheduledCourses")
    public ScheduledCourseListDto findAllScheduledCourses() {
        List<ScheduledCourseResponseDto> dtos = new ArrayList<>();
        for (ScheduledCourse scheduledCourse : service.getAllScheduledCourses()) {
            dtos.add(new ScheduledCourseResponseDto(scheduledCourse));
        }
        return new ScheduledCourseListDto(dtos);
    }

    @DeleteMapping("/scheduledCourses/{id}")
    public void deleteScheduledCourse(@PathVariable(name="id") int id) {
        service.deleteScheduledCourse(id);
    }

    @DeleteMapping("/scheduledCourses")
    public void deleteAllScheduledCourses() {
        service.deleteAllScheduledCourses();
    }
}