package SpringBootApp.SpringBootApp.config;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DATA_COLLECTION_QUEUE = "dataCollectionQueue";
    public static final String STATION_DATA_QUEUE = "stationDataQueue";
    public static final String DATA_RECEIVER_QUEUE = "dataReceiverQueue";
    public static final String PDF_GENERATOR_QUEUE = "pdfGeneratorQueue";
    public static final String APP_DISPATCHER_QUEUE = "app-dispatcher-queue";

    @Bean
    public Queue dataCollectionQueue() {
        return new Queue(DATA_COLLECTION_QUEUE, false);
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
    public Queue pdfGeneratorQueue() {
        return new Queue(PDF_GENERATOR_QUEUE, false);
    }
    @Bean
    public Queue appDispatcherQueue() {
        return new Queue(APP_DISPATCHER_QUEUE, false);
    }
}
