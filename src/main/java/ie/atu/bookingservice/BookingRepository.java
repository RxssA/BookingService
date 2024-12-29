package ie.atu.bookingservice;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<BookingDetails, String> {
    List<BookingDetails> findByUserId(String userId);
    List<BookingDetails> findByStatus(String status);
}
