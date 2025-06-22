package lk.ijse.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DigitalReceiptDTO {
    private String receiptId;
    private String transactionId;
    private LocalDateTime issuedDate;
    private double amountPaid;
    private String paidFor; // e.g., "Parking Fee for Booking #123"
    private String customerName; // Could fetch from User Service if needed, or pass in payment request
    private String paymentMethod;
    private String merchantInfo; // e.g., "Smart Parking Management System"
    private String remarks; // Any extra notes
}