package lk.ijse.vehicleservice.repo;

import lk.ijse.vehicleservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // For dynamic query building
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Marks this interface as a Spring Data JPA repository
public interface VehicleRepo extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
    // Custom query method to find vehicles by userId
    List<Vehicle> findByUserId(Long userId);

    // Custom query method to find a vehicle by plateNumber
    // Optional: add @Query if plateNumber is not unique and you expect multiple
    Vehicle findByPlateNumber(String plateNumber);
}