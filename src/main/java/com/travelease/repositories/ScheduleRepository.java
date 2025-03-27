package com.travelease.repositories;

import com.travelease.models.Schedule;
import com.travelease.models.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByRouteAndTravelDate(Route route, LocalDate travelDate);
}
