package com.example.cloud.controller;

import com.amazonaws.services.sqs.model.Message;
import com.example.cloud.domain.Demo;
import com.example.cloud.domain.MessageSns;
import com.google.gson.Gson;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping(value = "/messaging")
public class SqsController {

    private static final String TOPIC = "MyTopic";
    private static final String QUEUE1 = "MyQueue1";
    private static final String QUEUE2 = "MyQueue2";
    private static final String QUEUE3 = "MyQueue3";
    private static final String QUEUEDLQ = "MyQueueDLQ";

    Gson gson = new Gson();

    public static final Logger LOGGER = LoggerFactory.getLogger(SqsController.class);

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    private NotificationMessagingTemplate notificationMessagingTemplate;

    @PostMapping(value = "/sendSQS")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void sendMessageToSqs(@RequestBody final Demo demo) {
        LOGGER.info("Sending the message to the Amazon sqs.");
        queueMessagingTemplate.convertAndSend(QUEUE1, demo);
        LOGGER.info("Message sent successfully to the Amazon sqs.");
    }

    @PostMapping(value = "/sendSNS")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void sendMessageToSns(@RequestBody final Demo demo) {
        LOGGER.info("Sending the message to the Amazon sns.");
        notificationMessagingTemplate.convertAndSend(TOPIC, demo, Map.of("type", "C"));
        LOGGER.info("Message sent successfully to the Amazon sns.");
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> getMessageFromSqs() {
        var result = queueMessagingTemplate.receiveAndConvert(QUEUEDLQ, Demo.class);
        if(result != null) {
            LOGGER.info("Received message queueDlQ = {}", result.getValue());
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.noContent().build();
    }

    @SqsListener(value = QUEUE1, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void getMessageFromSqsg1(Message message) throws Exception {
        throw new Exception("ERROR");
//        var result = gson.fromJson(gson.fromJson(message.getBody(), MessageSns.class).getMessage(), Demo.class) ;
//        LOGGER.info("Received message queue 1 = {}", result.getValue());
    }

    @SqsListener(value = QUEUE2, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void getMessageFromSqsg2(Message message) {
        var result = gson.fromJson(gson.fromJson(message.getBody(), MessageSns.class).getMessage(), Demo.class) ;
        LOGGER.info("Received message queue 2 = {}", result.getValue());
    }

    @SqsListener(value = QUEUE3, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void getMessageFromSqsg3(Message message) throws Exception {
        throw new Exception("ERROR");
    }
}
