package DataCollectionDispacher.DataCollectionDispacher;

import DataCollectionDispacher.DataCollectionDispacher.listener.DataCollectionDispatcherListener;
import DataCollectionDispacher.DataCollectionDispacher.service.DataCollectionDispatcherService;
import DataCollectionDispacher.DataCollectionDispacher.config.RabbitMQConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static org.mockito.Mockito.verify;

class DataCollectionDispatcherListenerTest {

    @Mock
    private DataCollectionDispatcherService dispatcherService;

    @InjectMocks
    private DataCollectionDispatcherListener listener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @RabbitListener(queues = RabbitMQConfig.APP_DISPATCHER_QUEUE)
    void testHandleMessage() {
        String customerId = "12345";
        listener.handleMessage(customerId);
        verify(dispatcherService).startDataGatheringJob(customerId);
    }
}
