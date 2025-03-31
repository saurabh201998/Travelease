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
    List<Schedule> findByRouteAndTravelDate(Route route, LocalDate travelDate);
    
    @Modifying
    @Query("UPDATE Schedule s SET s.availableSeats = s.availableSeats + :seatDelta WHERE s.id = :scheduleId")
    void updateAvailableSeats(@Param("scheduleId") Long scheduleId, @Param("seatDelta") int seatDelta);
}