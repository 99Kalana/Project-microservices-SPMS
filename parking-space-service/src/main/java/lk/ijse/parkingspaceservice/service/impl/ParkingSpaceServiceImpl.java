package lk.ijse.parkingspaceservice.service.impl;

import lk.ijse.parkingspaceservice.dto.ParkingSpaceDTO;
import lk.ijse.parkingspaceservice.entity.ParkingSpace;
import lk.ijse.parkingspaceservice.repo.ParkingSpaceRepo;
import lk.ijse.parkingspaceservice.service.ParkingSpaceService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification; // Import for dynamic queries
import jakarta.persistence.criteria.Predicate; // Import for dynamic queries

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    private final ParkingSpaceRepo parkingSpaceRepo;
    private final ModelMapper modelMapper;

    public ParkingSpaceServiceImpl(ParkingSpaceRepo parkingSpaceRepo, ModelMapper modelMapper) {
        this.parkingSpaceRepo = parkingSpaceRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public ParkingSpaceDTO saveParkingSpace(ParkingSpaceDTO parkingSpaceDTO) {
        ParkingSpace parkingSpace = modelMapper.map(parkingSpaceDTO, ParkingSpace.class);
        ParkingSpace savedParkingSpace = parkingSpaceRepo.save(parkingSpace);
        return modelMapper.map(savedParkingSpace, ParkingSpaceDTO.class);
    }

    @Override
    public ParkingSpaceDTO getParkingSpaceById(Long id) {
        ParkingSpace parkingSpace = parkingSpaceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking Space not found with ID: " + id));
        return modelMapper.map(parkingSpace, ParkingSpaceDTO.class);
    }

    @Override
    public List<ParkingSpaceDTO> getAllParkingSpaces() {
        List<ParkingSpace> parkingSpaces = parkingSpaceRepo.findAll();
        return parkingSpaces.stream()
                .map(parkingSpace -> modelMapper.map(parkingSpace, ParkingSpaceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteParkingSpace(Long id) {
        if (!parkingSpaceRepo.existsById(id)) {
            throw new RuntimeException("Parking Space not found with ID: " + id);
        }
        parkingSpaceRepo.deleteById(id);
    }

    @Override
    public ParkingSpaceDTO updateParkingSpace(Long id, ParkingSpaceDTO parkingSpaceDTO) {
        ParkingSpace existingParkingSpace = parkingSpaceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking Space not found with ID: " + id));

        modelMapper.map(parkingSpaceDTO, existingParkingSpace);
        existingParkingSpace.setId(id);

        ParkingSpace updatedParkingSpace = parkingSpaceRepo.save(existingParkingSpace);
        return modelMapper.map(updatedParkingSpace, ParkingSpaceDTO.class);
    }

    @Override
    public List<ParkingSpaceDTO> getAvailableParkingSpacesByZone(String zone) {
        return parkingSpaceRepo.findByAvailableTrueAndZone(zone).stream()
                .map(parkingSpace -> modelMapper.map(parkingSpace, ParkingSpaceDTO.class))
                .collect(Collectors.toList());
    }

    // --- New Implementations for Requirements ---

    @Override
    public ParkingSpaceDTO reserveParkingSpace(Long id) {
        ParkingSpace parkingSpace = parkingSpaceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking Space not found with ID: " + id));

        if (!parkingSpace.isAvailable()) { // Check if already reserved/occupied
            throw new RuntimeException("Parking Space with ID: " + id + " is already occupied.");
        }
        parkingSpace.setAvailable(false); // Mark as unavailable (reserved)
        ParkingSpace updatedSpace = parkingSpaceRepo.save(parkingSpace);
        return modelMapper.map(updatedSpace, ParkingSpaceDTO.class);
    }

    @Override
    public ParkingSpaceDTO releaseParkingSpace(Long id) {
        ParkingSpace parkingSpace = parkingSpaceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking Space not found with ID: " + id));

        if (parkingSpace.isAvailable()) { // Check if already available
            throw new RuntimeException("Parking Space with ID: " + id + " is already available.");
        }
        parkingSpace.setAvailable(true); // Mark as available
        ParkingSpace updatedSpace = parkingSpaceRepo.save(parkingSpace);
        return modelMapper.map(updatedSpace, ParkingSpaceDTO.class);
    }

    @Override
    public ParkingSpaceDTO updateParkingSpaceStatus(Long id, boolean newStatus) {
        ParkingSpace parkingSpace = parkingSpaceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking Space not found with ID: " + id));

        parkingSpace.setAvailable(newStatus); // Set the new status
        ParkingSpace updatedSpace = parkingSpaceRepo.save(parkingSpace);
        return modelMapper.map(updatedSpace, ParkingSpaceDTO.class);
    }

    @Override
    public List<ParkingSpaceDTO> filterParkingSpaces(String location, String zone, Boolean available, String type) {
        // Use Spring Data JPA Specifications for dynamic query building
        Specification<ParkingSpace> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (location != null && !location.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
            }
            if (zone != null && !zone.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("zone")), "%" + zone.toLowerCase() + "%"));
            }
            if (available != null) {
                predicates.add(cb.equal(root.get("available"), available));
            }
            if (type != null && !type.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("type")), "%" + type.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<ParkingSpace> filteredSpaces = parkingSpaceRepo.findAll(spec);
        return filteredSpaces.stream()
                .map(parkingSpace -> modelMapper.map(parkingSpace, ParkingSpaceDTO.class))
                .collect(Collectors.toList());
    }
}