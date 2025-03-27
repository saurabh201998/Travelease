package com.travelease.controllers;

import com.travelease.services.CityService;
import com.travelease.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;

@Controller
public class SearchController {

    @Autowired
    private CityService cityService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/")
    public String showSearchForm(Model model) {
        model.addAttribute("cities", cityService.getAllCities());
        return "search";
    }

    @GetMapping("/search")
    public String searchBuses(
            @RequestParam(required = false) Long sourceCity,
            @RequestParam(required = false) Long destinationCity,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate travelDate,
            Model model) {

        // If any parameter is missing, return search page without filtering results
        if (sourceCity == null || destinationCity == null || travelDate == null) {
            model.addAttribute("message", "Please enter all details to search for buses.");
            return "search"; // This should match search.html inside templates
        }

        // Fetch schedules if all parameters are present
        model.addAttribute("schedules",
                scheduleService.findSchedulesByRouteAndDate(sourceCity, destinationCity, travelDate));
        return "searchResults"; // This should match searchResults.html inside templates
    }
}
