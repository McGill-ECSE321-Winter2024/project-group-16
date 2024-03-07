package ca.mcgill.ecse321.SportsSchedulePlus.controller;

import ca.mcgill.ecse321.SportsSchedulePlus.dto.*;
import ca.mcgill.ecse321.SportsSchedulePlus.model.*;
import ca.mcgill.ecse321.SportsSchedulePlus.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;


@CrossOrigin(origins = "*")
@RestController
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping(value={"/owner/{id}"})
    public OwnerDto getOwner(@PathVariable("id") int id){
        Owner owner = ownerService.getOwner(id);
        return convertToDto(owner);
    }

    @GetMapping(value={"/owner/suggested-courses"})
    public OwnerDto getOwnerByOwnerSuggestedCourses(@RequestParam CourseTypeDto courseTypeDto){
        CourseType courseType = new CourseType(courseTypeDto.getDescription(), courseTypeDto.isApprovedByOwner(), courseTypeDto.getPrice());
        Owner owner = ownerService.getInstructorByOwnerSuggestedCourses(courseType);
        return convertToDto(owner);

    }

    @GetMapping(value={"/owner/approved-courses"})
    public OwnerDto getOwnerByApprovedCourses(@RequestParam CourseTypeDto courseTypeDto){
        CourseType courseType = new CourseType(courseTypeDto.getDescription(), courseTypeDto.isApprovedByOwner(), courseTypeDto.getPrice());
        Owner owner = ownerService.getOwnerByApprovedCourses(courseType);
        return convertToDto(owner);
    }

    @GetMapping(value={"/owner/daily-schedule"})
    public OwnerDto getOwnerByDailySchedule(@RequestParam DailyScheduleDto dailyScheduleDto){
        DailySchedule dailySchedule = new DailySchedule(dailyScheduleDto.getOpeningTime(),dailyScheduleDto.getClosingTime());
        Owner owner = ownerService.getOwnerByDailySchedule(dailySchedule);
        return convertToDto(owner);
    }
    @PostMapping(value={"/owner"})
    public PersonDto createOwner(@RequestParam PersonDto personDto){
        Person person = ownerService.createOwner(personDto.getName(), personDto.getEmail(), personDto.getPassword());
        return convertToDto(person);
    }

    @PostMapping(value={"/owner/{id}"})
    public PersonDto updatedOwner(@PathVariable("id") int id, @RequestParam String password){
        Person person = ownerService.updateOwner(id, password);
        return convertToDto(person);
    }


    private PersonDto convertToDto(Person p){
        if (p == null){
            throw new IllegalArgumentException("There is no such owner!");
        }
        PersonDto personDto = new PersonDto(p.getName(), p.getEmail(), p.getPassword());
        return personDto;
    }
    private OwnerDto convertToDto(Owner owner){
        if (owner == null){
            throw new IllegalArgumentException("There is no such owner!");
        }
        DailySchedule dailySchedule = owner.getDailySchedule();
        int id = dailySchedule.getId();
        Time openingTime = dailySchedule.getOpeningTime();
        Time closingTime = dailySchedule.getClosingTime();
        DailyScheduleDto dailyScheduleDto = new DailyScheduleDto(id, openingTime, closingTime);
        OwnerDto ownerDto = new OwnerDto(owner.getId(),dailyScheduleDto);
        return ownerDto;
    }


}
