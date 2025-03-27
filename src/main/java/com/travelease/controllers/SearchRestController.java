package com.travelease.controllers;

import com.travelease.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController  // This makes it return JSON instead of HTML
@RequestMapping("/api")  // All endpoints in this controller start with /api
public class SearchRestController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/search")
    public List<?> searchBuses(
            @RequestParam Long sourceCity,
            @RequestParam Long destinationCity,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate travelDate) {

        // Debugging: Print received parameters
        System.out.println("Received search request: " + sourceCity + ", " + destinationCity + ", " + travelDate);

        // Fetch schedules based on user input
        return scheduleService.findSchedulesByRouteAndDate(sourceCity, destinationCity, travelDate);
    }
}
