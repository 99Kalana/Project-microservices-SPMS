package lk.ijse.paymentservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Using UUID for transactionId
    @Column(name = "transaction_id", updatable = false, nullable = false)
    private String transactionId;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId; // Foreign key or identifier for the associated booking

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime transactionTime;

    @Column(nullable = false)
    private String status; // e.g., "SUCCESS", "FAILED", "PENDING"

    private String paymentMethod; // e.g., "CARD", "CASH"

    // Card details (for mock validation, ideally not stored in production or encrypted)
    private String cardHolderName;
    private String maskedCardNumber; // Store only last 4 digits or masked version
    private String expiryDate; // Only if necessary for audit, not for re-use

    // Receipt details
    private String receiptId; // Unique ID for the digital receipt
    private String paidFor;
    private String customerName; // Can be derived from booking/user service
    private String merchantInfo;
    private String remarks;

    // Optional: for errors or additional info
    private String errorMessage;
}