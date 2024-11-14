package ie.atu.bookingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private BookingService bookingService;
    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public BookingDetails createBooking(BookingDetails bookingDetails) {
        bookingDetails.setStatus("Pending");
        return bookingRepository.save(bookingDetails);
    }

    public Optional<BookingDetails> getBookingById(String id) {
        return bookingRepository.findById(id);
    }

    public List<BookingDetails> getByBookingUserId(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public BookingDetails updateBooking(BookingDetails bookingDetails, BookingDetails updatedBooking) {
        if(bookingRepository.existsById(bookingDetails.getId())) {
            updatedBooking.setId(bookingDetails.getId());
            return bookingRepository.save(updatedBooking);
        }
        return null;
    }

    public void deleteBooking(String id) {
        bookingRepository.deleteById(id);
    }
}
