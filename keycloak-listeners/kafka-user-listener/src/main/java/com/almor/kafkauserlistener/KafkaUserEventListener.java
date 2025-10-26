package com.almor.kafkauserlistener;

import com.almor.kafkauserlistener.message.CreateUserMessage;
import com.almor.kafkauserlistener.message.DeleteUserMessage;
import com.almor.kafkauserlistener.message.UpdateUserMessage;
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
import org.keycloak.events.admin.OperationType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.Map;
import java.util.Properties;

public class KafkaUserEventListener implements EventListenerProvider {

    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KeycloakSession session;

    public KafkaUserEventListener(KeycloakSession keycloakSession) {
        this.session = keycloakSession;

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-broker:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
    }

    private void sendMessage(Object message) {
        String strMessage;
        String topic;
        String typeId;

        try {
            strMessage = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
            return;
        }

        if (message instanceof CreateUserMessage) {
            topic = "user.created";
            typeId = "createUserMessage";
        } else if (message instanceof UpdateUserMessage) {
            topic = "user.updated";
            typeId = "updateUserMessage";
        } else if (message instanceof DeleteUserMessage) {
            topic = "user.deleted";
            typeId = "deleteUserMessage";
        } else {
            return;
        }

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, strMessage);
        record.headers().add(new RecordHeader("__TypeId__", typeId.getBytes()));
        producer.send(record);
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType().equals(EventType.REGISTER)) {
            RealmModel realm = session.realms().getRealm(event.getRealmId());
            UserModel user = session.users().getUserById(realm, event.getUserId());
            long createdAt = user.getCreatedTimestamp();

            Map<String, String> details = event.getDetails();
            CreateUserMessage message = CreateUserMessage.builder()
                    .id(event.getUserId())
                    .username(details.get("username"))
                    .email(details.get("email"))
                    .firstName(details.get("first_name"))
                    .lastName(details.get("last_name"))
                    .createdAt(createdAt)
                    .build();

            sendMessage(message);
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        if ("USER".equalsIgnoreCase(adminEvent.getResourceTypeAsString()) &&
                adminEvent.getOperationType().equals(OperationType.CREATE)) {

            Map<String, Object> representation = null;
            try {
                representation = objectMapper.readValue(adminEvent.getRepresentation(), Map.class);
            } catch (JsonProcessingException e) {
                System.err.println(e.getMessage());
                return;
            }

            if (representation != null) {
                String userId = adminEvent.getResourcePath().replace("users/", "");
                RealmModel realm = session.realms().getRealm(adminEvent.getRealmId());
                UserModel user = session.users().getUserById(realm, userId);
                long createdAt = user.getCreatedTimestamp();

                CreateUserMessage message = CreateUserMessage.builder()
                        .id(userId)
                        .username((String) representation.get("username"))
                        .email((String) representation.get("email"))
                        .firstName((String) representation.get("firstName"))
                        .lastName((String) representation.get("lastName"))
                        .createdAt(createdAt)
                        .build();

                sendMessage(message);
            }
        } else if ("USER".equalsIgnoreCase(adminEvent.getResourceTypeAsString()) &&
                adminEvent.getOperationType().equals(OperationType.UPDATE)) {

            Map<String, Object> representation = null;
            try {
                representation = objectMapper.readValue(adminEvent.getRepresentation(), Map.class);
            } catch (JsonProcessingException e) {
                System.err.println(e.getMessage());
                return;
            }

            if (representation != null) {
                String userId = adminEvent.getResourcePath().replace("users/", "");

                UpdateUserMessage message = UpdateUserMessage.builder()
                        .id(userId)
                        .email((String) representation.get("email"))
                        .firstName((String) representation.get("firstName"))
                        .lastName((String) representation.get("lastName"))
                        .enable((Boolean) representation.get("enabled"))
                        .build();

                sendMessage(message);
            }
        } else if ("USER".equalsIgnoreCase(adminEvent.getResourceTypeAsString()) &&
                adminEvent.getOperationType().equals(OperationType.DELETE)) {
            String userId = adminEvent.getResourcePath().replace("users/", "");

            DeleteUserMessage message = DeleteUserMessage.builder()
                    .id(userId)
                    .build();

            sendMessage(message);
        }
    }

    @Override
    public void close() {
        producer.close();
    }
}
