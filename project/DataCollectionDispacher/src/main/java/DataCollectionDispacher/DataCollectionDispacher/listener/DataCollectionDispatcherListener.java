package DataCollectionDispacher.DataCollectionDispacher.listener;

import DataCollectionDispacher.DataCollectionDispacher.service.DataCollectionDispatcherService;
import DataCollectionDispacher.DataCollectionDispacher.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DataCollectionDispatcherListener {
    private final DataCollectionDispatcherService dispatcherService;

    public DataCollectionDispatcherListener(DataCollectionDispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @RabbitListener(queues = RabbitMQConfig.APP_DISPATCHER_QUEUE)
    public void handleMessage(String customerId) {
        // Start the data gathering job when a message is received
        dispatcherService.startDataGatheringJob(customerId);
    }
}
