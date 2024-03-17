package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.instructor.InstructorListDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.instructor.InstructorResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseListDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.ScheduledCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ScheduledCourseController {

    @Autowired
    private ScheduledCourseService scheduledCourseService;

    @PostMapping("/scheduledCourses")
    public ScheduledCourseResponseDTO createScheduledCourse(@RequestBody ScheduledCourseRequestDTO request) {
        ScheduledCourse createdScheduledCourse = scheduledCourseService.createScheduledCourse(request.getDate(), request.getStartTime(), request.getEndTime(), request.getLocation(), request.getCourseType().getId());
        return new ScheduledCourseResponseDTO(createdScheduledCourse);
    }

    @PutMapping("/scheduledCourses/{id}")
    public ScheduledCourseResponseDTO updateScheduledCourse(@PathVariable(name = "id") int id, @RequestBody ScheduledCourseRequestDTO request) {
        ScheduledCourse updatedScheduledCourse = scheduledCourseService.updateScheduledCourse(id, request.getDate(), request.getStartTime(), request.getEndTime(), request.getLocation(), request.getCourseType().getId());
        return new ScheduledCourseResponseDTO(updatedScheduledCourse);
    }

    @GetMapping("/scheduledCourses/instructors/{id}")
    public InstructorListDTO getInstructorsBySupervisedCourse(@PathVariable(name = "id") int scheduledCourseId) {
        List<InstructorResponseDTO> instructorResponseDTOS = new ArrayList<>();
        List<Instructor> instructors = scheduledCourseService.getInstructorsBySupervisedCourse(scheduledCourseId);
        for (Instructor instructor : instructors) {
            instructorResponseDTOS.add(new InstructorResponseDTO(instructor));
        }
        return new InstructorListDTO(instructorResponseDTOS);
    }

    @GetMapping("/scheduledCourses/course/{id}")
    public ScheduledCourseResponseDTO findScheduledCourseById(@PathVariable(name = "id") int id) {
        ScheduledCourse scheduledCourse = scheduledCourseService.getScheduledCourse(id);
        return new ScheduledCourseResponseDTO(scheduledCourse);
    }

    @GetMapping("/scheduledCourses")
    public ScheduledCourseListDTO findAllScheduledCourses() {
        List<ScheduledCourseResponseDTO> scheduledCourseResponseDTOS = new ArrayList<>();
        for (ScheduledCourse scheduledCourse : scheduledCourseService.getAllScheduledCourses()) {
            scheduledCourseResponseDTOS.add(new ScheduledCourseResponseDTO(scheduledCourse));
        }
        return new ScheduledCourseListDTO(scheduledCourseResponseDTOS);
    }

    @DeleteMapping("/scheduledCourses/{id}")
    public ResponseEntity<String> deleteScheduledCourse(@PathVariable(name = "id") int id) {
        scheduledCourseService.deleteScheduledCourse(id);
        return ResponseEntity.ok("Scheduled course with ID " + id + " has been deleted.");
    }

    @DeleteMapping("/scheduledCourses")
    public ResponseEntity<String> deleteAllScheduledCourses() {
        scheduledCourseService.deleteAllScheduledCourses();
        return ResponseEntity.ok("All scheduled courses have been deleted.");
    }

    @GetMapping(value = "/scheduledCourses/{date}")
    public List<ScheduledCourseDTO> getScheduledCoursesForWeekByDate(@PathVariable("date") String date) {
        List<ScheduledCourse> scheduledCourses = scheduledCourseService.getScheduledCoursesByWeek(date);
        List<ScheduledCourseDTO> scheduledCourseDTOs = new ArrayList<>();
        for (ScheduledCourse scheduledCourse : scheduledCourses) {
            scheduledCourseDTOs.add(new ScheduledCourseDTO(scheduledCourse));
        }
        return scheduledCourseDTOs;
    }

}