package lk.ijse.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    // This field is for registration/password change. For 'get' requests, it should be null.
    @NotBlank(message = "Password cannot be blank", groups = {RegisterValidation.class}) // Only required for registration
    @Size(min = 6, message = "Password must be at least 6 characters long", groups = {RegisterValidation.class})
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String  lastName;

    @NotBlank(message = "Contact number cannot be blank")
    @Pattern(regexp = "^\\+?[0-9.()-]{7,15}$", message = "Invalid contact number format")
    private String contactNumber;

    @NotBlank(message = "Role cannot be blank")
    private String role; // e.g., "USER", "ADMIN", "OWNER"

    // Marker interface for validation groups
    public interface RegisterValidation {}
}