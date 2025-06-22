package lk.ijse.userservice.controller;

import lk.ijse.userservice.dto.BookingDTO;
import lk.ijse.userservice.dto.ResponseDTO; // Import your ResponseDTO
import lk.ijse.userservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Create a new booking (User or Admin)
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
        try {
            BookingDTO createdBooking = bookingService.createBooking(bookingDTO);
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.CREATED.value(), "Booking created successfully", createdBooking),
                    HttpStatus.CREATED
            );
        } catch (RuntimeException e) {
            // More specific error handling could be implemented here (e.g., UserNotFoundException)
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // Get a booking by ID (Authenticated user - Admin or owner)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO> getBookingById(@PathVariable Long id) {
        BookingDTO booking = bookingService.getBookingById(id);
        if (booking == null) {
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Booking not found", null),
                    HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(
                new ResponseDTO(HttpStatus.OK.value(), "Booking retrieved successfully", booking),
                HttpStatus.OK
        );
    }

    // Get all bookings (Admin only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(
                new ResponseDTO(HttpStatus.OK.value(), "All bookings retrieved successfully", bookings),
                HttpStatus.OK
        );
    }

    // Get bookings by User ID (Admin or the user himself)
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO> getBookingsByUserId(@PathVariable Long userId) {
        try {
            List<BookingDTO> bookings = bookingService.getBookingsByUserId(userId);
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.OK.value(), "Bookings for user retrieved successfully", bookings),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {
            // e.g., "User not found with ID: ..." from service
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.NOT_FOUND.value(), e.getMessage(), null),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    // Update an existing booking (User or Admin - ownership logic apply)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO> updateBooking(@PathVariable Long id, @RequestBody BookingDTO bookingDTO) {
        try {
            BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDTO);
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.OK.value(), "Booking updated successfully", updatedBooking),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {
            // e.g., "Booking not found with ID: ..." from service
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.NOT_FOUND.value(), e.getMessage(), null),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    // Delete a booking (Admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.NO_CONTENT.value(), "Booking deleted successfully", null),
                    HttpStatus.NO_CONTENT
            ); // Note: For NO_CONTENT, Spring might not return a body, but the DTO indicates success.
            // The client will still see the 204 status code.
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.NOT_FOUND.value(), e.getMessage(), null),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}