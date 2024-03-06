package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.CourseTypeListDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.CourseTypeRequestDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.CourseTypeResponseDto;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.service.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class CourseTypeController {

    @Autowired
    private CourseTypeService service;

    @PostMapping("/courseTypes")
    public CourseTypeResponseDto createCourseType(@RequestBody CourseTypeRequestDto request) {
        // Assuming you have a createCourseType method in your service
        CourseType createdCourseType = service.createCourseType(request.getDescription(),
                request.isApprovedByOwner(), request.getPrice());
        return new CourseTypeResponseDto(createdCourseType);
    }

    @GetMapping("/courseTypes/{id}")
    public CourseTypeResponseDto findCourseTypeById(@PathVariable int id) {
        CourseType courseType = service.getCourseType(id);
        return new CourseTypeResponseDto(courseType);
    }

    @GetMapping("/courseTypes")
    public CourseTypeListDto findAllCourseTypes() {
        List<CourseTypeResponseDto> dtos = new ArrayList<>();
        for (CourseType courseType : service.getAllCourseTypes()) {
            dtos.add(new CourseTypeResponseDto(courseType));
        }
        return new CourseTypeListDto(dtos);
    }
    
    @DeleteMapping("/courseTypes/{id}")
    public void deleteCourseType(@PathVariable int id) {
        service.deleteCourseType(id);
    }

    @DeleteMapping("/courseTypes")
    public void deleteAllCourseTypes() {
        service.deleteAllCourseTypes();
    }
}
