package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.CourseTypeListDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.CourseTypeResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.InstructorResponseDTO; 
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.service.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class CourseTypeController {

    @Autowired
    private CourseTypeService service;

    @PostMapping("/courseTypes")
    public CourseTypeResponseDTO createCourseType(@RequestBody CourseTypeRequestDTO request) {
        // Assuming you have a createCourseType method in your service
        CourseType createdCourseType = service.createCourseType(request.getDescription(),
                request.isApprovedByOwner(), request.getPrice());
        return new CourseTypeResponseDTO(createdCourseType);
    }

    @GetMapping("/courseTypes/{id}")
    public CourseTypeResponseDTO findCourseTypeById(@PathVariable(name="id") int id) {
        CourseType courseType = service.getCourseType(id);
        return new CourseTypeResponseDTO(courseType);
    }

    @GetMapping("/courseTypes")
    public CourseTypeListDTO findAllCourseTypes() {
        List<CourseTypeResponseDTO> dtos = new ArrayList<>();
        for (CourseType courseType : service.getAllCourseTypes()) {
            dtos.add(new CourseTypeResponseDTO(courseType));
        }
        return new CourseTypeListDTO(dtos);
    }

    @DeleteMapping("/courseTypes/{id}")
    public ResponseEntity<String> deleteCourseType(@PathVariable(name="id") int id) {
        service.deleteCourseType(id);
         return ResponseEntity.ok("Course type with ID " + id + " has been deleted.");
    }

    @DeleteMapping("/courseTypes")
    public ResponseEntity<String> deleteAllCourseTypes() {
        service.deleteAllCourseTypes();
        return ResponseEntity.ok("All course types have been deleted.");
    }

    @GetMapping("/courseTypes/price/{price}")
    public CourseTypeListDTO getCourseTypeByPrice(@PathVariable(name = "price") float price) {
        List<CourseTypeResponseDTO> dtos = new ArrayList<>(); // Corrected to DTO
        List<CourseType> courseTypes = service.getCourseTypeByPrice(price);
        for (CourseType courseType : courseTypes) {
            dtos.add(new CourseTypeResponseDTO(courseType));
        }
        return new CourseTypeListDTO(dtos);
    }

    @GetMapping("/courseTypes/approvedByOwner/{isApprovedByOwner}")
    public CourseTypeListDTO getCourseTypeByApprovedByOwner(@PathVariable(name = "isApprovedByOwner") boolean approvedByOwner) {
        List<CourseTypeResponseDTO> dtos = new ArrayList<>(); // Corrected to DTO
        List<CourseType> courseTypes = service.getByApprovedByOwner(approvedByOwner);
        for (CourseType courseType : courseTypes) {
            dtos.add(new CourseTypeResponseDTO(courseType));
        }
        return new CourseTypeListDTO(dtos);
    }

    @PutMapping("/courseTypes/{id}")
    public CourseTypeResponseDTO updateCourseType(@PathVariable(name = "id") int id, @RequestBody CourseTypeRequestDTO request) {
        CourseType updatedCourseType = service.updateCourseType(id, request.getDescription(), request.isApprovedByOwner(), request.getPrice());
        return new CourseTypeResponseDTO(updatedCourseType);
    }

    @PutMapping("/courseTypes/approval/{id}")
    public CourseTypeResponseDTO updateCourseTypeApproval(@PathVariable(name = "id") int id, @RequestParam(name = "approved") boolean approved) {
        CourseType updatedCourseType = service.updateCourseTypeApproval(id, approved);
        return new CourseTypeResponseDTO(updatedCourseType);
    }

    @GetMapping("/instructors/courseType/{id}")
    public InstructorResponseDTO getInstructorsByInstructorSuggestedCourseType(@PathVariable(name = "id") int courseTypeId) {
        CourseType courseType = service.getCourseType(courseTypeId);
        Instructor instructor = service.getInstructorsByInstructorSuggestedCourseType(courseType);
        return new InstructorResponseDTO(instructor);
    }
}
