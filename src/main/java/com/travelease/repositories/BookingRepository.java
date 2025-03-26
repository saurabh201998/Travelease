package com.travelease.repositories;

import com.travelease.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByPhone(String phone);
    List<Booking> findByEmail(String email);
}