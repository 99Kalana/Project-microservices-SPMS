package lk.ijse.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private String transactionId;
    private Long bookingId;
    private double amount;
    private String status; // e.g., "SUCCESS", "FAILED", "PENDING"
    private String message; // e.g., "Payment successful", "Insufficient funds"
    private LocalDateTime transactionTime;
    private DigitalReceiptDTO digitalReceipt; // Nested DTO for receipt details
}