package com.travelease.services;

import com.travelease.models.Schedule;
import com.travelease.models.Route;
import com.travelease.models.City;
import com.travelease.repositories.ScheduleRepository;
import com.travelease.repositories.RouteRepository;
import com.travelease.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final RouteRepository routeRepository;
    private final CityRepository cityRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository,
                         RouteRepository routeRepository,
                         CityRepository cityRepository) {
        this.scheduleRepository = scheduleRepository;
        this.routeRepository = routeRepository;
        this.cityRepository = cityRepository;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    public List<Schedule> findSchedulesByRouteAndDate(Long sourceId, Long destId, LocalDate date) {
        City sourceCity = cityRepository.findById(sourceId)
                .orElseThrow(() -> new RuntimeException("Source city not found"));
        
        City destinationCity = cityRepository.findById(destId)
                .orElseThrow(() -> new RuntimeException("Destination city not found"));

        Optional<Route> routeOptional = routeRepository.findBySourceCityAndDestinationCity(sourceCity, destinationCity);

        return routeOptional.map(route -> 
            scheduleRepository.findByRouteAndTravelDateOrderByDepartureTime(route, date))
            .orElse(List.of());
    }

    public List<Schedule> findSchedulesByRouteIdAndDate(Long routeId, LocalDate travelDate) {
        return scheduleRepository.findByRouteIdAndTravelDate(routeId, travelDate);
    }

    @Transactional
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Schedule updateAvailableSeats(Long scheduleId, int seatsBooked) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        
        int newAvailableSeats = schedule.getAvailableSeats() - seatsBooked;
        if (newAvailableSeats < 0) {
            throw new IllegalStateException("Not enough seats available");
        }
        
        schedule.setAvailableSeats(newAvailableSeats);
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> findSchedulesByBus(Long busId) {
        return scheduleRepository.findByBusIdOrderByTravelDateAscDepartureTimeAsc(busId);
    }

    public List<Schedule> findUpcomingSchedules() {
        return scheduleRepository.findByTravelDateGreaterThanEqualOrderByTravelDateAscDepartureTimeAsc(LocalDate.now());
    }
}
