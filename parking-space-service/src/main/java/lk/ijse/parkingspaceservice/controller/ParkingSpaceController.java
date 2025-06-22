package lk.ijse.parkingspaceservice.controller;

import lk.ijse.parkingspaceservice.dto.ParkingSpaceDTO;
import lk.ijse.parkingspaceservice.dto.ResponseDTO;
import lk.ijse.parkingspaceservice.service.ParkingSpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("api/v1/parking-spaces")
@CrossOrigin
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;

    public ParkingSpaceController(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveParkingSpace(@Valid @RequestBody ParkingSpaceDTO parkingSpaceDTO) {
        ParkingSpaceDTO savedSpace = parkingSpaceService.saveParkingSpace(parkingSpaceDTO);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.CREATED.value(),
                "Parking Space Saved Successfully",
                savedSpace
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getParkingSpaceById(@PathVariable Long id) {
        ParkingSpaceDTO parkingSpace = parkingSpaceService.getParkingSpaceById(id);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Parking Space Retrieved Successfully",
                parkingSpace
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getAllParkingSpaces() {
        List<ParkingSpaceDTO> parkingSpaces = parkingSpaceService.getAllParkingSpaces();
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "All Parking Spaces Retrieved Successfully",
                parkingSpaces
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateParkingSpace(@PathVariable Long id, @Valid @RequestBody ParkingSpaceDTO parkingSpaceDTO) {
        ParkingSpaceDTO updatedSpace = parkingSpaceService.updateParkingSpace(id, parkingSpaceDTO);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Parking Space Updated Successfully",
                updatedSpace
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteParkingSpace(@PathVariable Long id) {
        parkingSpaceService.deleteParkingSpace(id);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(), // Changed to OK for consistent body
                "Parking Space Deleted Successfully",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/available-by-zone")
    public ResponseEntity<ResponseDTO> getAvailableParkingSpacesByZone(@RequestParam String zone) {
        List<ParkingSpaceDTO> availableSpaces = parkingSpaceService.getAvailableParkingSpacesByZone(zone);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Available Parking Spaces by Zone Retrieved Successfully",
                availableSpaces
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // --- New Endpoints for Requirements ---

    @PatchMapping("/{id}/reserve") // PATCH is suitable for partial updates like status changes
    public ResponseEntity<ResponseDTO> reserveParkingSpace(@PathVariable Long id) {
        ParkingSpaceDTO reservedSpace = parkingSpaceService.reserveParkingSpace(id);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Parking Space Reserved Successfully",
                reservedSpace
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/release") // PATCH is suitable for partial updates like status changes
    public ResponseEntity<ResponseDTO> releaseParkingSpace(@PathVariable Long id) {
        ParkingSpaceDTO releasedSpace = parkingSpaceService.releaseParkingSpace(id);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Parking Space Released Successfully",
                releasedSpace
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status") // Generic endpoint for status update
    public ResponseEntity<ResponseDTO> updateParkingSpaceStatus(@PathVariable Long id, @RequestParam boolean status) {
        ParkingSpaceDTO updatedSpace = parkingSpaceService.updateParkingSpaceStatus(id, status);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Parking Space Status Updated Successfully",
                updatedSpace
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/filter") // More generic filtering endpoint
    public ResponseEntity<ResponseDTO> filterParkingSpaces(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) String type) {
        List<ParkingSpaceDTO> filteredSpaces = parkingSpaceService.filterParkingSpaces(location, zone, available, type);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Filtered Parking Spaces Retrieved Successfully",
                filteredSpaces
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}