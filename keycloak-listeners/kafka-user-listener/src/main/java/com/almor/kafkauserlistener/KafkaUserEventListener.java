package com.almor.kafkauserlistener;

import com.almor.kafkauserlistener.message.CreateUserMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;

import java.util.Map;
import java.util.Properties;

public class KafkaUserEventListener implements EventListenerProvider {

    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaUserEventListener() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-broker:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType().equals(EventType.REGISTER)) {
            Map<String, String> details = event.getDetails();
            CreateUserMessage message = CreateUserMessage.builder()
                    .id(event.getUserId())
                    .username(details.get("username"))
                    .email(details.get("email"))
                    .firstName(details.get("first_name"))
                    .lastName(details.get("last_name"))
                    .build();

            try {
                String strMessage = objectMapper.writeValueAsString(message);

                ProducerRecord<String, String> record = new ProducerRecord<>("user.created", strMessage);
                record.headers().add(new RecordHeader("__TypeId__", "createUserMessage".getBytes()));

                producer.send(record);
            } catch (JsonProcessingException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {
        producer.close();
    }
}
