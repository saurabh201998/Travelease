package com.travelease.controllers;

import com.travelease.models.*;
import com.travelease.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import com.razorpay.*;
import org.json.JSONObject;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final ScheduleService scheduleService;
    private final BookingService bookingService;
    
    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Autowired
    public BookingController(ScheduleService scheduleService,
                           BookingService bookingService) {
        this.scheduleService = scheduleService;
        this.bookingService = bookingService;
    }

    @GetMapping("/new")
    public String showBookingForm(@RequestParam Long scheduleId, Model model) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID"));
        
        Booking booking = new Booking();
        model.addAttribute("schedule", schedule);
        model.addAttribute("booking", booking);
        model.addAttribute("sourceCity", schedule.getRoute().getSourceCity());
        model.addAttribute("destinationCity", schedule.getRoute().getDestinationCity());
        
        return "booking/bookingForm";
    }

    @PostMapping
    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public String createBooking(@ModelAttribute("booking") Booking booking,
                              @RequestParam Long scheduleId,
                              Model model) {
        try {
            // Validate input
            if (booking.getNumberOfSeats() == null || booking.getNumberOfSeats() <= 0) {
                throw new IllegalArgumentException("Number of seats must be positive");
            }
            
            Schedule schedule = scheduleService.getScheduleById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID"));

            if (booking.getNumberOfSeats() > schedule.getAvailableSeats()) {
                throw new IllegalStateException("Only " + schedule.getAvailableSeats() + " seats available");
            }
            
            // Complete and save booking
            booking.completeBookingDetails(schedule);
            booking.setPaymentStatus("PENDING"); // Initialize payment status
            Booking savedBooking = bookingService.saveBooking(booking);
            
            // Update available seats
            scheduleService.updateAvailableSeats(scheduleId, booking.getNumberOfSeats());
            
            return "redirect:/bookings/" + savedBooking.getId();
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return handleBookingError(scheduleId, model);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = e.getMostSpecificCause() != null ? 
                e.getMostSpecificCause().getMessage() : "Database constraint violation";
            model.addAttribute("error", "Database error: " + errorMessage);
            return handleBookingError(scheduleId, model);
        } catch (Exception e) {
            model.addAttribute("error", "System error: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));
        
        model.addAttribute("booking", booking);
        model.addAttribute("razorpayKeyId", razorpayKeyId);
        return "booking/bookingConfirmation";
    }

    @PostMapping("/{id}/initiate-payment")
    @ResponseBody
    public String initiatePayment(@PathVariable Long id) throws RazorpayException {
        Booking booking = bookingService.getBookingById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));

        RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", booking.getTotalFare() * 100); // amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_" + booking.getId());
        orderRequest.put("payment_capture", 1);
        
        Order order = razorpay.orders.create(orderRequest);
        return order.toString();
    }

    @PostMapping("/{id}/complete-payment")
    @Transactional
    public String completePayment(@PathVariable Long id,
                                @RequestParam String razorpayPaymentId,
                                @RequestParam String razorpayOrderId,
                                @RequestParam String razorpaySignature,
                                Model model) {
        try {
            Booking booking = bookingService.getBookingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));

            // In production, verify the signature here
            // verifyPaymentSignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);
            
            booking.setPaymentStatus("PAID");
            booking.setPaymentId(razorpayPaymentId);
            booking.setPaymentMethod("RAZORPAY");
            bookingService.saveBooking(booking);
            
            return "redirect:/bookings/" + id + "/success";
        } catch (Exception e) {
            model.addAttribute("error", "Payment failed: " + e.getMessage());
            return "paymentError";
        }
    }

    @GetMapping("/{id}/success")
    public String paymentSuccess(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));
        
        model.addAttribute("booking", booking);
        return "booking/paymentSuccess";
    }

    private String handleBookingError(Long scheduleId, Model model) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId).orElse(null);
        if (schedule != null) {
            model.addAttribute("schedule", schedule);
            model.addAttribute("sourceCity", schedule.getRoute().getSourceCity());
            model.addAttribute("destinationCity", schedule.getRoute().getDestinationCity());
        }
        return "booking/bookingForm";
    }
}