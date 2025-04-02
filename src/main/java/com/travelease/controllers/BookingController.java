package com.travelease.controllers;

import java.time.LocalDateTime;
import com.travelease.models.*;
import com.travelease.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private ScheduleService scheduleService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private CityService cityService;

    @GetMapping("/new")
    public String showBookingForm(@RequestParam Long scheduleId, Model model) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID"));
        
        model.addAttribute("schedule", schedule);
        model.addAttribute("booking", new Booking());
        model.addAttribute("sourceCity", schedule.getRoute().getSourceCity());
        model.addAttribute("destinationCity", schedule.getRoute().getDestinationCity());
        
        return "booking/bookingForm";
    }

    @PostMapping
    public String createBooking(@ModelAttribute Booking booking, 
                              @RequestParam Long scheduleId,
                              Model model) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID"));
        
        // Validate available seats
        if (booking.getNumberOfSeats() > schedule.getAvailableSeats()) {
            model.addAttribute("error", "Not enough seats available");
            return showBookingForm(scheduleId, model);
        }
        
        // Set booking details
        booking.setSchedule(schedule);
        booking.setTotalFare(schedule.getFare() * booking.getNumberOfSeats());
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("CONFIRMED");
        
        // Save booking
        bookingService.saveBooking(booking);
        
        // Update available seats
        schedule.setAvailableSeats(schedule.getAvailableSeats() - booking.getNumberOfSeats());
        scheduleService.saveSchedule(schedule);
        
        return "redirect:/bookings/" + booking.getId();
    }

    @GetMapping("/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));
        
        model.addAttribute("booking", booking);
        return "bookingConfirmation";
    }
}