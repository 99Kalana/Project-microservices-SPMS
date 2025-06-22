package lk.ijse.vehicleservice.service;

import lk.ijse.vehicleservice.dto.VehicleDTO;

import java.util.List;

public interface VehicleService {
    VehicleDTO saveVehicle(VehicleDTO vehicleDTO);
    VehicleDTO getVehicleById(Long id);
    List<VehicleDTO> getAllVehicles();
    void deleteVehicle(Long id);
    VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO);

    // Methods for "Link vehicles to users"
    List<VehicleDTO> getVehiclesByUserId(Long userId);

    // Methods for "Simulate vehicle entry / exit tracking"
    VehicleDTO recordVehicleEntry(Long vehicleId); // Sets status to "IN"
    VehicleDTO recordVehicleExit(Long vehicleId);  // Sets status to "OUT"
    VehicleDTO updateVehicleStatus(Long vehicleId, String newStatus); // Generic status update
}