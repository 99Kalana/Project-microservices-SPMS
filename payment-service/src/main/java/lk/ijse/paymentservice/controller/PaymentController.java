package lk.ijse.paymentservice.controller;

import lk.ijse.paymentservice.dto.PaymentRequestDTO;
import lk.ijse.paymentservice.dto.PaymentResponseDTO;
import lk.ijse.paymentservice.dto.ResponseDTO;
import lk.ijse.paymentservice.dto.DigitalReceiptDTO;
import lk.ijse.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    // @PreAuthorize for security can be added here if needed, e.g., only authenticated users can process payments
    public ResponseEntity<ResponseDTO> processPayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        // Removed try-catch. If processPayment throws a RuntimeException (e.g., "Invalid card details"),
        // the GlobalExceptionHandler's handleRuntimeException will catch it.
        PaymentResponseDTO response = paymentService.processPayment(paymentRequestDTO);

        if ("SUCCESS".equals(response.getStatus())) {
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.OK.value(), "Payment processed successfully", response),
                    HttpStatus.OK
            );
        } else {
            // For a 'FAILED' status that is a valid business outcome (not an unexpected error),
            // we still return 400 BAD_REQUEST or a more specific client error status.
            // The message and data come from the service layer's response.
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.BAD_REQUEST.value(), response.getMessage() != null ? response.getMessage() : "Payment failed", response),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/{transactionId}")
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')") - User should only see their own, Admin can see all
    public ResponseEntity<ResponseDTO> getPaymentDetails(@PathVariable String transactionId) {
        // Removed try-catch. If getPaymentDetails throws a RuntimeException (e.g., if a custom
        // PaymentNotFoundException is introduced), the GlobalExceptionHandler will catch it.
        // For 'null' return, we handle it explicitly here as it's a specific business 'not found' case.
        PaymentResponseDTO paymentDetails = paymentService.getPaymentDetails(transactionId);
        if (paymentDetails == null) {
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Payment not found", null),
                    HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(
                new ResponseDTO(HttpStatus.OK.value(), "Payment details retrieved successfully", paymentDetails),
                HttpStatus.OK
        );
    }

    @GetMapping
    // @PreAuthorize("hasRole('ADMIN')") - Only Admin can retrieve all payments
    public ResponseEntity<ResponseDTO> getAllPayments() {
        // Removed try-catch. If getAllPayments throws an exception, GlobalExceptionHandler will catch it.
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
        return new ResponseEntity<>(
                new ResponseDTO(HttpStatus.OK.value(), "All payments retrieved successfully", payments),
                HttpStatus.OK
        );
    }

    @GetMapping("/booking/{bookingId}")
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')") - User can see their own booking payments, Admin can see all
    public ResponseEntity<ResponseDTO> getPaymentsByBookingId(@PathVariable Long bookingId) {
        // Removed try-catch. If getPaymentsByBookingId throws a RuntimeException,
        // GlobalExceptionHandler will catch it.
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByBookingId(bookingId);
        return new ResponseEntity<>(
                new ResponseDTO(HttpStatus.OK.value(), "Payments for booking retrieved successfully", payments),
                HttpStatus.OK
        );
    }

    @GetMapping("/{transactionId}/receipt")
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')") - User can get their own receipt, Admin can get all
    public ResponseEntity<ResponseDTO> getDigitalReceipt(@PathVariable String transactionId) {
        // Removed try-catch. If getDigitalReceipt throws a RuntimeException,
        // GlobalExceptionHandler will catch it.
        DigitalReceiptDTO receipt = paymentService.getDigitalReceipt(transactionId);
        return new ResponseEntity<>(
                new ResponseDTO(HttpStatus.OK.value(), "Digital receipt retrieved successfully", receipt),
                HttpStatus.OK
        );
    }
}