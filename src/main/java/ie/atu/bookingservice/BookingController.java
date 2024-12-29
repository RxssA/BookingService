package ie.atu.bookingservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Endpoint for creating a new booking
    @PostMapping("/createBooking")
    public ResponseEntity<BookingDetails> createBooking(@RequestBody BookingDetails bookingDetails) {
        BookingDetails createdBooking = bookingService.createBooking(bookingDetails);
        return ResponseEntity.ok(createdBooking);
    }

    // Endpoint to get all available bookings (those that are not yet booked)
    @GetMapping("/available")
    public ResponseEntity<List<BookingDetails>> getAvailableBookings() {
        List<BookingDetails> availableBookings = bookingService.getAvailableBookings();
        return ResponseEntity.ok(availableBookings);
    }

    // Endpoint to get all bookings
    @GetMapping
    public ResponseEntity<List<BookingDetails>> getAllBookings() {
        List<BookingDetails> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Endpoint to get a booking by its ID
    @GetMapping("getbooking/{id}")
    public ResponseEntity<BookingDetails> getBookingById(@PathVariable String id) {
        Optional<BookingDetails> bookingDetails = bookingService.getBookingById(id);
        return bookingDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to get bookings by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDetails>> getBookingByUserId(@PathVariable String userId) {
        List<BookingDetails> bookings = bookingService.getByBookingUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    // Endpoint for updating a booking
    @PutMapping("update/{id}")
    public ResponseEntity<BookingDetails> updateBooking(@PathVariable String id, @RequestBody BookingDetails bookingDetails) {
        BookingDetails updatedBooking = bookingService.updateBooking(id, bookingDetails);
        if (updatedBooking != null) {
            return ResponseEntity.ok(updatedBooking);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint for deleting a booking
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable String id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }
}
