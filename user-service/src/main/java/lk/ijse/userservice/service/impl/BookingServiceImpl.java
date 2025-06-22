package lk.ijse.userservice.service.impl;

import lk.ijse.userservice.dto.BookingDTO;
import lk.ijse.userservice.entity.Booking;
import lk.ijse.userservice.entity.User;
import lk.ijse.userservice.repo.BookingRepo;
import lk.ijse.userservice.repo.UserRepo;
import lk.ijse.userservice.service.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional // Ensures atomicity of operations
public class BookingServiceImpl implements BookingService {

    private final BookingRepo bookingRepo;
    private final UserRepo userRepo; // Needed to find the User entity
    private final ModelMapper modelMapper;

    @Autowired
    public BookingServiceImpl(BookingRepo bookingRepo, UserRepo userRepo, ModelMapper modelMapper) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        // Find the User entity by userId from DTO
        User user = userRepo.findById(bookingDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + bookingDTO.getUserId()));

        Booking booking = modelMapper.map(bookingDTO, Booking.class);
        booking.setUser(user); // Set the User entity
        Booking savedBooking = bookingRepo.save(booking);
        return modelMapper.map(savedBooking, BookingDTO.class);
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        Optional<Booking> bookingOptional = bookingRepo.findById(id);
        return bookingOptional.map(booking -> modelMapper.map(booking, BookingDTO.class))
                .orElse(null); // Or throw an exception
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        return bookingRepo.findAll().stream()
                .map(booking -> {
                    BookingDTO dto = modelMapper.map(booking, BookingDTO.class);
                    dto.setUserId(booking.getUser().getId()); // Set userId in DTO
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByUserId(Long userId) {
        // Ensure the user exists before fetching bookings
        if (!userRepo.existsById(userId)) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        return bookingRepo.findByUserId(userId).stream()
                .map(booking -> {
                    BookingDTO dto = modelMapper.map(booking, BookingDTO.class);
                    dto.setUserId(booking.getUser().getId()); // Set userId in DTO
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking existingBooking = bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        // Update fields from DTO, ensuring user is not changed or handled carefully
        if (bookingDTO.getVehicleNumber() != null) existingBooking.setVehicleNumber(bookingDTO.getVehicleNumber());
        if (bookingDTO.getBookingTime() != null) existingBooking.setBookingTime(bookingDTO.getBookingTime());
        if (bookingDTO.getCheckInTime() != null) existingBooking.setCheckInTime(bookingDTO.getCheckInTime());
        if (bookingDTO.getCheckOutTime() != null) existingBooking.setCheckOutTime(bookingDTO.getCheckOutTime());
        if (bookingDTO.getParkingSpotId() != null) existingBooking.setParkingSpotId(bookingDTO.getParkingSpotId());
        if (bookingDTO.getTotalCost() > 0) existingBooking.setTotalCost(bookingDTO.getTotalCost());
        if (bookingDTO.getStatus() != null) existingBooking.setStatus(bookingDTO.getStatus());
        if (bookingDTO.getRemarks() != null) existingBooking.setRemarks(bookingDTO.getRemarks());

        // Note: Changing `userId` for an existing booking is generally not allowed via a simple update.
        // If it's necessary, you would need to fetch the new User and set it,
        // potentially requiring specific business rules.

        Booking updatedBooking = bookingRepo.save(existingBooking);
        return modelMapper.map(updatedBooking, BookingDTO.class);
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepo.existsById(id)) {
            throw new RuntimeException("Booking not found with ID: " + id);
        }
        bookingRepo.deleteById(id);
    }
}