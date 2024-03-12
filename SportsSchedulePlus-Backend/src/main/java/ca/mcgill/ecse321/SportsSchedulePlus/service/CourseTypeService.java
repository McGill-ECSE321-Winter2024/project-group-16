package ca.mcgill.ecse321.SportsSchedulePlus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.utils.Helper;


import java.util.List;

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
        // Validate the course before saving
        validateCourseType(description,price,true);
        CourseType courseType = new CourseType();

        courseType.setDescription(description);
        courseType.setApprovedByOwner(approvedByOwner);
        courseType.setPrice(price);


        courseTypeRepository.save(courseType);
        return courseType;
    }

    @Transactional
    public CourseType updateCourseType(int id, String description, boolean approvedByOwner, float price) {
        CourseType courseType = getCourseType(id);
       
        boolean newDescription = ! courseType.getDescription().equals(description);

        // Validate the updated course before saving

        validateCourseType(description,price,newDescription);
        // Update the course fields
        courseType.setDescription(description);
        courseType.setApprovedByOwner(approvedByOwner);
        courseType.setPrice(price);

        // Save the updated course
        courseTypeRepository.save(courseType);
        return courseType;
    }


    @Transactional
    public Instructor getInstructorsByInstructorSuggestedCourseType(CourseType courseType) {
        if (courseType == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "Course type cannot be null.");
        }

        return instructorRepository.findInstructorByInstructorSuggestedCourseTypes(courseType);
    }

    private void validateCourseType(String description, float price, boolean newDescription) {
        if (description == null || description.trim().isEmpty()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course description cannot be null or empty.");
        }

        if (price <= 0) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course price must be greater than zero.");
        }

        if (newDescription){
            if (courseTypeRepository.findCourseTypeByDescription(description) != null){
                throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course description must be unique");
            }
        }

         // Check if the description contains at least one letter
        if (!description.matches(".*[a-zA-Z].*")) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Description must contain letters.");
        }

        if (description.length() > 255) {
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
        List<CourseType> courseTypes;
        if(approvedByOwner){
             courseTypes = courseTypeRepository.findByApprovedByOwnerTrue();
        }
        else{
            courseTypes = courseTypeRepository.findByApprovedByOwnerFalse();
        }
        if (courseTypes == null) {
            throw new SportsScheduleException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving course types by approvedByOwner: " + approvedByOwner);
        } else if (courseTypes.isEmpty()) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "No course types found with approvedByOwner: " + approvedByOwner);
        }
        return courseTypes;
    }

    @Transactional
    public CourseType updateCourseTypeApproval(int id, boolean approved) {
        CourseType courseType = getCourseType(id);
        courseType.setApprovedByOwner(approved);
        // Save the updated course before returning
        courseTypeRepository.save(courseType);
        return courseType;
    }

  
}
