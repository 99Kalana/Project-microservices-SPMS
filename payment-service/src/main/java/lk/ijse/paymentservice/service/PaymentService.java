package lk.ijse.paymentservice.service;

import lk.ijse.paymentservice.dto.PaymentRequestDTO;
import lk.ijse.paymentservice.dto.PaymentResponseDTO;
import lk.ijse.paymentservice.dto.DigitalReceiptDTO;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO getPaymentDetails(String transactionId);
    List<PaymentResponseDTO> getAllPayments();
    List<PaymentResponseDTO> getPaymentsByBookingId(Long bookingId);
    DigitalReceiptDTO getDigitalReceipt(String transactionId);
    // You might add methods for refund, void, etc. later
}