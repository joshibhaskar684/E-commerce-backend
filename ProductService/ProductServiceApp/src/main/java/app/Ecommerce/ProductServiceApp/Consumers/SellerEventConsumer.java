package app.Ecommerce.ProductServiceApp.Consumers;

import app.Ecommerce.ProductServiceApp.Entity.SellerData;
import app.Ecommerce.ProductServiceApp.Enums.Status;
import app.Ecommerce.ProductServiceApp.Events.SellerApprovedEvent;
import app.Ecommerce.ProductServiceApp.Repository.SellerDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SellerEventConsumer {

    private final SellerDataRepository sellerDataRepository;
    private final ObjectMapper objectMapper;

    public SellerEventConsumer(SellerDataRepository sellerDataRepository,
                               ObjectMapper objectMapper) {
        this.sellerDataRepository = sellerDataRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "seller-events", groupId = "product-group")
    public void consume(String message) throws Exception {

        SellerApprovedEvent event =
                objectMapper.readValue(message, SellerApprovedEvent.class);

        System.out.println("Seller ID: " + event.getSellerId());

        SellerData data = new SellerData();
        data.setSellerId(event.getSellerId());
        data.setStatus(Status.APPROVED);

        sellerDataRepository.save(data);

        System.out.println("Seller data saved in product DB");
    }
}