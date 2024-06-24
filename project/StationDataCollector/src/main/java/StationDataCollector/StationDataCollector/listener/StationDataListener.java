package StationDataCollector.StationDataCollector.listener;

import StationDataCollector.StationDataCollector.config.DataSourceConfig;
import StationDataCollector.StationDataCollector.entity.Charge;
import StationDataCollector.StationDataCollector.entity.ChargeData;
import StationDataCollector.StationDataCollector.service.MessagingService;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class StationDataListener {

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Autowired
    private MessagingService messagingService;

    @RabbitListener(queues = "stationDataQueue")
    public void receiveMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            String dbUrl = jsonObject.getString("dbUrl");
            int stationId = jsonObject.getInt("stationId");
            Long customerId = jsonObject.getLong("customerId");
            String realDbUrl = dbUrl + "/"+"stationdb";

            DataSource dataSource = dataSourceConfig.createDataSource(realDbUrl);
            List<Charge> charges = getChargesByCustomerId(dataSource, customerId);

            // Create a ChargeData object and send it
            ChargeData chargeData = new ChargeData(charges, realDbUrl);
            System.out.println(chargeData);
            messagingService.sendData(chargeData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<Charge> getChargesByCustomerId(DataSource dataSource, Long customerId) throws Exception {
        List<Charge> charges = new ArrayList<>();
        String query = "SELECT * FROM Charge WHERE customer_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, customerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Charge charge = new Charge();
                    charge.setId(resultSet.getLong("id"));
                    charge.setKwh(resultSet.getFloat("kwh"));
                    charge.setCustomerId(resultSet.getInt("customer_id"));
                    charges.add(charge);
                }
            }
        }
        return charges;
    }
}
