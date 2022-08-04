package com.example.cloud.controller;

import com.amazonaws.services.sqs.model.Message;
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
@RequestMapping(value = "/sqs")
public class SqsController {

    private static final String QUEUE = "MyQueue";

    public static final Logger LOGGER = LoggerFactory.getLogger(SqsController.class);

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @PostMapping(value = "/send")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void sendMessageToSqs(@RequestBody final Message message) {
        LOGGER.info("Sending the message to the Amazon sqs.");
        queueMessagingTemplate.convertAndSend(QUEUE, message);
        LOGGER.info("Message sent successfully to the Amazon sqs.");
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> getMessageFromSqs(Message message, @Header("MessageId") String messageId) {
        var result = queueMessagingTemplate.receiveAndConvert(QUEUE, Message.class);
        return ResponseEntity.ok(result);
    }

    @SqsListener(value = QUEUE, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void getMessageFromSqsg(Message message) {
        LOGGER.info("Received message= {}", message);
    }
}
