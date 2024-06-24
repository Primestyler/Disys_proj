package PDFgenerator.PDFgenerator.listener;

import PDFgenerator.PDFgenerator.entity.Charge;
import PDFgenerator.PDFgenerator.entity.Customer;
import PDFgenerator.PDFgenerator.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.Optional;

import static org.mockito.Mockito.*;

class PDFGeneratorListenerTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private PDFGeneratorListener pdfGeneratorListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveDataMessage() {
        String message = "{\"dbUrl\": [{\"customerId\": 1, \"otherField\": \"value\"}]}";

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));

        pdfGeneratorListener.receiveDataMessage(message);

        // Verify interactions and state changes
        verify(customerRepository, times(1)).findById(1L);
    }

}
