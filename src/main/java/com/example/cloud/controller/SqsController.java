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
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/messaging")
public class SqsController {

    private static final String QUEUE = "MyQueue";
    private static final String TOPIC = "MyTopic";

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
        queueMessagingTemplate.convertAndSend(QUEUE, demo);
        LOGGER.info("Message sent successfully to the Amazon sqs.");
    }

    @PostMapping(value = "/sendSNS")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void sendMessageToSns(@RequestBody final Demo demo) {
        LOGGER.info("Sending the message to the Amazon sns.");
        notificationMessagingTemplate.convertAndSend(TOPIC, demo);
        LOGGER.info("Message sent successfully to the Amazon sns.");
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> getMessageFromSqs(Message message, @Header("MessageId") String messageId) {
        var result = queueMessagingTemplate.receiveAndConvert(QUEUE, Message.class);
        return ResponseEntity.ok(result);
    }

    @SqsListener(value = QUEUE, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void getMessageFromSqsg(Message message) {
        var result = gson.fromJson(gson.fromJson(message.getBody(), MessageSns.class).getMessage(), Demo.class) ;
        LOGGER.info("Received message = {}", result.getValue());
    }
}
