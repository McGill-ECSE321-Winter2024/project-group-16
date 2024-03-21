package ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice;

import ca.mcgill.ecse321.SportsSchedulePlus.beans.MailConfigBean;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.*;
import ca.mcgill.ecse321.SportsSchedulePlus.service.dailyscheduleservice.DailyScheduleService;
import ca.mcgill.ecse321.SportsSchedulePlus.service.mailerservice.Mailer;
import ca.mcgill.ecse321.utils.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


@Service
public class ScheduledCourseService {

    @Autowired
    private ScheduledCourseRepository scheduledCourseRepository;
    @Autowired
    private CourseTypeRepository courseTypeRepository;
    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private DailyScheduleService dailyScheduleService;

    private Mailer mailer;

    /**
     * @param date
     * @param startTime
     * @param endTime
     * @param location
     * @param courseTypeId
     * @return the new ScheduledCourse
     */
    @Transactional
    public ScheduledCourse createScheduledCourse(String date, String startTime, String endTime, String location, int courseTypeId) {
        LocalDate localDate = LocalDate.parse(date);
        Date parsedDate = Date.valueOf(localDate);

        Time parsedStartTime = Time.valueOf(startTime);
        Time parsedEndTime = Time.valueOf(endTime);

        // create the scheduled course
        ScheduledCourse scheduledCourse = new ScheduledCourse();
        scheduledCourse.setDate(parsedDate);
        scheduledCourse.setStartTime(parsedStartTime);
        scheduledCourse.setEndTime(parsedEndTime);
        scheduledCourse.setLocation(location);
        scheduledCourse.setCourseType(courseTypeRepository.findById(courseTypeId).orElse(null));

        validateScheduledCourse(scheduledCourse);

        scheduledCourseRepository.save(scheduledCourse);
        return scheduledCourse;
    }

