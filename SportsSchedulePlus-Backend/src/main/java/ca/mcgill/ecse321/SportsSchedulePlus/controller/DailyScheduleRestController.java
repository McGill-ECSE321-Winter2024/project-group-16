package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.DailyScheduleResponseDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.dto.DailyScheduleRequestDTO;
import ca.mcgill.ecse321.SportsSchedulePlus.service.DailyScheduleService;

/**
 * Rest controller for managing the opening hours of the sports center
 * @author Vladimir Venkov
 */
@CrossOrigin(origins = "*")
@RestController
public class DailyScheduleRestController {
    
    @Autowired
    private DailyScheduleService dailyScheduleService;

    /*
     * get the opening hours
     */
    @GetMapping(value = { "/openingHours", "/openingHours/" })
    public List<DailyScheduleResponseDTO> getAllDailySchedules() {
        return dailyScheduleService.getAllDailySchedules().stream().map(ds ->
        new DailyScheduleResponseDTO(ds)).collect(Collectors.toList());
    }

    @GetMapping(value = { "/openingHours/{id}", "/openingHours/{id}/" })
    public DailyScheduleResponseDTO getDailyScheduleById(@PathVariable("id") int id) {
        return new DailyScheduleResponseDTO(dailyScheduleService.getDailyScheduleById(id));
    }

    /*
     * update the opening hours for a day in the week
     */
    @PutMapping(value = { "/openingHours/{id}", "/openingHours/{id}/" })
    public DailyScheduleResponseDTO updateDailySchedule(@PathVariable("id") int id, @RequestBody DailyScheduleRequestDTO request) {
        Time openingTime = Time.valueOf(request.getOpeningTime());
        Time closingTime = Time.valueOf(request.getClosingTime());
        return new DailyScheduleResponseDTO(dailyScheduleService.updateDailyScheduleByID(id, openingTime, closingTime));
    }
}