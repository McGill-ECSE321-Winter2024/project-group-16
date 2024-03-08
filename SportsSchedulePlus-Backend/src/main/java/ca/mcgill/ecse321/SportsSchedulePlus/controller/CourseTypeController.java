package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.CourseTypeListDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.CourseTypeRequestDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.CourseTypeResponseDto;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.InstructorResponseDto;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
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
    public CourseTypeResponseDto findCourseTypeById(@PathVariable(name="id") int id) {
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
    public void deleteCourseType(@PathVariable(name="id") int id) {
        service.deleteCourseType(id);
    }

    @DeleteMapping("/courseTypes")
    public void deleteAllCourseTypes() {
        service.deleteAllCourseTypes();
    }

    @GetMapping("/courseTypes/price/{price}")
    public CourseTypeListDto getCourseTypeByPrice(@PathVariable(name = "price") float price) {
        List<CourseTypeResponseDto> dtos = new ArrayList<>();
        List<CourseType> courseTypes = service.getCourseTypeByPrice(price);
        for (CourseType courseType : courseTypes) {
            dtos.add(new CourseTypeResponseDto(courseType));
        }
        return new CourseTypeListDto(dtos);
    }

    @GetMapping("/courseTypes/approvedByOwner/{isApprovedByOwner}")
    public CourseTypeListDto getCourseTypeByApprovedByOwner(@PathVariable(name = "isApprovedByOwner") boolean approvedByOwner) {
        List<CourseTypeResponseDto> dtos = new ArrayList<>();
        List<CourseType> courseTypes = service.getByApprovedByOwner(approvedByOwner);
        for (CourseType courseType : courseTypes) {
            dtos.add(new CourseTypeResponseDto(courseType));
        }
        return new CourseTypeListDto(dtos);
    }

    @PutMapping("/courseTypes/{id}")
    public CourseTypeResponseDto updateCourseType(@PathVariable(name = "id") int id, @RequestBody CourseTypeRequestDto request) {
        CourseType updatedCourseType = service.updateCourseType(id, request.getDescription(), request.isApprovedByOwner(), request.getPrice());
        return new CourseTypeResponseDto(updatedCourseType);
    }

    @GetMapping("/instructors/courseType/{id}")
    public InstructorResponseDto getInstructorsByInstructorSuggestedCourseType(@PathVariable(name = "id") int courseTypeId) {
        CourseType courseType = service.getCourseType(courseTypeId);
        Instructor instructor = service.getInstructorsByInstructorSuggestedCourseType(courseType);
        
        return new InstructorResponseDto(instructor);
    }



}
