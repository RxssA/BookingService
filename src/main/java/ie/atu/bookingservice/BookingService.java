package ie.atu.bookingservice;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingEventPublisher bookingEventPublisher;

    public BookingService(BookingRepository bookingRepository, BookingEventPublisher bookingEventPublisher) {
        this.bookingRepository = bookingRepository;
        this.bookingEventPublisher = bookingEventPublisher;
    }

    public BookingDetails createBooking(BookingDetails bookingDetails) {
        // Save booking to database
        BookingDetails savedBooking = bookingRepository.save(bookingDetails);

        // Publish event
        BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO();
        bookingDetailsDTO.setId(savedBooking.getId());
        bookingDetailsDTO.setAmount(savedBooking.getAmount());
        bookingDetailsDTO.setNumberOfGuests(savedBooking.getNumberOfGuests());
        bookingDetailsDTO.setRoomType(savedBooking.getRoomType());

        bookingEventPublisher.publishBookingCreatedEvent(bookingDetailsDTO);

        return savedBooking;
    }

    public List<BookingDetails> getAllBookings() {
        return bookingRepository.findAll();
    }

    public BookingDetails updateBooking(String id, BookingDetails bookingDetails) {
        return bookingRepository.findById(id).map(existingBooking -> {
            existingBooking.setRoomType(bookingDetails.getRoomType());
            existingBooking.setNumberOfGuests(bookingDetails.getNumberOfGuests());
            existingBooking.setAmount(bookingDetails.getAmount());
            return bookingRepository.save(existingBooking);
        }).orElse(null);
    }

    public boolean confirmBooking(String id) {
        return bookingRepository.findById(id).map(existingBooking -> {
            existingBooking.setStatus("Confirmed");
            bookingRepository.save(existingBooking);
            return true;
        }).orElse(false);
    }

    public void deleteBooking(String id) {
        bookingRepository.deleteById(id);
    }
}
