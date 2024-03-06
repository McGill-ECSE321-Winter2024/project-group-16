package ca.mcgill.ecse321.SportsSchedulePlus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;

import java.util.List;
import java.util.ArrayList;

@Service
public class CourseTypeService {

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Transactional
    public CourseType createCourseType(String description, boolean approvedByOwner, float price) {
        CourseType courseType = new CourseType();
        courseType.setDescription(description);
        courseType.setApprovedByOwner(approvedByOwner);
        courseType.setPrice(price);

        courseTypeRepository.save(courseType);
        return courseType;
    }

    @Transactional
    public CourseType getCourseType(Integer id) {
        return courseTypeRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<CourseType> getAllCourseTypes() {
        return toList(courseTypeRepository.findAll());
    }

    private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}

  
}
