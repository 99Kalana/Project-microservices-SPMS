package lk.ijse.parkingspaceservice.repo;

import lk.ijse.parkingspaceservice.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // Import this
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSpaceRepo extends JpaRepository<ParkingSpace, Long>, JpaSpecificationExecutor<ParkingSpace> { // Extend JpaSpecificationExecutor
    List<ParkingSpace> findByAvailableTrueAndZone(String zone);
}