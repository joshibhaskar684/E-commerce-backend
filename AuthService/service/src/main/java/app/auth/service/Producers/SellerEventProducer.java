package app.auth.service.Producers;

import app.auth.service.Events.SellerApprovedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SellerEventProducer {

    private KafkaTemplate<String, SellerApprovedEvent> kafkaTemplate;

    public SellerEventProducer(KafkaTemplate<String, SellerApprovedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSellerApprovedEvent(SellerApprovedEvent event) {
        kafkaTemplate.send("seller-events", event);
    }
}