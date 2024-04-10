package ca.mcgill.ecse321.SportsSchedulePlus.controller;

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

/**
 * Rest Controller that handles ScheduledCourse
 */
@CrossOrigin(origins = "http://localhost:8087")
@RestController
public class ScheduledCourseController {

    @Autowired
    private ScheduledCourseService scheduledCourseService;
     
    /**
     * Creates a scheduled course with the information in the request body
     * @param request
     * @return ScheduledCourseResponseDTO
     */
    @PostMapping("/scheduledCourses")
    public ScheduledCourseResponseDTO createScheduledCourse(@RequestBody ScheduledCourseRequestDTO request) {
        ScheduledCourse createdScheduledCourse = scheduledCourseService.createScheduledCourse(request.getDate(), request.getStartTime(), request.getEndTime(), request.getLocation(),request.getInstructorId(), request.getCourseType().getId());
        return new ScheduledCourseResponseDTO(createdScheduledCourse);
    }
    /**
     * Updates the scheduled course with the path variable id using the request body information
     * @param id
     * @param request
     * @return ScheduledCourseResponseDTO
     */
    @PutMapping("/scheduledCourses/{id}")
    public ScheduledCourseResponseDTO updateScheduledCourse(@PathVariable(name = "id") int id, @RequestBody ScheduledCourseRequestDTO request) {
        String newLocation = "";
        if (!(request.getLocation() == null)) {
            newLocation = request.getLocation();
        }
        int newCourseTypeId = 0;
        if (!(request.getCourseType() == null)) {
            newCourseTypeId = request.getCourseType().getId();
        }
        int instructorId = 0;
        if (!(request.getInstructorId() == 0)) {
            instructorId = request.getInstructorId();
        }
        ScheduledCourse updatedScheduledCourse = scheduledCourseService.updateScheduledCourse(id, request.getDate(), request.getStartTime(), request.getEndTime(), instructorId, newLocation, newCourseTypeId);
        return new ScheduledCourseResponseDTO(updatedScheduledCourse);
    }
    
    /**
     * Retrieves instructors that supervise the course with the path variable id
     * @param scheduledCourseId
     * @return InstructorListDTO
     */
    @GetMapping("/scheduledCourses/instructors/{id}")
    public InstructorListDTO getInstructorsBySupervisedCourse(@PathVariable(name = "id") int scheduledCourseId) {
        List<InstructorResponseDTO> instructorResponseDTOS = new ArrayList<>();
        List<Instructor> instructors = scheduledCourseService.getInstructorsBySupervisedCourse(scheduledCourseId);
        for (Instructor instructor : instructors) {
            instructorResponseDTOS.add(new InstructorResponseDTO(instructor));
        }
        return new InstructorListDTO(instructorResponseDTOS);
    }
    
    /**
     * Retrieves the scheduled course with the path variable id
     * @param id
     * @return ScheduledCourseResponseDTO
     */
    @GetMapping("/scheduledCourses/course/{id}")
    public ScheduledCourseResponseDTO findScheduledCourseById(@PathVariable(name = "id") int id) {
        ScheduledCourse scheduledCourse = scheduledCourseService.getScheduledCourse(id);
        return new ScheduledCourseResponseDTO(scheduledCourse);
    }
    
    /**
     * Retrieves all scheduled courses
     * @return ScheduledCourseListDTO
     */
    @GetMapping("/scheduledCourses")
    public ScheduledCourseListDTO findAllScheduledCourses() {
        List<ScheduledCourseResponseDTO> scheduledCourseResponseDTOS = new ArrayList<>();
        for (ScheduledCourse scheduledCourse : scheduledCourseService.getAllScheduledCourses()) {
            scheduledCourseResponseDTOS.add(new ScheduledCourseResponseDTO(scheduledCourse));
        }
        return new ScheduledCourseListDTO(scheduledCourseResponseDTOS);
    }
    
    /**
     * Deletes the scheduled course with the path variable id
     * @param id
     * @return String response entity
     */
    @DeleteMapping("/scheduledCourses/{id}")
    public ResponseEntity<String> deleteScheduledCourse(@PathVariable(name = "id") int id) {
        scheduledCourseService.deleteScheduledCourse(id);
        return ResponseEntity.ok("Scheduled course with ID " + id + " has been deleted.");
    }
    
    /**
     * Deletes all scheduled courses
     * @return String response entity
     */
    @DeleteMapping("/scheduledCourses")
    public ResponseEntity<String> deleteAllScheduledCourses() {
        scheduledCourseService.deleteAllScheduledCourses();
        return ResponseEntity.ok("All scheduled courses have been deleted.");
    }
    
    /**
     * Retrieves the scheduled courses for the week, given a start date path variable
     * @param date
     * @return 
     */
    @GetMapping(value = "/scheduledCourses/{date}")
    public ScheduledCourseListDTO getScheduledCoursesForWeekByDate(@PathVariable("date") String date) {
        List<ScheduledCourse> scheduledCourses = scheduledCourseService.getScheduledCoursesByWeek(date);
        List<ScheduledCourseResponseDTO> scheduledCourseDTOs = new ArrayList<>();
        for (ScheduledCourse scheduledCourse : scheduledCourses) {
            scheduledCourseDTOs.add(new ScheduledCourseResponseDTO(scheduledCourse));
        }
        return new ScheduledCourseListDTO(scheduledCourseDTOs);
    }

    /**
     * Retrieves the scheduled courses for the course type with the path variable id
     * @param courseTypeId
     * @return
     */
    @GetMapping(value = "/courseTypes/{courseTypeId}/scheduledCourses")
    public ScheduledCourseListDTO getScheduledCoursesByCourseType(@PathVariable("courseTypeId") int courseTypeId) {
        List<ScheduledCourse> scheduledCourses = scheduledCourseService.getScheduledCoursesByCourseType(courseTypeId);
        List<ScheduledCourseResponseDTO> scheduledCourseDTOs = new ArrayList<>();
        for (ScheduledCourse scheduledCourse : scheduledCourses) {
            scheduledCourseDTOs.add(new ScheduledCourseResponseDTO(scheduledCourse));
        }
        return new ScheduledCourseListDTO(scheduledCourseDTOs);
    }
}