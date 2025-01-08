package ie.atu.bookingservice;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BookingEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    public BookingEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishBookingCreatedEvent(BookingDetails bookingDetails) {
        BookingDetailsDTO dto = new BookingDetailsDTO();
        dto.setId(bookingDetails.getId());
        dto.setAmount(bookingDetails.getAmount()); // Include amount

        rabbitTemplate.convertAndSend(exchange, routingKey, dto);
    }

}
