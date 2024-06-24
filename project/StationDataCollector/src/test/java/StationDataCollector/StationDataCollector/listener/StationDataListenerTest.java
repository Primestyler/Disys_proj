package StationDataCollector.StationDataCollector.listener;

import StationDataCollector.StationDataCollector.config.DataSourceConfig;
import StationDataCollector.StationDataCollector.entity.Charge;
import StationDataCollector.StationDataCollector.entity.ChargeData;
import StationDataCollector.StationDataCollector.service.MessagingService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StationDataListenerTest {

    @Mock
    private DataSourceConfig dataSourceConfig;

    @Mock
    private MessagingService messagingService;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private StationDataListener stationDataListener;

    private String message;

    @BeforeEach
    public void setUp() {
        message = "{\"dbUrl\": \"jdbc:postgresql://localhost:5432\", \"stationId\": 1, \"customerId\": 123}";
    }

    @Test
    public void testReceiveMessage() throws Exception {
        when(dataSourceConfig.createDataSource(any(String.class))).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        List<Charge> charges = new ArrayList<>();
        Charge charge = new Charge();
        charge.setId(1L);
        charge.setKwh(10.0f);
        charge.setCustomerId(123);
        charges.add(charge);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(charge.getId());
        when(resultSet.getFloat("kwh")).thenReturn(charge.getKwh());
        when(resultSet.getInt("customer_id")).thenReturn(charge.getCustomerId());

        stationDataListener.receiveMessage(message);

        verify(messagingService, times(1)).sendData(any(ChargeData.class));
    }

    @Test
    public void testGetChargesByCustomerId() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        List<Charge> charges = new ArrayList<>();
        Charge charge = new Charge();
        charge.setId(1L);
        charge.setKwh(10.0f);
        charge.setCustomerId(123);
        charges.add(charge);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(charge.getId());
        when(resultSet.getFloat("kwh")).thenReturn(charge.getKwh());
        when(resultSet.getInt("customer_id")).thenReturn(charge.getCustomerId());

        List<Charge> result = stationDataListener.getChargesByCustomerId(dataSource, 123L);

        assertEquals(1, result.size());
        assertEquals(charge.getId(), result.get(0).getId());
        assertEquals(charge.getKwh(), result.get(0).getKwh());
        assertEquals(charge.getCustomerId(), result.get(0).getCustomerId());
    }
}
