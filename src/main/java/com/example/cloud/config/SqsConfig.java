package com.example.cloud.config;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import io.awspring.cloud.messaging.config.annotation.EnableSqs;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableSqs
public class SqsConfig {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withRegion(region)
                .build();
    }

    @Bean
    public NotificationMessagingTemplate notificationMessagingTemplate() {
        return new NotificationMessagingTemplate(amazonSNSAsync());
    }

    @Bean
    @Primary
    public AmazonSNSAsync amazonSNSAsync() {
        return AmazonSNSAsyncClientBuilder
                .standard()
                .withRegion(region)
                .build();
    }
}
