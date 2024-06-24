package SpringBootApp.SpringBootApp;

import SpringBootApp.SpringBootApp.config.RabbitMQConfig;
import SpringBootApp.SpringBootApp.controller.InvoiceController;
import SpringBootApp.SpringBootApp.Service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class InvoiceControllerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStartInvoiceGeneration() {
        String customerId = "123";

        ResponseEntity<Void> response = invoiceController.startInvoiceGeneration(customerId);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(rabbitTemplate, times(1)).convertAndSend(eq(RabbitMQConfig.APP_DISPATCHER_QUEUE), eq(customerId));
    }

    @Test
    public void testGetInvoice_InvoiceGenerated() {
        String customerId = "123";

        // Mock checkInvoiceStatus to return true
        when(invoiceService.checkInvoiceStatus(customerId)).thenReturn(true);

        ResponseEntity<String> response = invoiceController.getInvoice(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        String expectedInvoiceUrl = "files/Customer_" + customerId + "_" + LocalDate.now() + ".pdf";
        assertEquals(expectedInvoiceUrl, Objects.requireNonNull(response.getBody()));
    }

    @Test
    public void testGetInvoice_InvoiceNotGenerated() {
        String customerId = "123";

        // Mock checkInvoiceStatus to return false
        when(invoiceService.checkInvoiceStatus(customerId)).thenReturn(false);

        ResponseEntity<String> response = invoiceController.getInvoice(customerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
