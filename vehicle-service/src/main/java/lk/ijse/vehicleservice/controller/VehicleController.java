package lk.ijse.vehicleservice.controller;

import lk.ijse.vehicleservice.dto.ResponseDTO; // Important: use the ResponseDTO from THIS service
import lk.ijse.vehicleservice.dto.VehicleDTO;
import lk.ijse.vehicleservice.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController // Marks this class as a REST Controller
@RequestMapping("api/v1/vehicles") // Base path for all endpoints in this controller
@CrossOrigin // Allows cross-origin requests
public class VehicleController {

    private final VehicleService vehicleService;

    // Constructor injection for VehicleService
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // Endpoint to register a new vehicle
    @PostMapping
    public ResponseEntity<ResponseDTO> saveVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO savedVehicle = vehicleService.saveVehicle(vehicleDTO);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.CREATED.value(),
                "Vehicle Registered Successfully",
                savedVehicle
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint to retrieve vehicle details by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getVehicleById(@PathVariable Long id) {
        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Vehicle Retrieved Successfully",
                vehicle
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to retrieve all vehicle details
    @GetMapping
    public ResponseEntity<ResponseDTO> getAllVehicles() {
        List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "All Vehicles Retrieved Successfully",
                vehicles
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to update vehicle details
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO updatedVehicle = vehicleService.updateVehicle(id, vehicleDTO);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Vehicle Updated Successfully",
                updatedVehicle
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to delete a vehicle
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Vehicle Deleted Successfully",
                null // No data to return on successful deletion
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to get vehicles linked to a specific user
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<ResponseDTO> getVehiclesByUserId(@PathVariable Long userId) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByUserId(userId);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Vehicles for User ID " + userId + " Retrieved Successfully",
                vehicles
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to record vehicle entry
    @PatchMapping("/{id}/entry")
    public ResponseEntity<ResponseDTO> recordVehicleEntry(@PathVariable Long id) {
        VehicleDTO updatedVehicle = vehicleService.recordVehicleEntry(id);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Vehicle Entry Recorded Successfully",
                updatedVehicle
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to record vehicle exit
    @PatchMapping("/{id}/exit")
    public ResponseEntity<ResponseDTO> recordVehicleExit(@PathVariable Long id) {
        VehicleDTO updatedVehicle = vehicleService.recordVehicleExit(id);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Vehicle Exit Recorded Successfully",
                updatedVehicle
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to update vehicle status generically (e.g., "IN", "OUT", "PARKED")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO> updateVehicleStatus(@PathVariable Long id, @RequestParam String status) {
        VehicleDTO updatedVehicle = vehicleService.updateVehicleStatus(id, status);
        ResponseDTO response = new ResponseDTO(
                HttpStatus.OK.value(),
                "Vehicle Status Updated Successfully to " + status,
                updatedVehicle
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}