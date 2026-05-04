package app.Ecommerce.ProductServiceApp.Consumers;

import app.Ecommerce.ProductServiceApp.Repository.ProductsRepository;
import app.Ecommerce.ProductServiceApp.Service.ProductsService;
import app.Ecommerce.ProductServiceApp.Service.SellerDataService;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import com.ecommerce.commonlib.base_domains.Event.ChangeProductStatusWithSeller;
import com.ecommerce.commonlib.base_domains.Event.SellerApprovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SellerEventConsumerForProduct {

    private static final Logger LOGGER= LoggerFactory.getLogger(SellerEventConsumerForProduct.class);


private ProductsService productsService;

    public SellerEventConsumerForProduct(ProductsService productsService) {
        this.productsService = productsService;
    }

    @KafkaListener(topics = "${spring.kafka.topics.seller}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(SellerApprovedEvent event) throws Exception {

LOGGER.info(String.format("seller approved Event Received => %s", event.toString()));


        switch (event.getEventType()) {


            case SUSPEND -> productsService.updateAllProductsBySeller(event.getSellerId(), Status.ACTIVE,Status.INACTIVE);

            default -> LOGGER.warn("Unknown event type: {}", event.getEventType());
        }



    }

}