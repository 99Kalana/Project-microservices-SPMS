package lk.ijse.userservice.repo;

import lk.ijse.userservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
    // Custom query to find bookings by user ID
    List<Booking> findByUserId(Long userId);

    // Optional: find bookings by status
    List<Booking> findByStatus(String status);
}