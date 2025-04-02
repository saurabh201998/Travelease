package com.travelease.services;

import com.travelease.models.Booking;
import com.travelease.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Booking saveBooking(Booking booking) {
        try {
            return bookingRepository.save(booking);
        } catch (DataIntegrityViolationException e) {
            Throwable rootCause = e.getRootCause();
            throw new IllegalStateException("Database error: " + 
                (rootCause != null ? rootCause.getMessage() : e.getMessage()));
        }
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Transactional
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public List<Booking> getBookingsByPhone(String phone) {
        return bookingRepository.findByPhone(phone);
    }

    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByEmail(email);
    }
}
