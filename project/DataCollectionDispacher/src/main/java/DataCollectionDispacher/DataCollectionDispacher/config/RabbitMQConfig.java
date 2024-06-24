package DataCollectionDispacher.DataCollectionDispacher.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String APP_DISPATCHER_QUEUE = "app-dispatcher-queue";
    public static final String STATION_DATA_QUEUE = "stationDataQueue";
    public static final String DATA_RECEIVER_QUEUE = "dataReceiverQueue";

    @Bean
    public Queue appDispatcherQueue() {
        return new Queue(APP_DISPATCHER_QUEUE, false);
    }

    @Bean
    public Queue stationDataQueue() {
        return new Queue(STATION_DATA_QUEUE, false);
    }

    @Bean
    public Queue dataReceiverQueue() {
        return new Queue(DATA_RECEIVER_QUEUE, false);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
