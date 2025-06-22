package lk.ijse.userservice.entity;

import jakarta.persistence.Column;
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
public class User {
    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    private Long id;

    @Column(unique = true, nullable = false) // Username must be unique and not null
    private String username;

    @Column(nullable = false) // Password must not be null
    private String password; // Will store hashed password

    @Column(unique = true) // Email should be unique, but can be nullable if desired
    private String email;

    private String firstName;
    private String lastName;
    private String contactNumber;

    // Role for authorization (e.g., "USER", "ADMIN", "OWNER")
    @Column(nullable = false)
    private String role; // Ensure default is set or handled during registration

    // You can add fields for 'booking history' or 'logs' here
    // but often, this is better handled by linking to other entities
    // or by separate services if "history" means a collection of events.
    // For now, we'll keep it simple and focus on core user data.
}