package ie.atu.bookingservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/createBooking")
    public ResponseEntity<BookingDetails> createBooking(@RequestBody BookingDetails bookingDetails) {
        // Ensure required fields are populated
        if (bookingDetails.getRoomType() == null || bookingDetails.getRoomType().isEmpty()) {
            bookingDetails.setRoomType("Standard"); // Default room type
        }
        if (bookingDetails.getNumberOfGuests() == 0) {
            bookingDetails.setNumberOfGuests(1); // Default to 1 guest
        }

        BookingDetails createdBooking = bookingService.createBooking(bookingDetails);
        return ResponseEntity.ok(createdBooking);
    }

    @GetMapping
    public ResponseEntity<List<BookingDetails>> getAllBookings() {
        List<BookingDetails> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<BookingDetails> updateBooking(@PathVariable String id, @RequestBody BookingDetails bookingDetails) {
        BookingDetails updatedBooking = bookingService.updateBooking(id, bookingDetails);
        if (updatedBooking != null) {
            return ResponseEntity.ok(updatedBooking);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/confirmBooking")
    public ResponseEntity<String> confirmBooking(@RequestBody BookingDetails bookingDetails) {
        String authToken = bookingDetails.getToken();

        if (authToken == null || authToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Auth token is required.");
        }

        boolean isConfirmed = bookingService.confirmBooking(bookingDetails.getId());

        if (isConfirmed) {
            return ResponseEntity.ok("Booking confirmed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Booking could not be confirmed.");
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable String id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }
}
