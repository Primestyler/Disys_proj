package StationDataCollector.StationDataCollector.config;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String DATA_COLLECTION_QUEUE = "dataCollectionQueue";
    public static final String STATION_DATA_QUEUE = "stationDataQueue";

    @Bean
    public Queue dataCollectionQueue() {
        return new Queue(DATA_COLLECTION_QUEUE, false);
    }

    @Bean
    public Queue stationDataQueue() {
        return new Queue(STATION_DATA_QUEUE, false);
    }
}
