package app.auth.service.Producers;

import app.auth.service.Events.SellerApprovedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.impl.io.AbstractMessageWriter;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.shaded.com.google.protobuf.MessageOrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import org.springframework.messaging.Message;
import  org.springframework.messaging.support.MessageBuilder;
@Service
public class SellerEventProducer {

    private  static final Logger LOGGER= LoggerFactory.getLogger(SellerEventProducer.class);
    private NewTopic topic;
    private final KafkaTemplate<String, SellerApprovedEvent> kafkaTemplate;
//    private final ObjectMapper objectMapper;


    public SellerEventProducer(NewTopic topic, KafkaTemplate<String, SellerApprovedEvent> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(SellerApprovedEvent event) throws Exception {

       LOGGER.info(String.format("Seller Approved Event => %s", event.toString()));


        Message<SellerApprovedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();

        kafkaTemplate.send(message);

    }
}