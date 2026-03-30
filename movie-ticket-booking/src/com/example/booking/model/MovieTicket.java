package com.example.booking.model;

import com.example.booking.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MovieTicket {
    private final String bookingId;
    private final Show show;
    private final List<Seat> seats;
    private final int totalAmount;
    private final LocalDateTime bookedAt;
    private volatile BookingStatus status;

    public MovieTicket(String bookingId, Show show, List<Seat> seats, int totalAmount) {
        this.bookingId = Objects.requireNonNull(bookingId);
        this.show = Objects.requireNonNull(show);
        this.seats = Collections.unmodifiableList(seats);
        this.totalAmount = totalAmount;
        this.bookedAt = LocalDateTime.now();
        this.status = BookingStatus.CONFIRMED;
    }

    public String getBookingId() { return bookingId; }
    public Show getShow() { return show; }
    public List<Seat> getSeats() { return seats; }
    public int getTotalAmount() { return totalAmount; }
    public LocalDateTime getBookedAt() { return bookedAt; }
    public BookingStatus getStatus() { return status; }

    public void cancel() { this.status = BookingStatus.CANCELLED; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MOVIE TICKET ===\n");
        sb.append("Booking ID : ").append(bookingId).append("\n");
        sb.append("Movie      : ").append(show.getMovie().getTitle()).append("\n");
        sb.append("Theatre    : ").append(show.getTheatre().getName()).append("\n");
        sb.append("Screen     : ").append(show.getScreen().getName()).append("\n");
        sb.append("Show Time  : ").append(show.getStartTime()).append("\n");
        sb.append("Seats      : ");
        for (Seat s : seats) sb.append(s.getId()).append(" ");
        sb.append("\n");
        sb.append("Total      : Rs. ").append(totalAmount).append("\n");
        sb.append("Status     : ").append(status).append("\n");
        sb.append("====================");
        return sb.toString();
    }
}
