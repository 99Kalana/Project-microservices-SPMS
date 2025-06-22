package lk.ijse.vehicleservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component; // Consider removing @Component for DTOs

@NoArgsConstructor
@AllArgsConstructor
@Data // Includes @Getter, @Setter, @ToString, @EqualsAndHashCode
@Component // As discussed before, consider if this is truly needed for a DTO
public class ResponseDTO {
    private int status;
    private String message;
    private Object data;
}