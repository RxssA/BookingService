package ie.atu.bookingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingEventPublisher bookingEventPublisher;

    @Autowired
    public BookingService(BookingRepository bookingRepository, BookingEventPublisher bookingEventPublisher) {
        this.bookingRepository = bookingRepository;
        this.bookingEventPublisher = bookingEventPublisher;
    }

    public BookingDetails createBooking(BookingDetails bookingDetails) {
        bookingDetails.setStatus("AVAILABLE"); // Default status
        BookingDetails savedBooking = bookingRepository.save(bookingDetails);

        // Publish event to RabbitMQ
        BookingDetailsDTO dto = new BookingDetailsDTO();
        dto.setId(savedBooking.getId());
        dto.setAmount(savedBooking.getAmount());
        bookingEventPublisher.publishBookingCreatedEvent(dto); // Pass the DTO

        return savedBooking;
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
