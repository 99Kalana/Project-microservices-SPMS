package lk.ijse.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking") // Specify table name
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Assuming a booking is associated with a user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column
    private User user;

    private String vehicleNumber;
    private LocalDateTime bookingTime;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String parkingSpotId; // e.g., "A1", "B2"
    private double totalCost;
    private String status; // e.g., "PENDING", "CONFIRMED", "COMPLETED", "CANCELLED"

    // Optional: for logs or additional details
    private String remarks;
}