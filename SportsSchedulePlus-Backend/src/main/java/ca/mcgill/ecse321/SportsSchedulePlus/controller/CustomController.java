package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import ca.mcgill.ecse321.SportsSchedulePlus.service.userservice.CustomService;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.user.instructor.InstructorResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.scheduledcourse.ScheduledCourseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;

/**
 * this file contains most of the custom actions need to complete certain use cases
 * @author Vladimir Venkov
 * @author Jessie Kurtz
 * @author Dania Bouhmidi
 */
@CrossOrigin(origins = "*")
@RestController
public class CustomController {

    @Autowired
    private CustomService customService;

    /*
     * customer applies to become instructor
     */
    @PutMapping(value = {"/customers/{customerId}/apply"})
    public void applyForInstructor(@PathVariable("customerId") int customerId) {
        customService.applyForInstructor(customerId);
    }

    /*
     * customer is approved to become instructor
     */
    @PutMapping(value = {"/customers/{customerId}/approve"})
    public InstructorResponseDTO approveCustomer(@PathVariable("customerId") int customerId) {
        return new InstructorResponseDTO(customService.approveCustomer(customerId));
    }

    /*
     * customer is rejected to become instructor
     */
    @PutMapping(value = {"/customers/{customerId}/reject"})
    public void rejectCustomer(@PathVariable("customerId") int customerId) {
        customService.rejectCustomer(customerId);
    }

    /*
     * get all scheduled courses for a week
     */
    @GetMapping(value = "/scheduledCourses/{date}")
    public List<ScheduledCourseDTO> getScheduledCoursesForWeekByDate(@PathVariable("date") String date) {
        LocalDate inputDate = LocalDate.parse(date);
        LocalDate mondayLocalDate = inputDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sundayLocalDate = inputDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        Date monday = java.sql.Date.valueOf(mondayLocalDate);
        Date sunday = java.sql.Date.valueOf(sundayLocalDate);
        List<ScheduledCourse> scheduledCourses = customService.getScheduledCoursesByWeek(monday, sunday);
        List<ScheduledCourseDTO> scheduledCourseDTOs = new ArrayList<>(); // Corrected to DTO
        for (ScheduledCourse scheduledCourse : scheduledCourses) {
            scheduledCourseDTOs.add(new ScheduledCourseDTO(scheduledCourse));
        }
        return scheduledCourseDTOs;
    }
}