package com.travelease.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    @NotNull(message = "Schedule is required")
    private Schedule schedule;

    @NotBlank(message = "Passenger name is required")
    @Column(nullable = false)
    private String passengerName;

    @Column
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false)
    private String phone;

    @NotNull(message = "Number of seats must be positive")
    @Column(nullable = false)
    private Integer numberOfSeats;

    @Column(name = "total_fare", nullable = false)
    private Double totalFare;
    
    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;
    
    @Column(nullable = false)
    private String status = "CONFIRMED";
    
    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;

    @Column(name = "payment_status")
    private String paymentStatus;
    
    @Column(name = "payment_id")
    private String paymentId;
    
    @Column(name = "payment_method")
    private String paymentMethod;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Schedule getSchedule() { return schedule; }
    public void setSchedule(Schedule schedule) { this.schedule = schedule; }
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Integer getNumberOfSeats() { return numberOfSeats; }
    public void setNumberOfSeats(Integer numberOfSeats) { this.numberOfSeats = numberOfSeats; }
    public Double getTotalFare() { return totalFare; }
    public void setTotalFare(Double totalFare) { this.totalFare = totalFare; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    // Business method
    public void completeBookingDetails(Schedule schedule) {
        this.schedule = schedule;
        if (schedule != null) {
            this.travelDate = schedule.getTravelDate();
            this.bookingTime = LocalDateTime.now();
            this.status = "CONFIRMED";
            if (this.numberOfSeats != null && schedule.getFare() != null) {
                this.totalFare = schedule.getFare() * this.numberOfSeats;
            }
        }
    }
}
