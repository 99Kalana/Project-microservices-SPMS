package lk.ijse.userservice.service;

import lk.ijse.userservice.dto.AuthRequestDTO;
import lk.ijse.userservice.dto.AuthResponseDTO;
import lk.ijse.userservice.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO); // For user registration
    AuthResponseDTO authenticateUser(AuthRequestDTO authRequest); // For user login and JWT generation
    UserDTO getUserById(Long id);
    UserDTO getUserByUsername(String username); // New method for security context
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);

    // Placeholder for booking history/logs (implementation might involve inter-service communication)
    // List<BookingHistoryDTO> getUserBookingHistory(Long userId);
}