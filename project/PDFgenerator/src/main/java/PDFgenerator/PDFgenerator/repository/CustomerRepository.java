package PDFgenerator.PDFgenerator.repository;

import PDFgenerator.PDFgenerator.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
