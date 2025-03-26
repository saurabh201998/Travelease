package com.travelease.repositories;

import com.travelease.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByRouteIdAndTravelDate(Long routeId, LocalDate travelDate);
}