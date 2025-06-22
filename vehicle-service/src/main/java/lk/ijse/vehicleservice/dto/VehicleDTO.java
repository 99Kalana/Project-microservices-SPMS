package lk.ijse.vehicleservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern; // For plate number validation
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: Generates a no-argument constructor
@AllArgsConstructor // Lombok: Generates a constructor with all arguments
public class VehicleDTO {
    private Long id;

    @NotBlank(message = "Vehicle make cannot be blank")
    private String make; // e.g., "Honda", "Toyota"

    @NotBlank(message = "Vehicle model cannot be blank")
    private String model; // e.g., "Civic", "CR-V"

    @NotBlank(message = "Plate number cannot be blank")
    @Pattern(regexp = "^[A-Z0-9-]{1,10}$", message = "Invalid plate number format") // Example regex, adjust as needed
    private String plateNumber; // Unique vehicle plate number, e.g., "ABC-1234"

    @NotBlank(message = "Color cannot be blank")
    private String color; // e.g., "Red", "Black"

    @NotBlank(message = "Vehicle type cannot be blank")
    private String type; // e.g., "Car", "Motorbike", "Van"

    @NotNull(message = "User ID cannot be null")
    private Long userId; // To link to a user in the user-service

    @NotBlank(message = "Entry status cannot be blank")
    // Consider using an enum here for stricter control, but String is fine for now
    private String entryStatus; // "IN", "OUT", "PARKED" - for tracking entry/exit
}