package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.instructor.InstructorListDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.instructor.InstructorResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseListDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.service.ScheduledCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ScheduledCourseController {

    @Autowired
    private ScheduledCourseService service;

    @PostMapping("/scheduledCourses")
    public ScheduledCourseResponseDTO createScheduledCourse(@RequestBody ScheduledCourseRequestDTO request) {
        // Assuming you have a createScheduledCourse method in your service
        ScheduledCourse createdScheduledCourse = service.createScheduledCourse(request.getId(),
                request.getDate(), request.getStartTime(), request.getEndTime(), request.getLocation(),
                request.getCourseType().getId());
        return new ScheduledCourseResponseDTO(createdScheduledCourse);
    }

    @PutMapping("/scheduledCourses/{id}")
    public ScheduledCourseResponseDTO updateScheduledCourse(@PathVariable(name = "id") int id,
                                                           @RequestBody ScheduledCourseRequestDTO request) {
        ScheduledCourse updatedScheduledCourse = service.updateScheduledCourse(
                id,
                request.getDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getLocation(),
                request.getCourseType().getId()
        );
        return new ScheduledCourseResponseDTO(updatedScheduledCourse);
    }

    @GetMapping("/scheduledCourses/instructors/{id}")
    public InstructorListDTO getInstructorsBySupervisedCourse(@PathVariable(name = "id") int scheduledCourseId) {
        List<InstructorResponseDTO> instructorDtos = new ArrayList<>();
        List<Instructor> instructors = service.getInstructorsBySupervisedCourse(scheduledCourseId);

        
        for (Instructor instructor : instructors) {
            instructorDtos.add(new InstructorResponseDTO(instructor));
        }

        return new InstructorListDTO(instructorDtos);
    }

    @GetMapping("/scheduledCourses/{id}")
    public ScheduledCourseResponseDTO findScheduledCourseById(@PathVariable(name="id") int id) {
        ScheduledCourse scheduledCourse = service.getScheduledCourse(id);
        return new ScheduledCourseResponseDTO(scheduledCourse);
    }

    @GetMapping("/scheduledCourses")
    public ScheduledCourseListDTO findAllScheduledCourses() {
        List<ScheduledCourseResponseDTO> dtos = new ArrayList<>();
        for (ScheduledCourse scheduledCourse : service.getAllScheduledCourses()) {
            dtos.add(new ScheduledCourseResponseDTO(scheduledCourse));
        }
        return new ScheduledCourseListDTO(dtos);
    }

    @DeleteMapping("/scheduledCourses/{id}")
    public ResponseEntity<String> deleteScheduledCourse(@PathVariable(name="id") int id) {
        service.deleteScheduledCourse(id);
        return ResponseEntity.ok("Scheduled course with ID " + id + " has been deleted.");
    }

    @DeleteMapping("/scheduledCourses")
    public ResponseEntity<String> deleteAllScheduledCourses() {
        service.deleteAllScheduledCourses();
        return ResponseEntity.ok("All scheduled courses have been deleted.");
    }

}