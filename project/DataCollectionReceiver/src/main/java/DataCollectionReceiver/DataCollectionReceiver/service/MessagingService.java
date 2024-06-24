package DataCollectionReceiver.DataCollectionReceiver.service;

import DataCollectionReceiver.DataCollectionReceiver.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendToPDFGenerator(String message) {
        try {
            System.out.println(message);
            rabbitTemplate.convertAndSend(RabbitMQConfig.PDF_GENERATOR_QUEUE, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
