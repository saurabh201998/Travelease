package com.travelease.services;

import com.travelease.models.Schedule;
import com.travelease.models.Route;
import com.travelease.models.City;
import com.travelease.repositories.ScheduleRepository;
import com.travelease.repositories.RouteRepository;
import com.travelease.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private CityRepository cityRepository;

    // Get all schedules
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // Get schedule by ID
    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    // Find schedules by route and date
    public List<Schedule> findSchedulesByRouteAndDate(Long sourceId, Long destId, LocalDate date) {
        City sourceCity = cityRepository.findById(sourceId)
                .orElseThrow(() -> new RuntimeException("Source city not found"));
        
        City destinationCity = cityRepository.findById(destId)
                .orElseThrow(() -> new RuntimeException("Destination city not found"));

        Optional<Route> routeOptional = routeRepository.findBySourceCityAndDestinationCity(sourceCity, destinationCity);

        if (routeOptional.isPresent()) {
            Route route = routeOptional.get();
            return scheduleRepository.findByRouteAndTravelDateOrderByDepartureTime(route, date);
        }
        return List.of();
    }

    // Find schedules by route ID and date (direct query version)
    public List<Schedule> findSchedulesByRouteIdAndDate(Long routeId, LocalDate travelDate) {
        return scheduleRepository.findByRouteIdAndTravelDate(routeId, travelDate);
    }

    // Save or update a schedule
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    // Delete a schedule
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    // Update available seats when booking is made
    public Schedule updateAvailableSeats(Long scheduleId, int seatsBooked) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        
        int newAvailableSeats = schedule.getAvailableSeats() - seatsBooked;
        if (newAvailableSeats < 0) {
            throw new RuntimeException("Not enough seats available");
        }
        
        schedule.setAvailableSeats(newAvailableSeats);
        return scheduleRepository.save(schedule);
    }

    // Find schedules by bus
    public List<Schedule> findSchedulesByBus(Long busId) {
        return scheduleRepository.findByBusIdOrderByTravelDateAscDepartureTimeAsc(busId);
    }

    // Find upcoming schedules
    public List<Schedule> findUpcomingSchedules() {
        return scheduleRepository.findByTravelDateGreaterThanEqualOrderByTravelDateAscDepartureTimeAsc(LocalDate.now());
    }
}