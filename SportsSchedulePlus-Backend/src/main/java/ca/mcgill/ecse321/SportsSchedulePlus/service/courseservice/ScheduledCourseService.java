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


    @Transactional
    public ScheduledCourse createScheduledCourse(String date, String startTime, String endTime, String location, int courseTypeId) {
        LocalDate localDate = LocalDate.parse(date);
        Date parsedDate = Date.valueOf(localDate);

        Time parsedStartTime = Time.valueOf(startTime);
        Time parsedEndTime = Time.valueOf(endTime);

        long durationInMillis = parsedEndTime.getTime() - parsedStartTime.getTime();
        long durationInMinutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis);

        if (durationInMinutes < 30) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Duration must be at least 30 minutes.");
        }

        ScheduledCourse scheduledCourse = new ScheduledCourse();
        scheduledCourse.setDate(parsedDate);
        scheduledCourse.setStartTime(parsedStartTime);
        scheduledCourse.setEndTime(parsedEndTime);
        scheduledCourse.setLocation(location);
        scheduledCourse.setCourseType(courseTypeRepository.findById(courseTypeId).orElse(null));

        validateScheduledCourse(scheduledCourse);
        validateStartAndEndTimes(scheduledCourse);

        scheduledCourseRepository.save(scheduledCourse);
        return scheduledCourse;
    }

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

    }

    private void validateStartAndEndTimes(ScheduledCourse scheduledCourse) {

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int mappedDayOfWeek = (dayOfWeek + 6) % 7;

        DailySchedule dailySchedule = dailyScheduleService.getDailyScheduleById(mappedDayOfWeek);

        if (scheduledCourse.getStartTime().before(dailySchedule.getOpeningTime()) || scheduledCourse.getEndTime().after(dailySchedule.getClosingTime())) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Scheduled course must respect opening and closing hours.");
        }

    }

    @Transactional
    public ScheduledCourse updateScheduledCourse(int id, String date, String startTime, String endTime,
                                                 String location, int courseTypeId) {
        ScheduledCourse existingScheduledCourse = scheduledCourseRepository.findById(id);
        ScheduledCourse originalScheduledCourseCourse = new ScheduledCourse(existingScheduledCourse);
        if (existingScheduledCourse == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "Scheduled course not found");
        }
        if (courseTypeRepository.findCourseTypeById(courseTypeId) == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "Course type not found");
        }
        LocalDate localDate = LocalDate.parse(date);
        Date parsedDate = Date.valueOf(localDate);

        Time parsedStartTime = Time.valueOf(startTime);
        Time parsedEndTime = Time.valueOf(endTime);

        long durationInMillis = parsedEndTime.getTime() - parsedStartTime.getTime();
        long durationInMinutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis);

        if (durationInMinutes < 30) {
            throw new SportsScheduleException(HttpStatus.BAD_REQUEST, "Duration must be at least 30 minutes.");
        }

        existingScheduledCourse.setDate(parsedDate);
        existingScheduledCourse.setStartTime(parsedStartTime);
        existingScheduledCourse.setEndTime(parsedEndTime);
        existingScheduledCourse.setLocation(location);
        existingScheduledCourse.setCourseType(courseTypeRepository.findById(courseTypeId).orElse(null));

        validateScheduledCourse(existingScheduledCourse);
        validateStartAndEndTimes(existingScheduledCourse);

        scheduledCourseRepository.save(existingScheduledCourse);

        notifyUsersOfCourseUpdate(originalScheduledCourseCourse, existingScheduledCourse);
        return existingScheduledCourse;
    }

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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendCourseUpdateNotificationEmail(ScheduledCourse originalCourse, ScheduledCourse updatedCourse, Customer customer) throws IOException {
        String courseUpdateHtml = generateCourseUpdateHtml(originalCourse, updatedCourse, customer);
        String userEmail = personRepository.findById(customer.getId()).get().getEmail();

        MailConfigBean mailSender = new MailConfigBean("imap.gmail.com", "smtp.gmail.com", "sports.schedule.plus@gmail.com", "aqlq ldup ymfh eejb");
        mailer = new Mailer(mailSender);

        mailer.sendEmail("Course Update Notification", "The course you have registered for has been updated", courseUpdateHtml, userEmail);
    }

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
        if (scheduledCourse == null) {
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
        if (scheduledCourse == null) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There is no scheduled course with ID " + id + ".");
        }

        scheduledCourseRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllScheduledCourses() {
        List<ScheduledCourse> courses = Helper.toList(scheduledCourseRepository.findAll());

        if (courses == null || courses.isEmpty()) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "There are no course types.");
        }

        scheduledCourseRepository.deleteAll();

    }

    @Transactional
    public List<ScheduledCourse> getScheduledCoursesByWeek(String date) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        if (!Pattern.matches(regex, date)) {
            throw new SportsScheduleException(HttpStatus.NOT_FOUND, "Date needs to be provided in YEAR/MONTH/DATE format");
        }
        LocalDate inputDate = LocalDate.parse(date);
        LocalDate mondayLocalDate = inputDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sundayLocalDate = inputDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        Date monday = java.sql.Date.valueOf(mondayLocalDate);
        Date sunday = java.sql.Date.valueOf(sundayLocalDate);
        return scheduledCourseRepository.findScheduledCoursesByDateBetween(monday, sunday);
    }


}
