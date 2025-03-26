package com.travelease.controllers;

import com.travelease.models.City;
import com.travelease.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public String listCities(Model model) {
        model.addAttribute("cities", cityService.getAllCities());
        return "admin/cities/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("city", new City());
        return "admin/cities/form";
    }

    @PostMapping
    public String saveCity(@Valid @ModelAttribute("city") City city, 
                           BindingResult result) {
        if (result.hasErrors()) {
            return "admin/cities/form";
        }
        cityService.saveCity(city);
        return "redirect:/admin/cities";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        City city = cityService.getCityById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid city ID: " + id));
        model.addAttribute("city", city);
        return "admin/cities/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return "redirect:/admin/cities";
    }
}