    /**
     * Helper method that validates the given scheduled course.
     * @param scheduledCourse
     */
    private void validateScheduledCourse(ScheduledCourse scheduledCourse) {
        if (scheduledCourse.getCourseType() == null) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Scheduled course must have a CourseType.");
        }
        if (scheduledCourse.getDate() == null) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Scheduled course date cannot be null.");
        }
        if (scheduledCourse.getStartTime() == null) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Scheduled course start time cannot be null.");
        }
        if (scheduledCourse.getEndTime() == null) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Scheduled course end time cannot be null.");
        }
        if (scheduledCourse.getEndTime().before(scheduledCourse.getStartTime())) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "End time must be after start time.");
        }
        long durationInMillis = scheduledCourse.getEndTime().getTime()- scheduledCourse.getStartTime().getTime();
        long durationInMinutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis);
        
        if (durationInMinutes < 30) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Duration must be at least 30 minutes.");
        }
        // Check for valid course hours
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(scheduledCourse.getDate());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int mappedDayOfWeek = (dayOfWeek + 6) % 7;

        DailySchedule dailySchedule = dailyScheduleService.getDailyScheduleById(mappedDayOfWeek);

        if (scheduledCourse.getStartTime().before(dailySchedule.getOpeningTime()) || scheduledCourse.getEndTime().after(dailySchedule.getClosingTime())) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Scheduled course must respect opening and closing hours.");
        }
    }

    /**
     * Information of the scheduled course to be updated is updated
     * @param id
     * @param date
     * @param startTime
     * @param endTime
     * @param location
     * @param courseTypeId
     * @return the updated ScheduledCourse
     */
    @Transactional
    public ScheduledCourse updateScheduledCourse(int id, String date, String startTime, String endTime, String location, int courseTypeId) {
        ScheduledCourse existingScheduledCourse = getScheduledCourse(id);
        ScheduledCourse originalScheduledCourseCourse = new ScheduledCourse(existingScheduledCourse);
        if (courseTypeRepository.findCourseTypeById(courseTypeId) == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "Course type not found");
        }
        LocalDate localDate = LocalDate.parse(date);
        Date parsedDate = Date.valueOf(localDate);

        Time parsedStartTime = Time.valueOf(startTime);
        Time parsedEndTime = Time.valueOf(endTime);

        // update the scheduled course
        existingScheduledCourse.setDate(parsedDate);
        existingScheduledCourse.setStartTime(parsedStartTime);
        existingScheduledCourse.setEndTime(parsedEndTime);
        existingScheduledCourse.setLocation(location);
        existingScheduledCourse.setCourseType(courseTypeRepository.findById(courseTypeId).orElse(null));

        validateScheduledCourse(existingScheduledCourse);

        scheduledCourseRepository.save(existingScheduledCourse);

        notifyUsersOfCourseUpdate(originalScheduledCourseCourse, existingScheduledCourse);
        return existingScheduledCourse;
    }

    /**
     * notifies users of the course update
     * @param originalScheduledCourse
     * @param updatedScheduledCourse
     */
    private void notifyUsersOfCourseUpdate(ScheduledCourse originalScheduledCourse, ScheduledCourse updatedScheduledCourse) {
        if (!originalScheduledCourse.equals(updatedScheduledCourse)) {
            List<Registration> payments = registrationRepository.findRegistrationsByKeyScheduledCourse(originalScheduledCourse);
            List<Customer> affectedCustomers = new ArrayList<>();
            for (Registration payment : payments) {
                affectedCustomers.add(payment.getKey().getCustomer());
            }
            for (Customer customer : affectedCustomers) {
                try {
                    sendCourseUpdateNotificationEmail(originalScheduledCourse, updatedScheduledCourse, customer);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * Sends an email to the given customer notifying them of the update to the given course.
     * @param originalCourse
     * @param updatedCourse
     * @param customer
     * @throws IOException
     */
    private void sendCourseUpdateNotificationEmail(ScheduledCourse originalCourse, ScheduledCourse updatedCourse, Customer customer) throws IOException {
        String courseUpdateHtml = generateCourseUpdateHtml(originalCourse, updatedCourse, customer);
        String userEmail = personRepository.findById(customer.getId()).get().getEmail();

        MailConfigBean mailSender = new MailConfigBean("imap.gmail.com", "smtp.gmail.com", "sports.schedule.plus@gmail.com", "aqlq ldup ymfh eejb");
        mailer = new Mailer(mailSender);

        mailer.sendEmail("Course Update Notification", "The course you have registered for has been updated", courseUpdateHtml, userEmail);
    }

    /**
     * @param originalCourse
     * @param updatedCourse
     * @param customer
     * @return the HTML for a course update notification email in a string
     */
    private String generateCourseUpdateHtml(ScheduledCourse originalCourse, ScheduledCourse updatedCourse, Customer customer) {
        StringBuilder html = new StringBuilder();
        String customerName = personRepository.findById(customer.getId()).get().getName();

        html.append("<html>")
                .append("<body>")
                .append("<h2>Course Update Notification</h2>")
                .append("<p>Dear ").append(customerName).append(",</p>")
                .append("<p>The course you have registered for has been updated. Here are the details:</p>")
                .append("<table border=\"1\">")
                .append("<tr>")
                .append("<th>Property</th>")
                .append("<th>Original Value</th>")
                .append("<th>Updated Value</th>")
                .append("</tr>");

        appendTableRow(html, "Date", originalCourse.getDate().toString(), updatedCourse.getDate().toString());
        appendTableRow(html, "Start Time", originalCourse.getStartTime().toString(), updatedCourse.getStartTime().toString());
        appendTableRow(html, "End Time", originalCourse.getEndTime().toString(), updatedCourse.getEndTime().toString());
        appendTableRow(html, "Location", originalCourse.getLocation(), updatedCourse.getLocation());

        html.append("</table>")
                .append("</body>")
                .append("</html>");

        return html.toString();
    }

    /**
     * Helper method to create the HTML email.
     * Appends a table row to the given HTML string.
     * @param html
     * @param property
     * @param originalValue
     * @param updatedValue
     */
    private void appendTableRow(StringBuilder html, String property, String originalValue, String updatedValue) {
        html.append("<tr>")
                .append("<td>").append(property).append("</td>")
                .append("<td>").append(originalValue).append("</td>")
                .append("<td>").append(updatedValue).append("</td>")
                .append("</tr>");
    }

    /**
     * @param scheduledCourseId
     * @return the instructor who is supervising the course
     */
    @Transactional
    public List<Instructor> getInstructorsBySupervisedCourse(int scheduledCourseId) {
        ScheduledCourse scheduledCourse = getScheduledCourse(scheduledCourseId);
        List<Instructor> instructors = instructorRepository.findInstructorBySupervisedCourses(scheduledCourse);
        return instructors;
    }

    /**
     * @param id
     * @return the scheduled course with the given ID
     */
    @Transactional
    public ScheduledCourse getScheduledCourse(int id) {
        ScheduledCourse scheduledCourse = scheduledCourseRepository.findById(id);
        if (scheduledCourse == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no scheduled course with ID " + id + ".");
        }
        return scheduledCourse;
    }

    /**
     * @return list of all scheduled courses
     */
    @Transactional
    public List<ScheduledCourse> getAllScheduledCourses() {
        return Helper.toList(scheduledCourseRepository.findAll());
    }

    /**
     * @param location
     * @return list of all scheduled courses at the given location
     */
    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByLocation(String location) {
        return scheduledCourseRepository.findScheduledCourseByLocation(location);
    }

    /**
     * @param date
     * @return list of all scheduled courses on the given date
     */
    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByDate(Date date) {
        return scheduledCourseRepository.findScheduledCoursesByDate(date);
    }

    /**
     * @param courseType
     * @return list of all scheduled courses of the given course type
     */
    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByCourseType(CourseType courseType) {
        return scheduledCourseRepository.findScheduledCoursesByCourseType(courseType);
    }

    /**
     * @param startTime
     * @return list of all scheduled courses that start at the given time
     */
    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByStartTime(Time startTime) {
        return scheduledCourseRepository.findScheduledCoursesByStartTime(startTime);
    }

    /**
     * @param endTime
     * @return list of all scheduled courses that end at the given time
     */
    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByEndTime(Time endTime) {
        return scheduledCourseRepository.findScheduledCoursesByEndTime(endTime);
    }

    /**
     * deletes a scheduled course with the given ID
     * @param id
     */
    @Transactional
    public void deleteScheduledCourse(int id) {
        ScheduledCourse scheduledCourse = getScheduledCourse(id);
        scheduledCourseRepository.deleteById(id);
    }

    /**
     * Deletes all scheduled courses in the system
     */
    @Transactional
    public void deleteAllScheduledCourses() {
        List<ScheduledCourse> courses = Helper.toList(scheduledCourseRepository.findAll());

        if (courses == null || courses.isEmpty()) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There are no course types.");
        }
        scheduledCourseRepository.deleteAll();

    }

    /**
     * @param date
     * @return list of all scheduled courses in the week of a given date
     */
    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByWeek(String date) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        if (!Pattern.matches(regex, date)) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "Date needs to be provided in yyyy/mm/dd format");
        }
        LocalDate inputDate = LocalDate.parse(date);
        LocalDate mondayLocalDate = inputDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sundayLocalDate = inputDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        Date monday = java.sql.Date.valueOf(mondayLocalDate);
        Date sunday = java.sql.Date.valueOf(sundayLocalDate);
        return scheduledCourseRepository.findScheduledCoursesByDateBetween(monday, sunday);
    }


}