package lk.ijse.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private Long userId; // Include user ID in response for client-side use
    private String username;
    private String role;
    // You might include more user-specific data here if needed, but avoid sensitive info
}