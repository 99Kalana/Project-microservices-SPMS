package lk.ijse.parkingspaceservice.service;

import lk.ijse.parkingspaceservice.dto.ParkingSpaceDTO;

import java.util.List;

public interface ParkingSpaceService {
    ParkingSpaceDTO saveParkingSpace(ParkingSpaceDTO parkingSpaceDTO);
    ParkingSpaceDTO getParkingSpaceById(Long id);
    List<ParkingSpaceDTO> getAllParkingSpaces();
    void deleteParkingSpace(Long id);
    ParkingSpaceDTO updateParkingSpace(Long id, ParkingSpaceDTO parkingSpaceDTO);

    List<ParkingSpaceDTO> getAvailableParkingSpacesByZone(String zone);

    // New methods for reserving, releasing, and status update
    ParkingSpaceDTO reserveParkingSpace(Long id); // Marks as unavailable
    ParkingSpaceDTO releaseParkingSpace(Long id); // Marks as available
    ParkingSpaceDTO updateParkingSpaceStatus(Long id, boolean isAvailable); // Generic status update

    List<ParkingSpaceDTO> filterParkingSpaces(String location, String zone, Boolean available, String type); // More generic filtering
}