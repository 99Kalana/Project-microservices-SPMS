package lk.ijse.vehicleservice.service.impl;

import lk.ijse.vehicleservice.dto.VehicleDTO;
import lk.ijse.vehicleservice.entity.Vehicle;
import lk.ijse.vehicleservice.repo.VehicleRepo;
import lk.ijse.vehicleservice.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service // Marks this class as a Spring Service component
@Transactional // Ensures methods are executed within a transaction
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepo vehicleRepo;
    private final ModelMapper modelMapper;

    // Constructor Injection
    public VehicleServiceImpl(VehicleRepo vehicleRepo, ModelMapper modelMapper) {
        this.vehicleRepo = vehicleRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public VehicleDTO saveVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = modelMapper.map(vehicleDTO, Vehicle.class);
        Vehicle savedVehicle = vehicleRepo.save(vehicle);
        return modelMapper.map(savedVehicle, VehicleDTO.class);
    }

    @Override
    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + id));
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepo.findAll();
        return vehicles.stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteVehicle(Long id) {
        if (!vehicleRepo.existsById(id)) {
            throw new RuntimeException("Vehicle not found with ID: " + id);
        }
        vehicleRepo.deleteById(id);
    }

    @Override
    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        Vehicle existingVehicle = vehicleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + id));

        // Map DTO to existing entity to update its fields
        modelMapper.map(vehicleDTO, existingVehicle);
        existingVehicle.setId(id); // Ensure the ID remains the same

        Vehicle updatedVehicle = vehicleRepo.save(existingVehicle);
        return modelMapper.map(updatedVehicle, VehicleDTO.class);
    }

    @Override
    public List<VehicleDTO> getVehiclesByUserId(Long userId) {
        List<Vehicle> vehicles = vehicleRepo.findByUserId(userId);
        return vehicles.stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDTO recordVehicleEntry(Long vehicleId) {
        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + vehicleId));
        vehicle.setEntryStatus("IN"); // Set status to "IN"
        Vehicle updatedVehicle = vehicleRepo.save(vehicle);
        return modelMapper.map(updatedVehicle, VehicleDTO.class);
    }

    @Override
    public VehicleDTO recordVehicleExit(Long vehicleId) {
        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + vehicleId));
        vehicle.setEntryStatus("OUT"); // Set status to "OUT"
        Vehicle updatedVehicle = vehicleRepo.save(vehicle);
        return modelMapper.map(updatedVehicle, VehicleDTO.class);
    }

    @Override
    public VehicleDTO updateVehicleStatus(Long vehicleId, String newStatus) {
        // Basic validation for newStatus, you might want a more robust enum or set of allowed values
        if (!newStatus.equalsIgnoreCase("IN") && !newStatus.equalsIgnoreCase("OUT") && !newStatus.equalsIgnoreCase("PARKED")) {
            throw new IllegalArgumentException("Invalid vehicle status: " + newStatus + ". Allowed statuses are IN, OUT, PARKED.");
        }

        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + vehicleId));
        vehicle.setEntryStatus(newStatus.toUpperCase()); // Set the new status
        Vehicle updatedVehicle = vehicleRepo.save(vehicle);
        return modelMapper.map(updatedVehicle, VehicleDTO.class);
    }
}