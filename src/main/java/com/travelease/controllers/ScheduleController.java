package com.travelease.controllers;

import com.travelease.models.Schedule;
import com.travelease.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")  // 🔹 Use '/api/schedules' for RESTful convention
@CrossOrigin(origins = "*")  // 🔹 Allow frontend to access API (Optional)
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/search")  // 🔹 Explicitly name endpoint as 'search'
    public ResponseEntity<List<Schedule>> searchSchedules(
            @RequestParam Long sourceId,
            @RequestParam Long destId,
            @RequestParam String date) {
        
        LocalDate travelDate = LocalDate.parse(date);
        List<Schedule> schedules = scheduleService.findSchedulesByRouteAndDate(sourceId, destId, travelDate);
        
        if (schedules.isEmpty()) {
            return ResponseEntity.noContent().build(); // ✅ 204 No Content if no schedules found
        }
        
        return ResponseEntity.ok(schedules);  // ✅ Return 200 OK with schedule list
    }
}
