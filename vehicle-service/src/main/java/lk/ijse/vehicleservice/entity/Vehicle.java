package lk.ijse.vehicleservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Marks this class as a JPA entity, mapping to a database table
@Data // Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: Generates a no-argument constructor
@AllArgsConstructor // Lombok: Generates a constructor with all arguments
public class Vehicle {
    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    private Long id;
    private String make; // e.g., "Honda", "Toyota"
    private String model; // e.g., "Civic", "CR-V"
    private String plateNumber; // Unique vehicle plate number, e.g., "ABC-1234"
    private String color; // e.g., "Red", "Black"
    private String type; // e.g., "Car", "Motorbike", "Van"
    private Long userId; // To link to a user in the user-service (Foreign Key concept without strict FK constraint here)
    private String entryStatus; // "IN", "OUT", "PARKED" - for tracking entry/exit
    // Add other fields as per your specific requirements, e.g., year, chassis number, etc.
}