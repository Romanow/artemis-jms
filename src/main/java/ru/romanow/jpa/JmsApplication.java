package ru.romanow.jpa;

import jakarta.jms.TextMessage;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.jms.core.JmsTemplate;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.StringUtils.hasLength;
import static ru.romanow.jpa.HelpPrinter.HELP;
import static ru.romanow.jpa.HelpPrinter.print;

@SpringBootApplication
@EnableConfigurationProperties(JmsApplication.MessagingProperties.class)
public class JmsApplication {
    private static final Logger logger = getLogger(JmsApplication.class);
    private static final int TIMEOUT = 5000;

    public static void main(String[] args) {
        final SimpleCommandLinePropertySource commandLineArgs =
                new SimpleCommandLinePropertySource(args);
        if (commandLineArgs.containsProperty(HELP)) {
            print();
            System.exit(0);
        }

        SpringApplication.run(JmsApplication.class, args);
    }

//    @JmsListener(destination = "my-queue")
//    public void receiver(String content) {
//        logger.info("Incoming message from queue [{}]: [{}]", QUEUE_NAME, content);
//    }

    @Bean
    public ActiveMQQueue topic(MessagingProperties properties) {
        return ActiveMQQueue.createQueue(properties.queueName);
    }

    @Bean
    public ApplicationRunner sender(MessagingProperties messagingProperties, ActiveMQQueue queue, JmsTemplate jmsTemplate) {
        return args -> {
            while (true) {
                logger.info("Send message to queue [{}]", messagingProperties.queueName);
                final String text = getMessage(messagingProperties);
                jmsTemplate.send(queue, session -> {
                    final TextMessage message = session.createTextMessage(text);
                    final Map<String, String> properties = messagingProperties.getProperties();
                    if (properties != null && !properties.isEmpty()) {
                        properties.forEach((name, value) -> setProperty(message, name, value));
                    }
                    return message;
                });
                Thread.sleep(TIMEOUT);
            }
        };
    }

    @NotNull
    private String getMessage(MessagingProperties properties) {
        return hasLength(properties.message)
                ? properties.message
                : "Hello queue world " + now().format(ISO_LOCAL_DATE_TIME);
    }

    @SneakyThrows
    private void setProperty(TextMessage msg, String name, String value) {
        msg.setObjectProperty(name, value);
    }

    @Data
    @ConfigurationProperties(prefix = "messaging")
    public static class MessagingProperties {
        private String queueName;
        private String message;
        private Map<String, String> properties;
    }
}
