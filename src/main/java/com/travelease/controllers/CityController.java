package com.travelease.controllers;

import com.travelease.models.City;
import com.travelease.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Change @Controller to @RestController
@RequestMapping("/api/cities") // Change the mapping to match Postman request
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable Long id) {
        return cityService.getCityById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid city ID: " + id));
    }

    @PostMapping
    public City saveCity(@RequestBody City city) { // Use @RequestBody for JSON input
        return cityService.saveCity(city);
    }

    @DeleteMapping("/{id}")
    public String deleteCity(@PathVariable Long id) {
        boolean deleted = cityService.deleteCity(id);
        if (deleted) {
            return "City deleted successfully";
        } else {
            return "City not found";
        }
    }
}
