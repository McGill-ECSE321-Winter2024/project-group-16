package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Owner;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;



@Repository
public class SportsSchedulePlusRepository {

	@Autowired
	EntityManager entityManager;

	@Transactional
	public Person createPerson(String name) {
		Person p = new Person();
		p.setName(name);
		entityManager.persist(p);
		return p;
	}

	@Transactional
	public Person getPerson(String name) {
		Person p = entityManager.find(Person.class, name);
		return p;
	}

	@Transactional
    public ScheduledCourse createClass(int id, Date date, Time startTime, Time endTime, String location, CourseType courseType) {
        ScheduledCourse scheduledCourse = new ScheduledCourse(id, date, startTime, endTime, location, courseType);
        entityManager.persist(scheduledCourse);
        return scheduledCourse;
    }

	@Transactional
	public ScheduledCourse getClass(int id) {
		ScheduledCourse c = entityManager.find(ScheduledCourse.class, id);
		return c;
	}

	@Transactional
	public List<ScheduledCourse> getClassesBeforeADeadline(Date deadline) {
		TypedQuery<ScheduledCourse> q = entityManager.createQuery("select c from ScheduledCourse c where c.date < :deadline", ScheduledCourse.class);
		q.setParameter("deadline", deadline);
		List<ScheduledCourse> resultList = q.getResultList();
		return resultList;
	}

}
