package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import java.util.List;
import java.sql.Time;
import java.util.ArrayList;

import ca.mcgill.ecse321.SportsSchedulePlus.service.dailyscheduleservice.DailyScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.dailyschedule.DailyScheduleResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.model.DailySchedule;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.dailyschedule.DailyScheduleRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.dailyschedule.DailyScheduleListResponseDTO;

/**
 * Rest controller for managing the opening hours of the sports center
 *
 * @author Vladimir Venkov
 */
@CrossOrigin(origins = "http://localhost:8087")
@RestController
public class DailyScheduleController {

    @Autowired
    private DailyScheduleService dailyScheduleService;

    /**
     * Retrieves all daily schedules
     * @return DailyScheduleListResponseDTO
     */
    @GetMapping(value = {"/openingHours", "/openingHours/"})
    public DailyScheduleListResponseDTO getAllDailySchedules() {
        List<DailyScheduleResponseDTO> dailyScheduleResponseDTOS = new ArrayList<>();
        for (DailySchedule dailySchedule : dailyScheduleService.getAllDailySchedules()) {
            dailyScheduleResponseDTOS.add(new DailyScheduleResponseDTO(dailySchedule));
        }
        return new DailyScheduleListResponseDTO(dailyScheduleResponseDTOS);
    }

    /**
     * Retrieves the opening hours for a day in the week
     * @param id
     * @return DailyScheduleResponseDTO 
     */
    @GetMapping(value = {"/openingHours/{id}", "/openingHours/{id}/"})
    public DailyScheduleResponseDTO getDailyScheduleById(@PathVariable("id") int id) {
        return new DailyScheduleResponseDTO(dailyScheduleService.getDailyScheduleById(id));
    }

    /**
     * Updates the opening hours for a day in the week
     * @param id
     * @param request
     * @return DailyScheduleResponseDTO
     */
    @PutMapping(value = {"/openingHours/{id}", "/openingHours/{id}/"})
    public DailyScheduleResponseDTO updateDailySchedule(@PathVariable("id") int id, @RequestBody DailyScheduleRequestDTO request) {
        Time openingTime = Time.valueOf(request.getOpeningTime());
        Time closingTime = Time.valueOf(request.getClosingTime());
        return new DailyScheduleResponseDTO(dailyScheduleService.updateDailyScheduleByID(id, openingTime, closingTime));
    }
}