package ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import ca.mcgill.ecse321.SportsSchedulePlus.model.State;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.OwnerRepository;
import ca.mcgill.ecse321.utils.Helper;

@Service
public class CourseTypeService {

    @Autowired
    private CourseTypeRepository courseTypeRepository;


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
    public CourseType createCourseType(String name, String description, String image, boolean approvedByOwner, float price) {
        // Validate the course before saving
        Helper.validateCourseType(courseTypeRepository, name, description, image, price, true);
        CourseType courseType = new CourseType();
        courseType.setName(name);
        courseType.setDescription(description);
        courseType.setImage(image);
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
    public CourseType updateCourseType(int id, String name, String description, String image, boolean approvedByOwner, float price) {
        CourseType courseType = getCourseType(id);
        boolean newName = !courseType.getName().equals(name);
        Helper.validateCourseType(courseTypeRepository, name, description, image, price, newName);
        courseType.setName(name);
        courseType.setDescription(description);
        courseType.setImage(image);
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
        Optional<CourseType> optionalCourseType = courseTypeRepository.findById(id);
         if (!optionalCourseType.isPresent() ) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no course type with ID " + id + ".");
        } 
        Instructor instructor = instructorRepository.findInstructorByInstructorSuggestedCourseTypes(optionalCourseType.get());
        if(instructor != null){
            instructor.removeInstructorSuggestedCourseType(optionalCourseType.get());
            instructorRepository.save(instructor);
        }

        Owner owner =  ownerRepository.findOwnerByApprovedCourses(optionalCourseType.get());
        if(owner != null){
            owner.removeApprovedCourse(optionalCourseType.get());
            ownerRepository.save(owner);
        }
        owner = ownerRepository.findOwnerByOwnerSuggestedCourses(optionalCourseType.get());
        if(owner != null){
            owner.removeOwnerSuggestedCourse(optionalCourseType.get());
            ownerRepository.save(owner);
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
        courseTypeRepository.deleteAll();
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
     * @param b
     * @return list of CourseType with the given approval status
     */
    @Transactional
    public List<CourseType> getByApprovedByOwner(String b) {
        if (!Objects.equals(b, "true") && !Objects.equals(b, "false")) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Endpoint can only contain true or false strings.");
        }
        boolean approvedBoolean = Boolean.parseBoolean(b);
        List<CourseType> courseTypes;
        if (approvedBoolean) {
            courseTypes = courseTypeRepository.findByApprovedByOwnerTrue();
        } else {
            courseTypes = courseTypeRepository.findByApprovedByOwnerFalse();
        }
        if (courseTypes == null) {
            throw new SportsScheduleException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving course types by approvedByOwner: " + b);
        } else if (courseTypes.isEmpty()) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "No course types found with approvedByOwner: " + b);
        }
        return courseTypes;
    }

    /**
    * Approves the course type with the given ID
    * @param id The ID of the course type to be approved
    * @return The approved course type
    */
   @Transactional
   public CourseType approveCourseType(int id) {
       CourseType courseType = courseTypeRepository.findById(id).orElse(null);

       if (courseType == null) {
           throw new SportsScheduleException(HttpStatus.NOT_FOUND, "No course type with specified ID exists in the system");
       }

       courseType.setApprovedByOwner(true);
       courseType.setState(State.APPROVED);

       Owner owner = Helper.toList(ownerRepository.findAll()).get(0); // Assuming there is only one owner
       owner.addApprovedCourse(courseType);

       courseTypeRepository.save(courseType);
       ownerRepository.save(owner);

       return courseType;
   }

   /**
    * Rejects the course type with the given ID
    * @param id The ID of the course type to be rejected
    * @return The rejected course type
    */
   @Transactional
   public CourseType rejectCourseType(int id) {
       CourseType courseType = courseTypeRepository.findById(id).orElse(null);

       if (courseType == null) {
           throw new SportsScheduleException(HttpStatus.NOT_FOUND, "No course type with specified ID exists in the system");
       }

       courseType.setApprovedByOwner(false);
       courseType.setState(State.REJECTED);

       courseTypeRepository.save(courseType);
       return courseType;
   }
}
