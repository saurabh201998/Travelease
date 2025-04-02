package com.travelease.repositories;

import com.travelease.models.Schedule;
import com.travelease.models.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // Your original methods
    List<Schedule> findByRouteAndTravelDateOrderByDepartureTime(Route route, LocalDate travelDate);
    
    @Modifying
    @Query("UPDATE Schedule s SET s.availableSeats = s.availableSeats + :seatDelta WHERE s.id = :scheduleId")
    void updateAvailableSeats(@Param("scheduleId") Long scheduleId, @Param("seatDelta") int seatDelta);

    // New enhanced methods
    List<Schedule> findByRouteIdAndTravelDate(Long routeId, LocalDate travelDate);
    
    List<Schedule> findByBusIdOrderByTravelDateAscDepartureTimeAsc(Long busId);
    
    List<Schedule> findByTravelDateGreaterThanEqualOrderByTravelDateAscDepartureTimeAsc(LocalDate date);
    
    @Query("SELECT s FROM Schedule s WHERE s.route.sourceCity.id = :sourceCityId " +
           "AND s.route.destinationCity.id = :destinationCityId " +
           "AND s.travelDate = :travelDate " +
           "ORDER BY s.departureTime")
    List<Schedule> findByCitiesAndDate(
        @Param("sourceCityId") Long sourceCityId,
        @Param("destinationCityId") Long destinationCityId,
        @Param("travelDate") LocalDate travelDate);
}