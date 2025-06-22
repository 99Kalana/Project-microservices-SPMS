package lk.ijse.paymentservice.service.impl;

import lk.ijse.paymentservice.dto.DigitalReceiptDTO;
import lk.ijse.paymentservice.dto.PaymentRequestDTO;
import lk.ijse.paymentservice.dto.PaymentResponseDTO;
import lk.ijse.paymentservice.entity.Payment;
import lk.ijse.paymentservice.repo.PaymentRepo;
import lk.ijse.paymentservice.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public PaymentServiceImpl(PaymentRepo paymentRepo, ModelMapper modelMapper) {
        this.paymentRepo = paymentRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO) {
        // 1. Validate mock card/payment data (basic validation here, more complex in real scenario)
        if (!validateMockCardData(paymentRequestDTO)) {
            throw new RuntimeException("Invalid card details or payment request.");
        }

        // 2. Simulate transaction flow and status
        Payment payment = modelMapper.map(paymentRequestDTO, Payment.class);
        //payment.setTransactionId(UUID.randomUUID().toString()); // Generate unique transaction ID
        payment.setTransactionTime(LocalDateTime.now());
        payment.setPaymentMethod(paymentRequestDTO.getPaymentMethod() != null ? paymentRequestDTO.getPaymentMethod() : "CARD");

        // Simulate success/failure for demonstration
        // For example, decline if CVV is '000' or amount is very high
        if (paymentRequestDTO.getCvv().equals("000") || paymentRequestDTO.getAmount() > 10000.00) {
            payment.setStatus("FAILED");
            payment.setErrorMessage("Payment simulation failed: Invalid CVV or Amount exceeds limit.");
            payment.setMaskedCardNumber(maskCardNumber(paymentRequestDTO.getCardNumber()));
            // No receipt for failed payments
        } else {
            payment.setStatus("SUCCESS");
            payment.setErrorMessage(null);
            payment.setMaskedCardNumber(maskCardNumber(paymentRequestDTO.getCardNumber()));

            // 3. Generate digital receipts
            DigitalReceiptDTO receipt = generateDigitalReceipt(paymentRequestDTO, payment.getTransactionId(), payment.getTransactionTime());
            payment.setReceiptId(receipt.getReceiptId());
            payment.setPaidFor(receipt.getPaidFor());
            payment.setCustomerName(receipt.getCustomerName());
            payment.setMerchantInfo(receipt.getMerchantInfo());
            payment.setRemarks(receipt.getRemarks());
        }

        Payment savedPayment = paymentRepo.save(payment);

        PaymentResponseDTO responseDTO = modelMapper.map(savedPayment, PaymentResponseDTO.class);
        if (savedPayment.getStatus().equals("SUCCESS")) {
            responseDTO.setDigitalReceipt(generateDigitalReceipt(paymentRequestDTO, savedPayment.getTransactionId(), savedPayment.getTransactionTime()));
        }

        return responseDTO;
    }

    @Override
    public PaymentResponseDTO getPaymentDetails(String transactionId) {
        Optional<Payment> paymentOptional = paymentRepo.findById(transactionId);
        if (paymentOptional.isEmpty()) {
            return null; // Or throw an exception
        }
        Payment payment = paymentOptional.get();
        PaymentResponseDTO responseDTO = modelMapper.map(payment, PaymentResponseDTO.class);

        if (payment.getStatus().equals("SUCCESS") && payment.getReceiptId() != null) {
            // Reconstruct digital receipt for the response
            responseDTO.setDigitalReceipt(new DigitalReceiptDTO(
                    payment.getReceiptId(),
                    payment.getTransactionId(),
                    payment.getTransactionTime(),
                    payment.getAmount(),
                    payment.getPaidFor(),
                    payment.getCustomerName(),
                    payment.getPaymentMethod(),
                    payment.getMerchantInfo(),
                    payment.getRemarks()
            ));
        }
        return responseDTO;
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepo.findAll().stream()
                .map(this::mapPaymentToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByBookingId(Long bookingId) {
        // In a real system, you might first check if the bookingId exists in booking-service
        return paymentRepo.findByBookingId(bookingId).stream()
                .map(this::mapPaymentToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DigitalReceiptDTO getDigitalReceipt(String transactionId) {
        Payment payment = paymentRepo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + transactionId));

        if (!payment.getStatus().equals("SUCCESS") || payment.getReceiptId() == null) {
            throw new RuntimeException("Receipt not available for this transaction (status: " + payment.getStatus() + ")");
        }

        return new DigitalReceiptDTO(
                payment.getReceiptId(),
                payment.getTransactionId(),
                payment.getTransactionTime(),
                payment.getAmount(),
                payment.getPaidFor(),
                payment.getCustomerName(), // This might need to come from user-service or be passed in
                payment.getPaymentMethod(),
                payment.getMerchantInfo(),
                payment.getRemarks()
        );
    }

    // --- Helper Methods ---

    private boolean validateMockCardData(PaymentRequestDTO dto) {
        // Basic mock validation. In a real scenario, this would involve a payment gateway SDK.
        // For demonstration, let's say:
        // Card number must be 16 digits and start with 4 or 5
        // Expiry date must be in the future (MM/YY)
        // CVV must be 3 or 4 digits
        if (!dto.getCardNumber().matches("^\\d{16}$")) {
            System.err.println("Invalid Card Number format");
            return false;
        }

        // Simple expiry date check (MM/YY format)
        String[] expiry = dto.getExpiryDate().split("/");
        int month = Integer.parseInt(expiry[0]);
        int year = 2000 + Integer.parseInt(expiry[1]); // Assuming 2-digit year is in 20xx

        LocalDateTime now = LocalDateTime.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        if (year < currentYear || (year == currentYear && month < currentMonth)) {
            System.err.println("Card expired");
            return false;
        }

        if (!dto.getCvv().matches("^\\d{3,4}$")) {
            System.err.println("Invalid CVV format");
            return false;
        }

        return true;
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() <= 4) {
            return cardNumber;
        }
        return "XXXX-XXXX-XXXX-" + cardNumber.substring(cardNumber.length() - 4);
    }

    private DigitalReceiptDTO generateDigitalReceipt(PaymentRequestDTO paymentRequestDTO, String transactionId, LocalDateTime transactionTime) {
        return new DigitalReceiptDTO(
                "RECEIPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(), // Simple receipt ID
                transactionId,
                transactionTime,
                paymentRequestDTO.getAmount(),
                "Parking Fee for Booking #" + paymentRequestDTO.getBookingId(),
                paymentRequestDTO.getCardHolderName(), // Using card holder name as customer name
                paymentRequestDTO.getPaymentMethod() != null ? paymentRequestDTO.getPaymentMethod() : "CARD",
                "Smart Parking Management System",
                "Thank you for your payment!"
        );
    }

    // Helper to map Payment entity to PaymentResponseDTO, including receipt
    private PaymentResponseDTO mapPaymentToResponseDTO(Payment payment) {
        PaymentResponseDTO dto = modelMapper.map(payment, PaymentResponseDTO.class);
        if (payment.getStatus().equals("SUCCESS") && payment.getReceiptId() != null) {
            dto.setDigitalReceipt(new DigitalReceiptDTO(
                    payment.getReceiptId(),
                    payment.getTransactionId(),
                    payment.getTransactionTime(),
                    payment.getAmount(),
                    payment.getPaidFor(),
                    payment.getCustomerName(),
                    payment.getPaymentMethod(),
                    payment.getMerchantInfo(),
                    payment.getRemarks()
            ));
        }
        return dto;
    }
}