package ie.atu.bookingservice;

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
        rabbitTemplate.convertAndSend(exchange, routingKey, bookingDetailsDTO);
    }
}
