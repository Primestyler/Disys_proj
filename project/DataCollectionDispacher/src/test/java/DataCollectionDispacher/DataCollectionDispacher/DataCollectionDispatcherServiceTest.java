package DataCollectionDispacher.DataCollectionDispacher;

import DataCollectionDispacher.DataCollectionDispacher.config.RabbitMQConfig;
import DataCollectionDispacher.DataCollectionDispacher.model.Station;
import DataCollectionDispacher.DataCollectionDispacher.model.StationMessage;
import DataCollectionDispacher.DataCollectionDispacher.model.Notification;
import DataCollectionDispacher.DataCollectionDispacher.repository.StationRepository;
import DataCollectionDispacher.DataCollectionDispacher.service.DataCollectionDispatcherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DataCollectionDispatcherServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private DataCollectionDispatcherService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartDataGatheringJob() {
        String customerId = "12345";

        Station station1 = new Station();
        station1.setId(1L);
        station1.setDbUrl("dbUrl1");

        Station station2 = new Station();
        station2.setId(2L);
        station2.setDbUrl("dbUrl2");

        List<Station> stations = Arrays.asList(station1, station2);

        when(stationRepository.findAll()).thenReturn(stations);

        service.startDataGatheringJob(customerId);

        // Verify convertAndSend is called twice for STATION_DATA_QUEUE
        verify(rabbitTemplate, times(2)).convertAndSend(eq(RabbitMQConfig.STATION_DATA_QUEUE), any(StationMessage.class));

        // Verify convertAndSend is called once for DATA_RECEIVER_QUEUE
        verify(rabbitTemplate, times(1)).convertAndSend(eq(RabbitMQConfig.DATA_RECEIVER_QUEUE), any(Notification.class));
    }
}
