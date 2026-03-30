package com.example.booking.service;

import com.example.booking.enums.BookingStatus;
import com.example.booking.exception.BookingNotFoundException;
import com.example.booking.exception.SeatNotAvailableException;
import com.example.booking.model.MovieTicket;
import com.example.booking.model.Seat;
import com.example.booking.model.Show;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public  class BookingService {
    private final Map<String, MovieTicket> bookings = new ConcurrentHashMap<>();
    private final Map<String, String> bookingToTxn = new ConcurrentHashMap<>();
    private final PaymentService paymentService;
    private final AtomicLong idCounter = new AtomicLong(1);

    public BookingService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public MovieTicket bookTickets(Show show, List<String> seatIds) {
        List<Seat> requestedSeats = new ArrayList<>();
        for (String seatId : seatIds) {
            Seat seat = show.getScreen().getSeats().stream()
                    .filter(s -> s.getId().equals(seatId))
                    .findFirst()
                    .orElseThrow(() -> new SeatNotAvailableException("Seat " + seatId + " not found in show"));
            requestedSeats.add(seat);
        }

        List<Seat> lockedSeats = new ArrayList<>();
        try {
            for (Seat seat : requestedSeats) {
                if (!seat.lock()) {
                    throw new SeatNotAvailableException("Seat " + seat.getId() + " is already taken");
                }
                lockedSeats.add(seat);
            }

            int total = lockedSeats.stream().mapToInt(Seat::getPrice).sum();
            String txnId = paymentService.processPayment(total);

            for (Seat seat : lockedSeats) {
                seat.confirmBooking();
            }

            String bookingId = "BKG-" + idCounter.getAndIncrement();
            MovieTicket ticket = new MovieTicket(bookingId, show, lockedSeats, total);
            bookings.put(bookingId, ticket);
            bookingToTxn.put(bookingId, txnId);

            return ticket;
        } catch (SeatNotAvailableException e) {
            for (Seat s : lockedSeats) {
                s.release();
            }
            throw e;
        }
    }

    public MovieTicket cancelBooking(String bookingId) {
        MovieTicket ticket = bookings.get(bookingId);
        if (ticket == null) {
            throw new BookingNotFoundException("No booking found: " + bookingId);
        }
        if (ticket.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking " + bookingId + " is already cancelled");
        }

        for (Seat seat : ticket.getSeats()) {
            seat.release();
        }

        String originalTxn = bookingToTxn.get(bookingId);
        paymentService.processRefund(originalTxn, ticket.getTotalAmount());

        ticket.cancel();
        return ticket;
    }

    public MovieTicket getBooking(String bookingId) {
        return bookings.get(bookingId);
    }
}
