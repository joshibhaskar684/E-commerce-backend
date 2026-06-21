package cart.service.CartServiceApp.Consumers;

import cart.service.CartServiceApp.Entity.ProductsdataSnapshot;
import cart.service.CartServiceApp.Repository.ProductsdataSnapshotRepository;
import cart.service.CartServiceApp.Service.ConsumerHelperForProductEventConsumer;
import cart.service.CartServiceApp.Service.ProductsdataSnapshotService;
import com.ecommerce.commonlib.base_domains.Enums.EventType;
import com.ecommerce.commonlib.base_domains.Event.ProductsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Service;

@Service
public class ProductEventConsumer {
    private ConsumerHelperForProductEventConsumer consumerHelperForProductEventConsumer;
    private static final Logger LOGGER= LoggerFactory.getLogger(ProductEventConsumer.class);

    public ProductEventConsumer(ConsumerHelperForProductEventConsumer consumerHelperForProductEventConsumer) {
        this.consumerHelperForProductEventConsumer = consumerHelperForProductEventConsumer;
    }


    @KafkaListener(topics = "${spring.kafka.topics.products}", groupId = "${spring.kafka.consumer.group-id}")
    @RetryableTopic(attempts = "4")
    public void consume(ProductsEvent event) throws Exception {

        switch (event.getEventType()){
            case CREATE -> consumerHelperForProductEventConsumer.CreateProductDataSnapShot(event);
            case UPDATE -> consumerHelperForProductEventConsumer.UpdateProductDataSnapshot(event);
            case DELETE -> consumerHelperForProductEventConsumer.DeleteProductDataSnapshot(event);
                default -> LOGGER.warn("Unknown event type: {}", event.getEventType());
        }



    }
}
