package cart.service.CartServiceApp.Consumers;

import com.ecommerce.commonlib.base_domains.Enums.Status;
import com.ecommerce.commonlib.base_domains.Event.SellerApprovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SellerEventConsumer {
//
//    private static final Logger LOGGER= LoggerFactory.getLogger(SellerEventConsumer.class);
//
//
//    private final SellerDataRepository sellerDataRepository;
//
//    public SellerEventConsumer(SellerDataRepository sellerDataRepository) {
//        this.sellerDataRepository = sellerDataRepository;
//    }
//
//    @KafkaListener(topics = "${spring.kafka.topics.seller}", groupId = "${spring.kafka.consumer.group-id}")
//    public void consume(SellerApprovedEvent event) throws Exception {
//LOGGER.info(String.format("seller approved Event Received => %s", event.toString()));
//
//        System.out.println("Seller ID: " + event.getSellerId());
//
//        SellerData data = new SellerData();
//        data.setSellerId(event.getSellerId());
//        data.setStatus(Status.APPROVED);
//
//        sellerDataRepository.save(data);
//
//        System.out.println("Seller data saved in product DB");
//    }

}