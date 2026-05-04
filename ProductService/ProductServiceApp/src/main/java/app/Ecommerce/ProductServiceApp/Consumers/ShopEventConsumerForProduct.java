package app.Ecommerce.ProductServiceApp.Consumers;

import app.Ecommerce.ProductServiceApp.Service.ProductsService;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import com.ecommerce.commonlib.base_domains.Event.ChangeProductStatusWithSeller;
import com.ecommerce.commonlib.base_domains.Event.ChangeProductStatusWithShop;
import com.ecommerce.commonlib.base_domains.Event.ShopApprovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ShopEventConsumerForProduct {

    private static final Logger LOGGER= LoggerFactory.getLogger(ShopEventConsumerForProduct.class);


private ProductsService productsService;

    public ShopEventConsumerForProduct(ProductsService productsService) {
        this.productsService = productsService;
    }

    @KafkaListener(topics = "${spring.kafka.topics.shop}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ShopApprovedEvent event) throws Exception {

LOGGER.info(String.format("seller approved Event Received => %s", event.toString()));


        switch (event.getEventType()) {


            case SUSPEND -> productsService.updateAllProductsBySeller(event.getShopId(), Status.APPROVED,Status.INACTIVE);

            default -> LOGGER.warn("Unknown event type: {}", event.getEventType());
        }



    }

}