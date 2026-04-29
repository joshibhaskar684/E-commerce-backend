package app.auth.service.Producers;

import app.auth.service.Events.SellerApprovedEvent;
import com.fasterxml.jackson.databind.ObjectMapper; // ✅ FIXED
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SellerEventProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendSellerApprovedEvent(SellerApprovedEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("seller-topic", json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}