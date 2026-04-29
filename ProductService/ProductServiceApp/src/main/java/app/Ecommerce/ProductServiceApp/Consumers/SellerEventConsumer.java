package app.Ecommerce.ProductServiceApp.Consumers;

import app.Ecommerce.ProductServiceApp.Entity.SellerData;
import app.Ecommerce.ProductServiceApp.Enums.Status;
import app.Ecommerce.ProductServiceApp.Events.SellerApprovedEvent;
import app.Ecommerce.ProductServiceApp.Repository.SellerDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SellerEventConsumer {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SellerDataRepository sellerDataRepository;

    @KafkaListener(topics = "seller-topic", groupId = "product-group")
    public void consume(String message) {
        try {
            SellerApprovedEvent event =
                    objectMapper.readValue(message, SellerApprovedEvent.class);

            System.out.println("Received Seller Approved Event:");
            System.out.println("Seller ID: " + event.getSellerId());
            System.out.println("Status: " + event.getStatus());
            SellerData sellerData = new SellerData();
            sellerData.setSellerId(event.getSellerId());
            sellerData.setStatus(Status.valueOf(event.getStatus()));
            sellerDataRepository.save(sellerData);
            // 👉 Your business logic here

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}