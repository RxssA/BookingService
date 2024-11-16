package ie.atu.bookingservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDetails> createBooking(@RequestBody BookingDetails bookingDetails) {
        BookingDetails createdBooking = bookingService.createBooking(bookingDetails);
        return ResponseEntity.ok(createdBooking);
    }
    @GetMapping
    public ResponseEntity<List<BookingDetails>> getAllBookings() {
        List<BookingDetails> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookingDetails> getBookingById(@PathVariable String id) {
        Optional<BookingDetails> bookingDetails = bookingService.getBookingById(id);
        if (bookingDetails.isPresent()) {
            return ResponseEntity.ok(bookingDetails.get());
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDetails>> getBookingByUserId(@PathVariable String userId) {
        List<BookingDetails> bookings = bookingService.getByBookingUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id]")
    public ResponseEntity<BookingDetails> updateBooking(@PathVariable String id, @RequestBody BookingDetails bookingDetails) {
        BookingDetails updatedBooking = bookingService.updateBooking(id, bookingDetails);
        if (updatedBooking != null) {
            return ResponseEntity.ok(updatedBooking);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BookingDetails> deleteBooking(@PathVariable String id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }
}
