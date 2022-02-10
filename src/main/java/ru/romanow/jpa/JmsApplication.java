package ru.romanow.jpa;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.slf4j.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.slf4j.LoggerFactory.getLogger;

@SpringBootApplication
public class JmsApplication {
    private static final Logger logger = getLogger(JmsApplication.class);
    public static final String QUEUE_NAME = "my-queue";
    public static final int TIMEOUT = 5000;

    public static void main(String[] args) {
        SpringApplication.run(JmsApplication.class, args);
    }

//    @JmsListener(destination = QUEUE_NAME)
//    public void receiver(String content) {
//        logger.info("Incoming message from queue [{}]: [{}]", QUEUE_NAME, content);
//    }

    @Bean
    public ActiveMQQueue topic() {
        return ActiveMQQueue.createQueue(QUEUE_NAME);
    }

    @Bean
    public ApplicationRunner sender(ActiveMQQueue queue, JmsTemplate jmsTemplate) {
        return args -> {
            while (true) {
                logger.info("Send message to queue [{}]", QUEUE_NAME);
                jmsTemplate.send(queue, session -> session.createTextMessage("Hello queue world " + now().format(ISO_LOCAL_DATE_TIME)));
                Thread.sleep(TIMEOUT);
            }
        };
    }
}
