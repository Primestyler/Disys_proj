package DataCollectionDispacher.DataCollectionDispacher.service;

import DataCollectionDispacher.DataCollectionDispacher.config.RabbitMQConfig;
import DataCollectionDispacher.DataCollectionDispacher.model.Station;
import DataCollectionDispacher.DataCollectionDispacher.model.StationMessage;
import DataCollectionDispacher.DataCollectionDispacher.model.Notification;
import DataCollectionDispacher.DataCollectionDispacher.repository.StationRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataCollectionDispatcherService {
    private final RabbitTemplate rabbitTemplate;
    private final StationRepository stationRepository;

    public DataCollectionDispatcherService(RabbitTemplate rabbitTemplate, StationRepository stationRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.stationRepository = stationRepository;
    }

    public void startDataGatheringJob(String customerId) {
        // Retrieve all stations from the database
        List<Station> stations = stationRepository.findAll();

        // Send a message for every charging station to the Station Data Collector
        for (Station station : stations) {
            StationMessage message = new StationMessage(customerId, station.getId(), station.getDbUrl());
            System.out.println("Sending message to Station Data Queue: " + message); // Print message before sending
            rabbitTemplate.convertAndSend(RabbitMQConfig.STATION_DATA_QUEUE, message);
        }

        // Notify the Data Collection Receiver that a new job started
        Notification notification = new Notification("Customer: " + customerId);
        System.out.println("Sending notification to Data Receiver Queue: " + notification); // Print notification before sending
        rabbitTemplate.convertAndSend(RabbitMQConfig.DATA_RECEIVER_QUEUE, notification);
    }
}
