package app.Ecommerce.ProductServiceApp.Consumers;

import app.Ecommerce.ProductServiceApp.Entity.SellerData;
import app.Ecommerce.ProductServiceApp.Enums.Status;
import app.Ecommerce.ProductServiceApp.Events.SellerApprovedEvent;
import app.Ecommerce.ProductServiceApp.Repository.SellerDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SellerEventConsumer {

    private SellerDataRepository sellerDataRepository;

    public SellerEventConsumer(SellerDataRepository sellerDataRepository) {
        this.sellerDataRepository = sellerDataRepository;
    }

    @KafkaListener(topics = "seller-events", groupId = "product-group")
    public void consume(SellerApprovedEvent event) {

        SellerData data = new SellerData();
        data.setSellerId(event.getSellerId());
        data.setStatus(Status.APPROVED);

        sellerDataRepository.save(data);

        System.out.println("Seller data saved in product DB");
    }
}