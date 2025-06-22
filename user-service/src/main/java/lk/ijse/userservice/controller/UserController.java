package lk.ijse.userservice.controller;

import jakarta.validation.Valid;
import lk.ijse.userservice.dto.AuthRequestDTO;
import lk.ijse.userservice.dto.AuthResponseDTO;
import lk.ijse.userservice.dto.ResponseDTO;
import lk.ijse.userservice.dto.UserDTO;
import lk.ijse.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // For method-level security
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users") // Base path for all endpoints in this controller
@CrossOrigin // Allows cross-origin requests
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint for user registration (accessible without authentication)
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        // Use validation group for password requirement during registration
        userDTO.setPassword(userDTO.getPassword()); // Ensure password field is not null for hashing
        UserDTO registeredUser = userService.registerUser(userDTO);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.CREATED.value(),
                "User Registered Successfully",
                registeredUser
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint for user login and JWT token generation (accessible without authentication)
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> authenticateUser(@Valid @RequestBody AuthRequestDTO authRequest) {
        AuthResponseDTO authResponse = userService.authenticateUser(authRequest);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Authentication Successful",
                authResponse
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to retrieve user details by ID (requires authentication)
    // Example: Only ADMINs or the user themselves can view their profile
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id") // Placeholder for complex logic, requires user details to be principal
    public ResponseEntity<ResponseDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "User Retrieved Successfully",
                user
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to retrieve all users (requires ADMIN role)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Only ADMINs can view all users
    public ResponseEntity<ResponseDTO> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "All Users Retrieved Successfully",
                users
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to update user profile (requires authentication, only ADMINs or the user themselves)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id") // Similar to getUserById
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "User Profile Updated Successfully",
                updatedUser
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to delete a user (requires ADMIN role)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only ADMINs can delete users
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "User Deleted Successfully",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Example of accessing booking history (placeholder, logic depends on how you store/fetch this)
    // This could potentially involve making an inter-service call to a 'booking-service' later
    // @GetMapping("/{id}/booking-history")
    // @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    // public ResponseEntity<ResponseDTO> getUserBookingHistory(@PathVariable Long id) {
    //    // Implement logic to fetch booking history
    //    List<BookingHistoryDTO> history = userService.getUserBookingHistory(id);
    //    ResponseDTO response = new ResponseDTO(
    //            HttpStatus.OK.value(),
    //            "Booking History Retrieved",
    //            history
    //    );
    //    return new ResponseEntity<>(response, HttpStatus.OK);
    // }
}