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
        bookingDetails.setStatus("AVAILABLE"); // Default status
        return bookingRepository.save(bookingDetails);
    }

    public List<BookingDetails> getAllBookings() {
        return bookingRepository.findAll();
    }

    public BookingDetails updateBooking(String id, BookingDetails updatedBooking) {
        Optional<BookingDetails> existingBooking = bookingRepository.findById(id);
        if (existingBooking.isPresent()) {
            BookingDetails booking = existingBooking.get();
            booking.setServiceId(updatedBooking.getServiceId());
            booking.setServiceType(updatedBooking.getServiceType());
            booking.setStatus(updatedBooking.getStatus());
            booking.setAmount(updatedBooking.getAmount());
            return bookingRepository.save(booking);
        }
        return null;
    }

    public boolean confirmBooking(String id) {
        Optional<BookingDetails> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            BookingDetails booking = bookingOptional.get();
            if ("AVAILABLE".equals(booking.getStatus())) {
                booking.setStatus("CONFIRMED");
                bookingRepository.save(booking);
                return true;
            }
        }
        return false;
    }

    public void deleteBooking(String id) {
        bookingRepository.deleteById(id);
    }
}
