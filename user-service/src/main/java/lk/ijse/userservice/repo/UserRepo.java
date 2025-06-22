package lk.ijse.userservice.repo;

import lk.ijse.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // For dynamic query building
import org.springframework.stereotype.Repository;

import java.util.Optional; // Use Optional for methods that might not return a result

@Repository // Marks this interface as a Spring Data JPA repository
public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    // Custom query method to find a user by username (crucial for authentication)
    Optional<User> findByUsername(String username);

    // Optional: Find by email if you allow email-based login or lookup
    Optional<User> findByEmail(String email);
}