package cart.service.CartServiceApp.Consumers;

import cart.service.CartServiceApp.Entity.ProductsdataSnapshot;
import cart.service.CartServiceApp.Service.ConsumerHelperForProductEventConsumer;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import com.ecommerce.commonlib.base_domains.Event.ShopApprovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ShopEventConsumerForProduct {

    private static final Logger LOGGER= LoggerFactory.getLogger(ShopEventConsumerForProduct.class);


private ConsumerHelperForProductEventConsumer productsService;

    public ShopEventConsumerForProduct(ConsumerHelperForProductEventConsumer productsService) {
        this.productsService = productsService;
    }

    @KafkaListener(topics = "${spring.kafka.topics.shop}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ShopApprovedEvent event) throws Exception {

LOGGER.info(String.format("seller approved Event Received => %s", event.toString()));


        switch (event.getEventType()) {


            case SUSPEND -> productsService.suspendShopAndChangeStatus(event.getShopId(), Status.APPROVED,Status.INACTIVE);

            default -> LOGGER.warn("Unknown event type: {}", event.getEventType());
        }



    }

}