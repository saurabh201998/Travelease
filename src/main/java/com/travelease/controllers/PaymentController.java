package com.travelease.controllers;

import com.travelease.models.Booking;
import com.travelease.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/{bookingId}")
    public String showPaymentForm(@PathVariable Long bookingId, Model model) {
        Booking booking = bookingService.getBookingById(bookingId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));
        
        model.addAttribute("booking", booking);
        return "paymentForm";
    }

    @PostMapping("/process")
    public String processPayment(@RequestParam Long bookingId,
                               @RequestParam String cardNumber,
                               @RequestParam String cardHolder,
                               @RequestParam String expiry,
                               @RequestParam String cvv,
                               Model model) {
        Booking booking = bookingService.getBookingById(bookingId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));
        
        // Simulate payment processing
        boolean paymentSuccess = simulatePayment(cardNumber, cardHolder, expiry, cvv, booking.getTotalFare());
        
        if (paymentSuccess) {
            booking.setStatus("PAID");
            bookingService.saveBooking(booking);
            return "redirect:/bookings/" + bookingId + "?paymentSuccess=true";
        } else {
            model.addAttribute("error", "Payment failed. Please try again.");
            return showPaymentForm(bookingId, model);
        }
    }

    private boolean simulatePayment(String cardNumber, String cardHolder, 
                                  String expiry, String cvv, Double amount) {
        // Basic validation for simulation
        return cardNumber.length() == 16 && 
               !cardHolder.isEmpty() && 
               expiry.matches("\\d{2}/\\d{2}") && 
               cvv.matches("\\d{3}");
    }
}