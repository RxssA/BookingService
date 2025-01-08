package ie.atu.bookingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BookingEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingKey}")
    private String routingKey;

    public BookingEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishBookingCreatedEvent(BookingDetailsDTO bookingDetailsDTO) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(bookingDetailsDTO);
            System.out.println("Publishing JSON payload: " + json);
            rabbitTemplate.convertAndSend(exchange, routingKey, bookingDetailsDTO);
        } catch (Exception e) {
            System.err.println("Failed to serialize message: " + e.getMessage());
        }
    }
}
