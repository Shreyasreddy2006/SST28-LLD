package com.example.booking;

import com.example.booking.enums.City;
import com.example.booking.exception.SeatNotAvailableException;
import com.example.booking.exception.ShowConflictException;
import com.example.booking.model.*;
import com.example.booking.service.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    public static void main(String[] args) throws InterruptedException {
        TheatreService theatreService = new TheatreService();
        ShowService showService = new ShowService();
        PaymentService paymentService = new PaymentService();
        BookingService bookingService = new BookingService(paymentService);

        Movie inception = new Movie("m1", "Inception", 148);
        Movie darkKnight = new Movie("m2", "The Dark Knight", 152);
        Movie interstellar = new Movie("m3", "Interstellar", 169);

        Screen screen1 = new Screen("scr1", "Screen 1", 10, 5, 2);
        Screen screen2 = new Screen("scr2", "Screen 2", 8, 4, 2);
        Screen screen3 = new Screen("scr3", "Screen 3", 10, 5, 2);

        Theatre pvr = new Theatre("t1", "PVR Orion", City.BANGALORE,
                Arrays.asList(screen1, screen2));
        Theatre inox = new Theatre("t2", "INOX Garuda", City.BANGALORE,
                List.of(screen3));
        Theatre pvrMumbai = new Theatre("t3", "PVR Phoenix", City.MUMBAI,
                List.of(new Screen("scr4", "Screen 1", 10, 5, 2)));

        theatreService.addTheatre(pvr);
        theatreService.addTheatre(inox);
        theatreService.addTheatre(pvrMumbai);

        System.out.println("=== showTheatres(BANGALORE) ===");
        theatreService.showTheatres(City.BANGALORE).forEach(System.out::println);

        System.out.println("\n=== showTheatres(MUMBAI) ===");
        theatreService.showTheatres(City.MUMBAI).forEach(System.out::println);

        LocalDateTime today6pm = LocalDateTime.now().withHour(18).withMinute(0);
        LocalDateTime today9pm = LocalDateTime.now().withHour(21).withMinute(0);

        Show show1 = showService.addShow("s1", inception, screen1, pvr, today6pm);
        Show show2 = showService.addShow("s2", darkKnight, screen2, pvr, today6pm);
        Show show3 = showService.addShow("s3", interstellar, screen3, inox, today6pm);
        showService.addShow("s4", inception, screen1, pvr, today9pm);

        System.out.println("\n=== showMovies(BANGALORE) ===");
        showService.showMovies(City.BANGALORE).forEach(System.out::println);

        System.out.println("\n=== Concurrent show addition (conflict detection) ===");
        ExecutorService adminPool = Executors.newFixedThreadPool(2);
        CountDownLatch adminLatch = new CountDownLatch(2);

        adminPool.submit(() -> {
            try {
                showService.addShow("s5", darkKnight, screen1, pvr,
                        today6pm.plusMinutes(30));
                System.out.println("Admin-1: added show s5 (should NOT happen, conflicts with s1)");
            } catch (ShowConflictException e) {
                System.out.println("Admin-1: blocked - " + e.getMessage());
            } finally {
                adminLatch.countDown();
            }
        });

        adminPool.submit(() -> {
            try {
                Show added = showService.addShow("s6", darkKnight, screen2, pvr, today9pm);
                System.out.println("Admin-2: added show s6 on different screen - " + added);
            } catch (ShowConflictException e) {
                System.out.println("Admin-2: blocked - " + e.getMessage());
            } finally {
                adminLatch.countDown();
            }
        });

        adminLatch.await();
        adminPool.shutdown();

        System.out.println("\n=== bookTickets (normal) ===");
        MovieTicket ticket1 = bookingService.bookTickets(show1, Arrays.asList("A1", "A2", "A3"));
        System.out.println(ticket1);

        System.out.println("\n=== Concurrent booking - two users race for same seat ===");
        ExecutorService bookingPool = Executors.newFixedThreadPool(2);
        CountDownLatch bookingLatch = new CountDownLatch(2);

        bookingPool.submit(() -> {
            try {
                MovieTicket t = bookingService.bookTickets(show2, Arrays.asList("A1", "A2"));
                System.out.println("User-1 booked: " + t.getBookingId()
                        + " seats: A1,A2 total: Rs." + t.getTotalAmount());
            } catch (SeatNotAvailableException e) {
                System.out.println("User-1 failed: " + e.getMessage());
            } finally {
                bookingLatch.countDown();
            }
        });

        bookingPool.submit(() -> {
            try {
                MovieTicket t = bookingService.bookTickets(show2, Arrays.asList("A2", "A3"));
                System.out.println("User-2 booked: " + t.getBookingId()
                        + " seats: A2,A3 total: Rs." + t.getTotalAmount());
            } catch (SeatNotAvailableException e) {
                System.out.println("User-2 failed: " + e.getMessage());
            } finally {
                bookingLatch.countDown();
            }
        });

        bookingLatch.await();
        bookingPool.shutdown();

        System.out.println("\n=== Cancellation with refund ===");
        System.out.println("Before cancel: " + ticket1.getStatus());
        MovieTicket cancelled = bookingService.cancelBooking(ticket1.getBookingId());
        System.out.println("After cancel : " + cancelled.getStatus());
        System.out.println(cancelled);

        System.out.println("\n=== Re-book previously cancelled seats ===");
        MovieTicket rebooked = bookingService.bookTickets(show1, Arrays.asList("A1", "A2"));
        System.out.println(rebooked);

        System.out.println("\n=== Available seats for show3 ===");
        System.out.println("Available: " + show3.getAvailableSeats().size() + " seats");
    }
}
