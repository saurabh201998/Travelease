package com.travelease.services;

import com.travelease.models.Schedule;
import com.travelease.models.Route;
import com.travelease.models.City; // ✅ Import City model
import com.travelease.repositories.ScheduleRepository;
import com.travelease.repositories.RouteRepository;
import com.travelease.repositories.CityRepository; // ✅ Import CityRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private CityRepository cityRepository; // ✅ Inject CityRepository

    public List<Schedule> findSchedulesByRouteAndDate(Long sourceId, Long destId, LocalDate date) {
        // ✅ Fetch City objects using IDs
        City sourceCity = cityRepository.findById(sourceId)
                .orElseThrow(() -> new RuntimeException("Source city not found"));

        City destinationCity = cityRepository.findById(destId)
                .orElseThrow(() -> new RuntimeException("Destination city not found"));

        // ✅ Fetch Route using City objects
        Optional<Route> routeOptional = routeRepository.findBySourceCityAndDestinationCity(sourceCity, destinationCity);

        if (routeOptional.isPresent()) {
            Route route = routeOptional.get();
            // ✅ Query schedules using Route
            return scheduleRepository.findByRouteAndTravelDate(route, date);
        }
        return List.of(); // Return empty list if no route found
    }
}
