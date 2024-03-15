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

@CrossOrigin(origins = "*")
@RestController
public class CourseTypeController {

    @Autowired
    private CourseTypeService courseTypeService;

    @GetMapping("/courseTypes/{id}")
    public CourseTypeResponseDTO findCourseTypeById(@PathVariable(name = "id") int id) {
        CourseType courseType = courseTypeService.getCourseType(id);
        return new CourseTypeResponseDTO(courseType);
    }

    @GetMapping("/courseTypes")
    public CourseTypeListDTO findAllCourseTypes() {
        List<CourseTypeResponseDTO> courseTypeResponseDTOS = new ArrayList<>();
        for (CourseType courseType : courseTypeService.getAllCourseTypes()) {
            courseTypeResponseDTOS.add(new CourseTypeResponseDTO(courseType));
        }
        return new CourseTypeListDTO(courseTypeResponseDTOS);
    }

    @DeleteMapping("/courseTypes/{id}")
    public ResponseEntity<String> deleteCourseType(@PathVariable(name = "id") int id) {
        courseTypeService.deleteCourseType(id);
        return ResponseEntity.ok("Course type with ID " + id + " has been deleted.");
    }

    @DeleteMapping("/courseTypes")
    public ResponseEntity<String> deleteAllCourseTypes() {
        courseTypeService.deleteAllCourseTypes();
        return ResponseEntity.ok("All course types have been deleted.");
    }

    @GetMapping("/courseTypes/price/{price}")
    public CourseTypeListDTO getCourseTypeByPrice(@PathVariable(name = "price") float price) {
        List<CourseTypeResponseDTO> courseTypeResponseDTOS = new ArrayList<>();
        List<CourseType> courseTypes = courseTypeService.getCourseTypeByPrice(price);
        for (CourseType courseType : courseTypes) {
            courseTypeResponseDTOS.add(new CourseTypeResponseDTO(courseType));
        }
        return new CourseTypeListDTO(courseTypeResponseDTOS);
    }

    @GetMapping("/courseTypes/approvedByOwner/{isApprovedByOwner}")
    public CourseTypeListDTO getCourseTypeByApprovedByOwner(@PathVariable(name = "isApprovedByOwner") boolean approvedByOwner) {
        List<CourseTypeResponseDTO> courseTypeResponseDTOS = new ArrayList<>();
        List<CourseType> courseTypes = courseTypeService.getByApprovedByOwner(approvedByOwner);
        for (CourseType courseType : courseTypes) {
            courseTypeResponseDTOS.add(new CourseTypeResponseDTO(courseType));
        }
        return new CourseTypeListDTO(courseTypeResponseDTOS);
    }

    @PutMapping("/courseTypes/{id}")
    public CourseTypeResponseDTO updateCourseType(@PathVariable(name = "id") int id, @RequestBody CourseTypeRequestDTO request) {
        CourseType updatedCourseType = courseTypeService.updateCourseType(id, request.getDescription(), request.isApprovedByOwner(), request.getPrice());
        return new CourseTypeResponseDTO(updatedCourseType);
    }

    @PutMapping("/courseTypes/approval/{id}")
    public CourseTypeResponseDTO updateCourseTypeApproval(@PathVariable(name = "id") int id, @RequestBody boolean approved) {
        CourseType updatedCourseType = courseTypeService.updateCourseTypeApproval(id, approved);
        return new CourseTypeResponseDTO(updatedCourseType);
    }

    @GetMapping("/instructors/courseType/{id}")
    public InstructorResponseDTO getInstructorsByInstructorSuggestedCourseType(@PathVariable(name = "id") int courseTypeId) {
        CourseType courseType = courseTypeService.getCourseType(courseTypeId);
        Instructor instructor = courseTypeService.getInstructorsByInstructorSuggestedCourseType(courseType);
        return new InstructorResponseDTO(instructor);
    }
}
