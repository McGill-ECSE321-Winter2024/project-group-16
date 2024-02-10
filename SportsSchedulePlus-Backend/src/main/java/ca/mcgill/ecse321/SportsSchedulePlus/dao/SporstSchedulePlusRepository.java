package ca.mcgill.ecse321.sportsscheduleplus.dao;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.sportsscheduleplus.model.Person;
import ca.mcgill.ecse321.sportsscheduleplus.model.Class;

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
	public Class createClass(int id, String name, String description, String type, Class.ApprovalStatus approvalStatus, Class.ClassType classType, Instructor supervisor, Owner owner, Instructor... allInstructors) {
		Class c = new Class(id, name, description, type, approvalStatus, classType, supervisor, owner, allInstructors);
		entityManager.persist(c);
		return c;
	}

	@Transactional
	public Class getClass(int id) {
		Class c = entityManager.find(Class.class, id);
		return c;
	}

	@Transactional
	public List<Class> getClassesBeforeADeadline(Date deadline) {
		TypedQuery<Class> q = entityManager.createQuery("select c from Class c where c.date < :deadline", Class.class);
		q.setParameter("deadline", deadline);
		List<Class> resultList = q.getResultList();
		return resultList;
	}

}
