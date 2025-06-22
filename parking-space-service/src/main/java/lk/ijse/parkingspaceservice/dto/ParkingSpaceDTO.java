package lk.ijse.parkingspaceservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpaceDTO {
    private Long id;
    @NotBlank(message = "Location cannot be blank")
    private String location;
    @NotBlank(message = "Zone cannot be blank")
    private String zone;
    @NotNull(message = "Availability status cannot be null")
    private boolean available; // CHANGED FROM isAvailable
    @NotBlank(message = "Type cannot be blank")
    private String type;
    private String externalRefId;
}