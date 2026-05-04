package app.Ecommerce.ProductServiceApp.Consumers;

import app.Ecommerce.ProductServiceApp.Service.SellerDataService;
import com.ecommerce.commonlib.base_domains.Event.SellerApprovedEvent;
import com.ecommerce.commonlib.base_domains.Event.ShopApprovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ShopEventConsumer {

    private static final Logger LOGGER= LoggerFactory.getLogger(ShopEventConsumer.class);

private SellerDataService sellerDataService;

    public ShopEventConsumer(SellerDataService sellerDataService) {
        this.sellerDataService = sellerDataService;
    }

    @KafkaListener(topics = "${spring.kafka.topics.shop}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ShopApprovedEvent event) throws Exception {

LOGGER.info(String.format("shop Event Received => %s", event.toString()));


        switch (event.getEventType()) {

            case APPROVE -> sellerDataService.handleApproveShop(event);

            case REJECT -> sellerDataService.handleRejectShop(event);

            case SUSPEND -> sellerDataService.handleSuspendShop(event);

            default -> LOGGER.warn("Unknown event type: {}", event.getEventType());
        }



    }
}