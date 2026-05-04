package cart.service.CartServiceApp.Consumers;

import cart.service.CartServiceApp.Service.CartService;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import com.ecommerce.commonlib.base_domains.Event.CreateCartEvent;
import com.ecommerce.commonlib.base_domains.Event.SellerApprovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class CartConsumer {
    private CartService cartService;

    private static final Logger LOGGER= LoggerFactory.getLogger(CartConsumer.class);

    public CartConsumer(CartService cartService) {
        this.cartService = cartService;
    }
    @RetryableTopic(attempts = "4")
    @KafkaListener(topics = "${spring.kafka.topics.cart}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(CreateCartEvent event) throws Exception {
LOGGER.info(String.format("seller approved Event Received => %s", event.toString()));

     cartService.createCart(event.getUserId());

        System.out.println("Seller data saved in product DB");
    }

    @DltHandler
    public void listenDLT(
            CreateCartEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.OFFSET) Long offset) {

        LOGGER.info("DLT Event Received => event: {}, topic: {}, offset: {}",
                event, topic, offset);

        // optional: safe reprocessing OR logging only
        try {
            cartService.createCart(event.getUserId());
        } catch (Exception e) {
            LOGGER.error("Failed again while processing DLT event", e);
        }

    }
}
