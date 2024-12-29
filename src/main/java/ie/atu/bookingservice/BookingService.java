package ie.atu.bookingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public BookingDetails createBooking(BookingDetails bookingDetails) {
        bookingDetails.setStatus("AVAILABLE");
        return bookingRepository.save(bookingDetails);
    }

    public Optional<BookingDetails> getBookingById(String id) {
        return bookingRepository.findById(id);
    }

    public List<BookingDetails> getByBookingUserId(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public BookingDetails updateBooking(String id, BookingDetails updatedBooking) {
        if (updatedBooking != null && bookingRepository.existsById(id)) {
            updatedBooking.setId(id);
            return bookingRepository.save(updatedBooking);
        }
        return null;
    }

    public void deleteBooking(String id) {
        bookingRepository.deleteById(id);
    }

    public List<BookingDetails> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<BookingDetails> getAvailableBookings() {
        return bookingRepository.findByStatus("AVAILABLE");
    }

}
