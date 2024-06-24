package PDFgenerator.PDFgenerator.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String PDF_GENERATOR_QUEUE = "pdfGeneratorQueue";

    @Bean
    public Queue pdfGeneratorQueue() {
        return new Queue(PDF_GENERATOR_QUEUE, false);
    }
}
