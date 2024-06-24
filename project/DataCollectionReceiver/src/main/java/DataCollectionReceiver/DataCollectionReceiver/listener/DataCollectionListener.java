package DataCollectionReceiver.DataCollectionReceiver.listener;

import DataCollectionReceiver.DataCollectionReceiver.config.RabbitMQConfig;
import DataCollectionReceiver.DataCollectionReceiver.entity.Charge;
import DataCollectionReceiver.DataCollectionReceiver.entity.ChargeData;
import DataCollectionReceiver.DataCollectionReceiver.service.MessagingService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataCollectionListener {

    @Autowired
    private MessagingService messagingService;

    public final Map<Long, Map<String, List<Charge>>> customerData = new HashMap<>();
    public final Map<Long, Integer> messageCount = new HashMap<>();

    @RabbitListener(queues = RabbitMQConfig.DATA_RECEIVER_QUEUE)
    public void receiveJobMessage(String message) {
        try {
            JSONObject jobMessage = new JSONObject(message);
            String customerMessage = jobMessage.getString("message");
            Long customerId = Long.parseLong(customerMessage.split(":")[1].trim());
            System.out.println("Received job message for Customer: " + customerId);

            // Initialize data storage and message counter for this customer
            customerData.putIfAbsent(customerId, new HashMap<>());
            messageCount.putIfAbsent(customerId, 0);
        } catch (Exception e) {
            System.err.println("Failed to process job message: " + message);
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RabbitMQConfig.DATA_COLLECTION_QUEUE)
    public void receiveDataMessage(String message) {
        try {


            ChargeData dataMessage = ChargeData.fromJson(message);
            Long customerId = dataMessage.getCharges().get(0).getCustomerId();
            String dbUrl = dataMessage.getDbUrl();


            // Store data in the corresponding customer's list for the specific dbUrl
            customerData.computeIfAbsent(customerId, k -> new HashMap<>())
                    .computeIfAbsent(dbUrl, k -> new ArrayList<>())
                    .addAll(dataMessage.getCharges());

            // Increment the message counter
            messageCount.put(customerId, messageCount.getOrDefault(customerId, 0) + 1);
            System.out.println("Message count: " + messageCount);

            // Check if 3 messages have been received for this customer
            if (messageCount.get(customerId) >= 3) {
                // Create a JSON object to send to the PDF generator
                JSONObject completeDataJson = new JSONObject();
                Map<String, List<Charge>> dbUrlChargesMap = customerData.get(customerId);

                for (Map.Entry<String, List<Charge>> entry : dbUrlChargesMap.entrySet()) {
                    JSONArray jsonCharges = new JSONArray();
                    for (Charge charge : entry.getValue()) {
                        JSONObject jsonCharge = new JSONObject();
                        jsonCharge.put("id", charge.getId());
                        jsonCharge.put("customerId", charge.getCustomerId());
                        jsonCharge.put("kwh", charge.getKwh());
                        jsonCharges.put(jsonCharge);
                    }
                    completeDataJson.put(entry.getKey(), jsonCharges);
                }

                // Send the complete data to the PDF generator
                messagingService.sendToPDFGenerator(completeDataJson.toString());

                // Clear the stored data and counter for this customer
                customerData.remove(customerId);
                messageCount.remove(customerId);
            }
        } catch (Exception e) {
            System.err.println("Failed to process data message: " + message);
            e.printStackTrace();
        }
    }
}
