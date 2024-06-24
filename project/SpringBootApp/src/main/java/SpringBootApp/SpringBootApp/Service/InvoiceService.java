package SpringBootApp.SpringBootApp.Service;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Service
public class InvoiceService {

    public boolean checkInvoiceStatus(String customerId) {
        // Construct the file path
        String filePath = "files/Customer_" + customerId + "_" + LocalDate.now() + ".pdf";
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }
}