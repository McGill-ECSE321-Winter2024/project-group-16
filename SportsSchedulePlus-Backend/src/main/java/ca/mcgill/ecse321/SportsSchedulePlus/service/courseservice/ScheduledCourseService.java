package ca.mcgill.ecse321.SportsSchedulePlus.service.courseservice;

import ca.mcgill.ecse321.SportsSchedulePlus.beans.MailConfigBean;
import ca.mcgill.ecse321.SportsSchedulePlus.exception.SportsScheduleException;
import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Customer;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Instructor;
import ca.mcgill.ecse321.SportsSchedulePlus.model.Registration;
import ca.mcgill.ecse321.SportsSchedulePlus.model.ScheduledCourse;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.CourseTypeRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.InstructorRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.RegistrationRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.PersonRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.repository.ScheduledCourseRepository;
import ca.mcgill.ecse321.SportsSchedulePlus.service.mailerservice.Mailer;
import ca.mcgill.ecse321.utils.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


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

    private Mailer mailer;

  
    @Transactional
    public ScheduledCourse createScheduledCourse(String date, String startTime, String endTime, String location, int courseTypeId) {
        // Parse date string
        LocalDate localDate = LocalDate.parse(date);
        // Convert LocalDate to java.sql.Date
        Date parsedDate = Date.valueOf(localDate);

        // Parse time strings
        Time parsedStartTime = Time.valueOf(startTime);
        Time parsedEndTime = Time.valueOf(endTime);

        // Use parsed values to create ScheduledCourse
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

    private void validateScheduledCourse(ScheduledCourse scheduledCourse) {
        // Check if CourseType is not null
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

        // Check if end time is greater than start time
        if (scheduledCourse.getEndTime().before(scheduledCourse.getStartTime())) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "End time must be after start time.");
        }
    }

    @Transactional
    public ScheduledCourse updateScheduledCourse(int id, String date, String startTime, String endTime,
                                                 String location, int courseTypeId) {
        // Find the existing scheduled course
        ScheduledCourse existingScheduledCourse = scheduledCourseRepository.findById(id);
        ScheduledCourse originalScheduledCourseCourse = new ScheduledCourse(existingScheduledCourse);
        if (existingScheduledCourse == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "Scheduled course not found");
        }
        // Parse date string
        LocalDate localDate = LocalDate.parse(date);
        // Convert LocalDate to java.sql.Date
        Date parsedDate = Date.valueOf(localDate);
    
        // Parse time strings
        Time parsedStartTime = Time.valueOf(startTime);
        Time parsedEndTime = Time.valueOf(endTime);
    
        // Update the existing scheduled course with the new values
        existingScheduledCourse.setDate(parsedDate);
        existingScheduledCourse.setStartTime(parsedStartTime);
        existingScheduledCourse.setEndTime(parsedEndTime);
        existingScheduledCourse.setLocation(location);
        existingScheduledCourse.setCourseType(courseTypeRepository.findById(courseTypeId).orElse(null));
    
        // Validate the updated scheduled course
        validateScheduledCourse(existingScheduledCourse);

        // Save the updated scheduled course
        scheduledCourseRepository.save(existingScheduledCourse);

        // Notify users of update
        notifyUsersOfCourseUpdate(originalScheduledCourseCourse,existingScheduledCourse);
        return existingScheduledCourse;
    }

    // Method to notify users of the course update
    private void notifyUsersOfCourseUpdate(ScheduledCourse originalScheduledCourse, ScheduledCourse updatedScheduledCourse) {
        // Check for differences and send notifications to affected users
        if (!originalScheduledCourse.equals(updatedScheduledCourse)) {
            List<Registration> payments = registrationRepository.findRegistrationsByKeyScheduledCourse(originalScheduledCourse);
            List <Customer> affectedCustomers = new ArrayList<>();
            for (Registration payment : payments){
                // Retrieve the affected customers
                affectedCustomers.add(payment.getKey().getCustomer());
            }
            for (Customer customer : affectedCustomers) {
                try {
                    // Send course update notification
                    sendCourseUpdateNotificationEmail(originalScheduledCourse, updatedScheduledCourse, customer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to send course update notification email
    private void sendCourseUpdateNotificationEmail(ScheduledCourse originalCourse, ScheduledCourse updatedCourse, Customer customer) throws IOException {
        // Build the email content and send the notification to the customer
        String courseUpdateHtml = generateCourseUpdateHtml(originalCourse, updatedCourse, customer);

        String userEmail = personRepository.findById(customer.getId()).get().getEmail();

        // Set up mail configuration
        MailConfigBean mailSender = new MailConfigBean("imap.gmail.com", "smtp.gmail.com", "sports.schedule.plus@gmail.com", "aqlq ldup ymfh eejb");
        mailer = new Mailer(mailSender);

        // Sending the email using the custom Mailer
        mailer.sendEmail("Course Update Notification", "The course you have registered for has been updated", courseUpdateHtml, userEmail);
    }

    // Method to generate HTML content for the course update notification
    private String generateCourseUpdateHtml(ScheduledCourse originalCourse, ScheduledCourse updatedCourse, Customer customer) {
        // Build the HTML content for the course update notification
        StringBuilder html = new StringBuilder();
        String customerName = personRepository.findById(customer.getId()).get().getName();

        // Table header
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

        // Table rows for each property
        appendTableRow(html, "Date", originalCourse.getDate().toString(), updatedCourse.getDate().toString());
        appendTableRow(html, "Start Time", originalCourse.getStartTime().toString(), updatedCourse.getStartTime().toString());
        appendTableRow(html, "End Time", originalCourse.getEndTime().toString(), updatedCourse.getEndTime().toString());
        appendTableRow(html, "Location", originalCourse.getLocation(), updatedCourse.getLocation());
        // Add more rows for other properties as needed

        // Close the table and HTML tags
        html.append("</table>")
            .append("</body>")
            .append("</html>");

        return html.toString();
    }

    // Helper method to append a table row
    private void appendTableRow(StringBuilder html, String property, String originalValue, String updatedValue) {
        html.append("<tr>")
            .append("<td>").append(property).append("</td>")
            .append("<td>").append(originalValue).append("</td>")
            .append("<td>").append(updatedValue).append("</td>")
            .append("</tr>");
    }


    @Transactional
    public List<Instructor> getInstructorsBySupervisedCourse(int scheduledCourseId) {
        ScheduledCourse scheduledCourse = scheduledCourseRepository.findById(scheduledCourseId);
        if (scheduledCourse == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "Scheduled course not found with ID: " + scheduledCourseId);
        }
        List<Instructor> instructors = instructorRepository.findInstructorBySupervisedCourses(scheduledCourse);
        return instructors;
    }
    
    @Transactional
    public ScheduledCourse getScheduledCourse(int id) {
       ScheduledCourse scheduledCourse = scheduledCourseRepository.findById(id);
       if(scheduledCourse == null){
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no scheduled course with ID " + id + ".");
        }
       return scheduledCourse;
    }

    @Transactional
    public List<ScheduledCourse> getAllScheduledCourses() {
        return Helper.toList(scheduledCourseRepository.findAll());
    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByLocation(String location) {
        return scheduledCourseRepository.findScheduledCourseByLocation(location);
    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByDate(Date date) {
        return scheduledCourseRepository.findScheduledCoursesByDate(date);
    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByCourseType(CourseType courseType) {
        return scheduledCourseRepository.findScheduledCoursesByCourseType(courseType);
    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByStartTime(Time startTime) {
        return scheduledCourseRepository.findScheduledCoursesByStartTime(startTime);
    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByEndTime(Time endTime) {
        return scheduledCourseRepository.findScheduledCoursesByEndTime(endTime);
    }

    @Transactional
    public void deleteScheduledCourse(int id) {
        ScheduledCourse scheduledCourse = scheduledCourseRepository.findById(id);
        if(scheduledCourse == null){
             throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no scheduled course with ID " + id + ".");
         }

        scheduledCourseRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteAllScheduledCourses() {
        List<ScheduledCourse> courses = Helper.toList(scheduledCourseRepository.findAll());

        if (courses == null || courses.isEmpty()){
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There are no course types.");
        }
        
        scheduledCourseRepository.deleteAll();

    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByWeek(Date monday, Date sunday) {
        return scheduledCourseRepository.findScheduledCoursesByDateBetween(monday, sunday);
    }



}
