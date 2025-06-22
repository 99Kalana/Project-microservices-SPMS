package lk.ijse.paymentservice.repo;

import lk.ijse.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, String> { // ID type is String (UUID)
    List<Payment> findByBookingId(Long bookingId);
    Optional<Payment> findByReceiptId(String receiptId);
    List<Payment> findByStatus(String status);
}