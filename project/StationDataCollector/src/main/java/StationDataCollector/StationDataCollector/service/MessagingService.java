package StationDataCollector.StationDataCollector.service;

import StationDataCollector.StationDataCollector.config.RabbitMQConfig;
import StationDataCollector.StationDataCollector.entity.Charge;
import StationDataCollector.StationDataCollector.entity.ChargeData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendData(ChargeData chargeData) {
        try {
            // Create a JSON object from ChargeData
            JSONObject jsonChargeData = new JSONObject();
            jsonChargeData.put("dbUrl", chargeData.getDbUrl());

            JSONArray jsonCharges = new JSONArray();
            for (Charge charge : chargeData.getCharges()) {
                JSONObject jsonCharge = new JSONObject();
                jsonCharge.put("id", charge.getId());
                jsonCharge.put("customerId", charge.getCustomerId());
                jsonCharge.put("kwh", charge.getKwh());
                jsonCharges.put(jsonCharge);
            }
            jsonChargeData.put("charges", jsonCharges);

            // Convert the JSON object to a string
            String messageBody = jsonChargeData.toString();

            // Create a message with persistent delivery mode
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            Message message = new Message(messageBody.getBytes(), messageProperties);

            // Send the message
            System.out.println("Hello");
            System.out.println(message);
            rabbitTemplate.convertAndSend(RabbitMQConfig.DATA_COLLECTION_QUEUE, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
