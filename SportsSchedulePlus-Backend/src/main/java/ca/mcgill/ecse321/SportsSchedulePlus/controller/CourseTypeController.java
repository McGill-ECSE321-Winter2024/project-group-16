package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeListDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype.CourseTypeResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.instructor.InstructorResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Rest Controller that handles CRUD on course types
 */
@CrossOrigin(origins = "http://localhost:8087")
@RestController
public class CourseTypeController {

    @Autowired
    private CourseTypeService courseTypeService;

    /**
     * Creates a course type using information in the request DTO
     * @param request
     * @return CourseTypeResponseDTO
     */
    @PostMapping("/courseTypes")
    public CourseTypeResponseDTO createCourseType(@RequestBody CourseTypeRequestDTO request) {
        CourseType createdCourseType = courseTypeService.createCourseType(request.getDescription(), request.isApprovedByOwner(), request.getPrice());
        return new CourseTypeResponseDTO(createdCourseType);
    }
    
    /**
     * Retrieves a course type by the input id
     * @param id
     * @return CourseTypeResponseDTO
     */
    @GetMapping("/courseTypes/{id}")
    public CourseTypeResponseDTO findCourseTypeById(@PathVariable(name = "id") int id) {
        CourseType courseType = courseTypeService.getCourseType(id);
        return new CourseTypeResponseDTO(courseType);
    }

    /**
     * Retrieves all course types
     * @return CourseTypeListDTO
     */
    @GetMapping("/courseTypes")
    public CourseTypeListDTO findAllCourseTypes() {
        List<CourseTypeResponseDTO> courseTypeResponseDTOS = new ArrayList<>();
        for (CourseType courseType : courseTypeService.getAllCourseTypes()) {
            courseTypeResponseDTOS.add(new CourseTypeResponseDTO(courseType));
        }
        return new CourseTypeListDTO(courseTypeResponseDTOS);
    }
    
    /**
     * Deletes the course type with the input id
     * @param id
     * @return String response entity
     */
    @DeleteMapping("/courseTypes/{id}")
    public ResponseEntity<String> deleteCourseType(@PathVariable(name = "id") int id) {
        courseTypeService.deleteCourseType(id);
        return ResponseEntity.ok("Course type with ID " + id + " has been deleted.");
    }
    
    /**
     * Deletes all course types
     * @return String response entity
     */
    @DeleteMapping("/courseTypes")
    public ResponseEntity<String> deleteAllCourseTypes() {
        courseTypeService.deleteAllCourseTypes();
        return ResponseEntity.ok("All course types have been deleted.");
    }
    
    /**
     * Retrieves course types by the input price
     * @param price
     * @return CourseTypeListDTO 
     */
    @GetMapping("/courseTypes/price/{price}")
    public CourseTypeListDTO getCourseTypeByPrice(@PathVariable(name = "price") float price) {
        List<CourseTypeResponseDTO> courseTypeResponseDTOS = new ArrayList<>();
        List<CourseType> courseTypes = courseTypeService.getCourseTypeByPrice(price);
        for (CourseType courseType : courseTypes) {
            courseTypeResponseDTOS.add(new CourseTypeResponseDTO(courseType));
        }
        return new CourseTypeListDTO(courseTypeResponseDTOS);
    }
    
    /**
     * Retrieves course types by the approvedByOwner boolean (true or false)
     * @param approvedByOwner
     * @return CourseTypeListDTO
     */
    @GetMapping("/courseTypes/approvedByOwner/{isApprovedByOwner}")
    public CourseTypeListDTO getCourseTypeByApprovedByOwner(@PathVariable(name = "isApprovedByOwner") String approvedByOwner) {
        List<CourseTypeResponseDTO> courseTypeResponseDTOS = new ArrayList<>();
        List<CourseType> courseTypes = courseTypeService.getByApprovedByOwner(approvedByOwner);
        for (CourseType courseType : courseTypes) {
            courseTypeResponseDTOS.add(new CourseTypeResponseDTO(courseType));
        }
        return new CourseTypeListDTO(courseTypeResponseDTOS);
    }
    
    /**
     * Updates the course type with the path variable id using the information in the request body
     * @param id
     * @param request
     * @return CourseTypeResponseDTO
     */
    @PutMapping("/courseTypes/{id}")
    public CourseTypeResponseDTO updateCourseType(@PathVariable(name = "id") int id, @RequestBody CourseTypeRequestDTO request) {
        CourseType updatedCourseType = courseTypeService.updateCourseType(id, request.getDescription(), request.isApprovedByOwner(), request.getPrice());
        return new CourseTypeResponseDTO(updatedCourseType);
    }
    
    /**
     * Toggles the approval of the course type with the path variable id 
     * @return CourseTypeResponseDTO
     */
    @PutMapping("/courseTypes/toggleApproval/{id}")
    public CourseTypeResponseDTO updateCourseTypeApproval(@PathVariable(name = "id") int id) {
        CourseType updatedCourseType = courseTypeService.toggleCourseTypeApproval(id);
        return new CourseTypeResponseDTO(updatedCourseType);
    }
    
    /**
     * Retrieves an instructor by their suggested course type
     * @param courseTypeId
     * @return InstructorResponseDTO
     */
    @GetMapping("/instructors/courseType/{id}")
    public InstructorResponseDTO getInstructorsByInstructorSuggestedCourseType(@PathVariable(name = "id") int courseTypeId) {
        CourseType courseType = courseTypeService.getCourseType(courseTypeId);
        Instructor instructor = courseTypeService.getInstructorsByInstructorSuggestedCourseType(courseType);
        return new InstructorResponseDTO(instructor);
    }
}
