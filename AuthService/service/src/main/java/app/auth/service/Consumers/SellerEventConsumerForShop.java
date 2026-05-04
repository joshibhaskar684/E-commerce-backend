package app.auth.service.Consumers;

import app.auth.service.Repository.ShopRepository;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import com.ecommerce.commonlib.base_domains.Event.ChangeShopStatusWithSeller;
import com.ecommerce.commonlib.base_domains.Event.SellerApprovedEvent;
import com.ecommerce.commonlib.base_domains.Event.ShopApprovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SellerEventConsumerForShop {

    private static final Logger LOGGER= LoggerFactory.getLogger(SellerEventConsumerForShop.class);

private ShopRepository shopRepository;

    public SellerEventConsumerForShop(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @KafkaListener(topics = "${spring.kafka.topics.seller}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(SellerApprovedEvent event) throws Exception {

LOGGER.info(String.format("seller  Event Received for shop => %s", event.toString()));


        switch (event.getEventType()) {



            case SUSPEND -> shopRepository.setStatusSuspendedWhereSellerIdAndStatus(
                    event.getSellerId(),
                    Status.APPROVED,
                    Status.SUSPENDED
            );

            default -> LOGGER.warn("Unknown event type: {}", event.getEventType());
        }



    }
}