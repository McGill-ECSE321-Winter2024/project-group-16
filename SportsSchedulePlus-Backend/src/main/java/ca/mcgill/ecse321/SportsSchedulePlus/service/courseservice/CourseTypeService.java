package ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice;

import ca.mcgill.ecse321.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsSchedulePlusException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;


import java.util.List;
import java.util.Objects;

@Service
public class CourseTypeService {

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    /**
     * Creates a new course type with the given description, approval status, and price.
     * @param description
     * @param approvedByOwner
     * @param price
     * @return the new CourseType
     */
    @Transactional
    public CourseType createCourseType(String description, boolean approvedByOwner, float price) {
        // Validate the course before saving
        validateCourseType(description, price, true);
        CourseType courseType = new CourseType();

        courseType.setDescription(description);
        courseType.setApprovedByOwner(approvedByOwner);
        courseType.setPrice(price);
        courseTypeRepository.save(courseType);
        return courseType;
    }

    /**
     * Updates the course type with the given ID, changing its description, approval status, and price.
     * @param id
     * @param description
     * @param approvedByOwner
     * @param price
     * @return the updated CourseType
     */
    @Transactional
    public CourseType updateCourseType(int id, String description, boolean approvedByOwner, float price) {
        CourseType courseType = getCourseType(id);
        boolean newDescription = !courseType.getDescription().equals(description);
        validateCourseType(description, price, newDescription);
        courseType.setDescription(description);
        courseType.setApprovedByOwner(approvedByOwner);
        courseType.setPrice(price);
        courseTypeRepository.save(courseType);
        return courseType;
    }

    /**
     * @param courseType
     * @return the instructor who suggested the courseType
     */
    @Transactional
    public Instructor getInstructorsByInstructorSuggestedCourseType(CourseType courseType) {
        if (courseType == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "Course type not found.");
        }
        Instructor instructor = instructorRepository.findInstructorByInstructorSuggestedCourseTypes(courseType);
        if (instructor == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "Instructor for course type with ID " + courseType.getId() + " not found.");
        }
        return instructor;
    }

    /**
     * Validates the course type before saving or updating it.
     * @param description
     * @param price
     * @param newDescription
     */
    private void validateCourseType(String description, float price, boolean newDescription) {
        if (description == null || description.trim().isEmpty()) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course description cannot be null or empty.");
        }
        if (price <= 0) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course price must be greater than zero.");
        }
        if (newDescription) {
            if (courseTypeRepository.findCourseTypeByDescription(description) != null) {
                throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course description must be unique");
            }
        }
        if (!description.matches(".*[a-zA-Z].*")) {
            throw new SportsSchedulePlusException(HttpStatus.BAD_REQUEST, "Description must contain letters.");
        }
        if (description.length() > 60) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Course description cannot exceed 60 characters.");
        }
    }

    /**
     * @param id
     * @return CourseType with the given ID
     */
    @Transactional
    public CourseType getCourseType(Integer id) {
        CourseType courseType = courseTypeRepository.findById(id).orElse(null);
        if (courseType == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no course type with ID " + id + ".");
        }
        return courseType;
    }

    /**
     * @return all course types in the system
     */
    @Transactional
    public List<CourseType> getAllCourseTypes() {
        return Helper.toList(courseTypeRepository.findAll());
    }

    /**
     * @return all course types that have been approved by the owner
     */
    @Transactional
    public List<CourseType> getAllApprovedCourseTypes() {
        return courseTypeRepository.findByApprovedByOwnerTrue();
    }

    /**
     * deletes the course type with the given ID
     * @param id
     */
    @Transactional
    public void deleteCourseType(Integer id) {
        CourseType courseType = courseTypeRepository.findById(id).orElse(null);
        if (courseType == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no course type with ID " + id + ".");
        }
        List<ScheduledCourse> courses = scheduledCourseRepository.findScheduledCoursesByCourseType(courseType);
        for (ScheduledCourse course : courses) {
            scheduledCourseRepository.delete(course);
        }
        courseTypeRepository.deleteById(id);
    }

    /**
     * Deletes all course types in the system
     */
    @Transactional
    public void deleteAllCourseTypes() {
        List<CourseType> courseTypes = Helper.toList(courseTypeRepository.findAll());
        if (courseTypes == null || courseTypes.isEmpty()) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There are no course types.");
        }
        for (CourseType courseType : courseTypes) {
            deleteCourseType(courseType.getId());
        }
    }

    /**
     * @param price
     * @return all courseType with the given price
     */
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

    /**
     * @param approvedByOwner
     * @return list of CourseType with the given approval status
     */
    @Transactional
    public List<CourseType> getByApprovedByOwner(String approvedByOwner) {
        if (!Objects.equals(approvedByOwner, "true") && !Objects.equals(approvedByOwner, "false")) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Endpoint can only contain true or false strings.");
        }
        boolean approvedBoolean = Boolean.parseBoolean(approvedByOwner);
        List<CourseType> courseTypes;
        if (approvedBoolean) {
            courseTypes = courseTypeRepository.findByApprovedByOwnerTrue();
        } else {
            courseTypes = courseTypeRepository.findByApprovedByOwnerFalse();
        }
        if (courseTypes == null) {
            throw new SportsScheduleException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving course types by approvedByOwner: " + approvedByOwner);
        } else if (courseTypes.isEmpty()) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "No course types found with approvedByOwner: " + approvedByOwner);
        }
        return courseTypes;
    }

    /**
     * toggles the approval status of the course type with the given ID
     * @param id
     * @return the course type with the given ID
     */
    @Transactional
    public CourseType toggleCourseTypeApproval(int id) {
        CourseType courseType = courseTypeRepository.findById(id).orElse(null);

        if (courseType == null){
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "No course type with specified ID exists in the system");
        }

        boolean approvalStatus = courseType.getApprovedByOwner();
        courseType.setApprovedByOwner(!approvalStatus);
        Owner owner = Helper.toList(ownerRepository.findAll()).get(0);

        if (!approvalStatus) {
            owner.addApprovedCourse(courseType);
        }
        else {
            owner.removeApprovedCourse(courseType);
        }

        courseTypeRepository.save(courseType);
        ownerRepository.save(owner);
        return courseType;
    }
}
