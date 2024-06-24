package PDFgenerator.PDFgenerator.listener;

import PDFgenerator.PDFgenerator.config.RabbitMQConfig;
import PDFgenerator.PDFgenerator.entity.Charge;
import PDFgenerator.PDFgenerator.entity.Customer;
import PDFgenerator.PDFgenerator.repository.CustomerRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PDFGeneratorListener {

    @Autowired
    private CustomerRepository customerRepository;

    @RabbitListener(queues = RabbitMQConfig.PDF_GENERATOR_QUEUE)
    public void receiveDataMessage(String message) {
        System.out.println("Received message: " + message);
        FileOutputStream fos = null;
        try {
            JSONObject jsonMessage = new JSONObject(message);
            String customerId = getCustomerIdFromMessage(jsonMessage);
            Optional<Customer> customer = customerRepository.findById(Long.parseLong(customerId));
            String customerName = customer.map(c -> c.getFirstName() + " " + c.getLastName()).orElse("Unknown Customer");

            // Create directory if it doesn't exist
            File directory = new File("files");
            if (!directory.exists()) {
                directory.mkdir();
            }

            String fileName = "files/Customer_" + customerId + "_" + LocalDate.now() + ".pdf";

            fos = new FileOutputStream(fileName);
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("Invoice").setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(25));
            document.add(new Paragraph("Date: " + LocalDate.now()).setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("Customer: " + customerName).setTextAlignment(TextAlignment.LEFT));

            double totalCost = 0.0;
            for (String dbUrl : jsonMessage.keySet()) {
                JSONArray jsonCharges = jsonMessage.getJSONArray(dbUrl);
                List<Charge> charges = new ArrayList<>();

                for (int i = 0; i < jsonCharges.length(); i++) {
                    JSONObject jsonCharge = jsonCharges.getJSONObject(i);
                    Charge charge = new Charge(jsonCharge);
                    charges.add(charge);
                }

                document.add(new Paragraph("Charges for DB: " + dbUrl).setBold());
                totalCost += addChargesTable(document, charges);
            }

            document.add(new Paragraph(String.format("Total Cost: %.2f", totalCost)).setBold().setTextAlignment(TextAlignment.RIGHT));

            document.close();
            System.out.println("PDF created: " + fileName);
        } catch (Exception e) {
            System.err.println("Failed to process data message: " + message);
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.err.println("Failed to close FileOutputStream");
                    e.printStackTrace();
                }
            }
        }
    }

    private double addChargesTable(Document document, List<Charge> charges) {
        if (charges.isEmpty()) {
            document.add(new Paragraph("No charges available."));
            return 0.0;
        }

        double totalCost = 0.0;

        Table table = new Table(3);
        table.addHeaderCell("Charge ID");
        table.addHeaderCell("kWh");
        table.addHeaderCell("Price");

        for (Charge charge : charges) {
            table.addCell(charge.getId().toString());
            table.addCell(charge.getKwh().toString());
            // Calculate the price as kWh * 0.25
            double price = charge.getKwh() * 0.25;
            totalCost += price;
            table.addCell(String.format("%.2f", price));
        }

        document.add(table);
        document.add(new Paragraph());

        return totalCost;
    }

    private String getCustomerIdFromMessage(JSONObject message) {
        for (String dbUrl : message.keySet()) {
            JSONArray jsonCharges = message.getJSONArray(dbUrl);
            if (jsonCharges.length() > 0) {
                return String.valueOf(jsonCharges.getJSONObject(0).getLong("customerId"));
            }
        }
        return "Unknown";
    }
}
