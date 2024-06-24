package DataCollectionReceiver.DataCollectionReceiver;

import DataCollectionReceiver.DataCollectionReceiver.config.RabbitMQConfig;
import DataCollectionReceiver.DataCollectionReceiver.entity.Charge;
import DataCollectionReceiver.DataCollectionReceiver.entity.ChargeData;
import DataCollectionReceiver.DataCollectionReceiver.listener.DataCollectionListener;
import DataCollectionReceiver.DataCollectionReceiver.service.MessagingService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DataCollectionListenerTest {

    @Mock
    private MessagingService messagingService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private DataCollectionListener dataCollectionListener;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReceiveDataMessage_SingleCustomer() {
        // Mock incoming messages
        String message1 = "{\"dbUrl\":\"jdbc:mysql://localhost:3306/test\",\"charges\":[{\"id\":1,\"kwh\":100.0,\"customerId\":123}]}";
        String message2 = "{\"dbUrl\":\"jdbc:mysql://localhost:3306/test\",\"charges\":[{\"id\":2,\"kwh\":150.0,\"customerId\":123}]}";
        String message3 = "{\"dbUrl\":\"jdbc:mysql://localhost:3306/test\",\"charges\":[{\"id\":3,\"kwh\":200.0,\"customerId\":123}]}";

        // Mock dependencies
        Charge charge1 = new Charge(1L, 100.0, 123L);
        Charge charge2 = new Charge(2L, 150.0, 123L);
        Charge charge3 = new Charge(3L, 200.0, 123L);
        ChargeData chargeData1 = new ChargeData(List.of(charge1), "jdbc:mysql://localhost:3306/test");
        ChargeData chargeData2 = new ChargeData(List.of(charge2), "jdbc:mysql://localhost:3306/test");
        ChargeData chargeData3 = new ChargeData(List.of(charge3), "jdbc:mysql://localhost:3306/test");

        // Mock RabbitTemplate behavior
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString());

        // Test method
        dataCollectionListener.receiveDataMessage(message1);
        dataCollectionListener.receiveDataMessage(message2);
        dataCollectionListener.receiveDataMessage(message3);

        // Verify messagingService.sendToPDFGenerator() is called once with the expected JSON string
        verify(messagingService, times(1)).sendToPDFGenerator(anyString());

        // Optionally, you can verify the content of the JSON string sent to sendToPDFGenerator
        verify(messagingService).sendToPDFGenerator(argThat(json -> {
            JSONObject expectedJson = new JSONObject();
            expectedJson.put("jdbc:mysql://localhost:3306/test", new JSONArray()
                    .put(new JSONObject().put("id", 1).put("customerId", 123).put("kwh", 100.0))
                    .put(new JSONObject().put("id", 2).put("customerId", 123).put("kwh", 150.0))
                    .put(new JSONObject().put("id", 3).put("customerId", 123).put("kwh", 200.0)));
            return expectedJson.toString().equals(json);
        }));

        // Ensure customerData and messageCount maps are cleared after processing
        assertEquals(0, dataCollectionListener.customerData.size());
        assertEquals(0, dataCollectionListener.messageCount.size());
    }

    // Add more tests as needed for different scenarios (multiple customers, edge cases, etc.)
}
