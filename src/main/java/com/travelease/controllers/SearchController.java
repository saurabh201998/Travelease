package com.travelease.controllers;

import com.travelease.models.City;
import com.travelease.services.CityService;
import com.travelease.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.util.Optional;

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

        // Return to search page if any parameter is missing
        if (sourceCity == null || destinationCity == null || travelDate == null) {
            model.addAttribute("cities", cityService.getAllCities());
            model.addAttribute("message", "Please enter all details to search for buses.");
            return "search";
        }

        // Get city objects
        Optional<City> sourceCityObj = cityService.getCityById(sourceCity);
        Optional<City> destCityObj = cityService.getCityById(destinationCity);

        if (!sourceCityObj.isPresent() || !destCityObj.isPresent()) {
            model.addAttribute("cities", cityService.getAllCities());
            model.addAttribute("message", "Invalid city selection.");
            return "search";
        }

        // Add all required attributes to the model
        model.addAttribute("schedules",
                scheduleService.findSchedulesByRouteAndDate(sourceCity, destinationCity, travelDate));
        model.addAttribute("sourceCity", sourceCityObj.get());
        model.addAttribute("destinationCity", destCityObj.get());
        model.addAttribute("travelDate", travelDate);

        return "searchResults";
    }
}