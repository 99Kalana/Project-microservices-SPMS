package lk.ijse.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    // Assuming a payment is linked to a booking for parking fees
    @NotNull(message = "Booking ID cannot be null")
    private Long bookingId;

    @Min(value = 0, message = "Amount must be positive")
    private double amount;

    // Mock Card Details
    @NotBlank(message = "Card holder name is required")
    private String cardHolderName;

    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "^(?:(?<visa>4\\d{12}(?:\\d{3})?)|" +
            "(?<mastercard>5[1-5]\\d{14})|" +
            "(?<amex>3[47]\\d{13})|" +
            "(?<discover>6(?:011|5\\d{2})\\d{12}))$",
            message = "Invalid card number format")
    private String cardNumber;

    @NotBlank(message = "Expiry date is required (MM/YY)")
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/([0-9]{2})$",
            message = "Invalid expiry date format. Use MM/YY")
    private String expiryDate; // MM/YY

    @NotBlank(message = "CVV is required")
    @Pattern(regexp = "^\\d{3,4}$", message = "Invalid CVV format")
    private String cvv;

    // Optional: Payment method type (e.g., "CARD", "CASH", "MOBILE_PAY")
    private String paymentMethod;
}