package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.ScheduledCourseListDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.ScheduledCourseRequestDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.ScheduledCourseResponseDto;
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

    @GetMapping("/scheduledCourses/{id}")
    public ScheduledCourseResponseDto findScheduledCourseById(@PathVariable int id) {
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
}
