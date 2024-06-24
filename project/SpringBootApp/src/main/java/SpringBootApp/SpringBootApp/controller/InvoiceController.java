package SpringBootApp.SpringBootApp.controller;

import SpringBootApp.SpringBootApp.config.RabbitMQConfig;
import SpringBootApp.SpringBootApp.Service.InvoiceService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final RabbitTemplate rabbitTemplate;
    private final InvoiceService invoiceService;

    public InvoiceController(RabbitTemplate rabbitTemplate, InvoiceService invoiceService) {
        this.rabbitTemplate = rabbitTemplate;
        this.invoiceService = invoiceService;
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<Void> startInvoiceGeneration(@PathVariable String customerId) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.APP_DISPATCHER_QUEUE, customerId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<String> getInvoice(@PathVariable String customerId) {
        boolean invoiceGenerated = invoiceService.checkInvoiceStatus(customerId);

        if (invoiceGenerated) {
            String invoiceUrl = "files/Customer_" + customerId + "_" + LocalDate.now() + ".pdf";
            return ResponseEntity.ok(invoiceUrl);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
