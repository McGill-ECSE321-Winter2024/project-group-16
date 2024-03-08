package ca.mcgill.ecse321.SportsSchedulePlus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.utils.Helper;

import java.util.List;
import java.util.ArrayList;

@Service
public class CourseTypeService {

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    @Autowired
    private InstructorRepository instructorRepository;


    @Transactional
    public CourseType createCourseType(String description, boolean approvedByOwner, float price) {
        CourseType courseType = new CourseType();
        courseType.setDescription(description);
        courseType.setApprovedByOwner(approvedByOwner);
        courseType.setPrice(price);

        // Validate the course before saving
        validateCourse(courseType);

        courseTypeRepository.save(courseType);
        return courseType;
    }

    @Transactional
    public CourseType updateCourseType(int id, String description, boolean approvedByOwner, float price) {
        CourseType courseType = getCourseType(id);

        // Update the course fields
        courseType.setDescription(description);
        courseType.setApprovedByOwner(approvedByOwner);
        courseType.setPrice(price);

        // Validate the updated course before saving
        validateCourse(courseType);

        // Save the updated course
        return courseTypeRepository.save(courseType);
    }


    @Transactional
    public Instructor getInstructorsByInstructorSuggestedCourseType(CourseType courseType) {
        if (courseType == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "Course type cannot be null.");
        }

        return instructorRepository.findInstructorByInstructorSuggestedCourseTypes(courseType);
    }

    private void validateCourse(CourseType courseType) {
        if (courseType.getDescription() == null || courseType.getDescription().trim().isEmpty()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course description cannot be null or empty.");
        }

        if (courseType.getPrice() <= 0) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course price must be greater than zero.");
        }

        if (courseType.getDescription().length() > 255) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course description cannot exceed 255 characters.");
        }
    }

    @Transactional
    public CourseType getCourseType(Integer id) {
        CourseType courseType = courseTypeRepository.findById(id).orElse(null);
        if(courseType == null){
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no course type with ID " + id + ".");
        }
        return courseType;
    }

    @Transactional
    public List<CourseType> getAllCourseTypes() {
        return Helper.toList(courseTypeRepository.findAll());
    }

    @Transactional
    public void deleteCourseType(Integer id) {
        CourseType courseType = courseTypeRepository.findCourseTypeById(id);
        if(courseType == null){
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no course type with ID " + id + ".");
        }
        List <ScheduledCourse> courses = scheduledCourseRepository.findScheduledCoursesByCourseType(courseType);
        // Delete all courses associated with this course type before deleting the course type
        for (ScheduledCourse course : courses){
            scheduledCourseRepository.delete(course);
        }
        courseTypeRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllCourseTypes() {
        List<CourseType> courseTypes = Helper.toList(courseTypeRepository.findAll());
        if (courseTypes == null || courseTypes.isEmpty()){
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There are no course types.");
        }
        // Delete all courses associated with this course type before deleting the course type
        for (CourseType courseType : courseTypes){
            deleteCourseType(courseType.getId());
        }
    }

    @Transactional
    public List<CourseType> getCourseTypeByPrice(float price) {
        List<CourseType> courseTypes = courseTypeRepository.findByPrice(price);
        if (courseTypes == null) {
            throw new SportsScheduleException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving course types by price: " + price);
        } else if (courseTypes.isEmpty()) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "No course types found with price: " + price);
        }
        return courseTypes;
    }
    
    @Transactional
    public List<CourseType> getByApprovedByOwner(boolean approvedByOwner) {
        List<CourseType> courseTypes = courseTypeRepository.findByApprovedByOwnerTrue();
        if (courseTypes == null) {
            throw new SportsScheduleException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving course types by approvedByOwner: " + approvedByOwner);
        } else if (courseTypes.isEmpty()) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "No course types found with approvedByOwner: " + approvedByOwner);
        }
        return courseTypes;
    }

    @Transactional
    public CourseType approveCourseType(int id) {
        CourseType courseType = getCourseType(id);
        courseType.setApprovedByOwner(true);
        // Save the updated course before returning
        courseTypeRepository.save(courseType);
        return courseType;
    }

    @Transactional
    public CourseType disapproveCourseType(int id) {
        CourseType courseType = getCourseType(id);
        courseType.setApprovedByOwner(false);
        // Save the updated course before returning
        courseTypeRepository.save(courseType);
        return courseType;
    }


  
}
