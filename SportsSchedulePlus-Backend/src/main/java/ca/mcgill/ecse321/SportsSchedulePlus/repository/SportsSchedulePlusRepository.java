package ca.mcgill.ecse321.SportsSchedulePlus.repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SportsSchedulePlusRepository {

	@Autowired
	EntityManager entityManager;

	// TO DO : Implement @Transactional methods

}
