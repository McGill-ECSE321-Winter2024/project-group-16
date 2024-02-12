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
import ca.mcgill.ecse321.SportsSchedulePlus.model.Person;
import ca.mcgill.ecse321.SportsSchedulePlus.model.PersonRole;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
@Repository
public class SportsSchedulePlusRepository {

	@Autowired
	EntityManager entityManager;


	@Transactional
	public Person createPerson(String name,String email,String password,PersonRole personRole) {
		Person person = new Person(name, email, password, personRole);
		person.setName(name);
		person.setEmail(email);
		person.setPassword(password);
		person.setPersonRole(personRole);
		entityManager.persist(person);
		return person;
	}

	@Transactional
	public Person getPerson(String name) {
		Person person = entityManager.find(Person.class, name);
		return person;
	}

	@Transactional
	public ScheduledCourse createScheduledClass(int id,String name, Date date, Time startTime, Time endTime,String location, CourseType courseType) {
		ScheduledCourse course = new ScheduledCourse(id,date, startTime, endTime, location, courseType);
		course.setId(id);
		course.setDate(date);
		course.setStartTime(startTime);
		course.setEndTime(endTime);
		course.setLocation(location);
		entityManager.persist(course);
		return course;
	}

	@Transactional
	public ScheduledCourse getScheduledCourse(int id) {
		ScheduledCourse course  = entityManager.find(ScheduledCourse.class, id);
		return course;
	}
	@Transactional
	public List<ScheduledCourse> getEventsBeforeADeadline(Date deadline) {
		TypedQuery<ScheduledCourse> query = entityManager.createQuery("select c from ScheduledCourse c where c.date < :deadline",ScheduledCourse.class);
		query.setParameter("deadline", deadline);
		List<ScheduledCourse> resultList = query.getResultList();
		return resultList;
	}

}
