package lk.ijse.userservice.service.impl;

import lk.ijse.userservice.dto.AuthRequestDTO;
import lk.ijse.userservice.dto.AuthResponseDTO;
import lk.ijse.userservice.dto.UserDTO;
import lk.ijse.userservice.entity.User;
import lk.ijse.userservice.repo.UserRepo;
import lk.ijse.userservice.service.UserService;
import lk.ijse.userservice.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy; // Import Lazy
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager; // Make this @Lazy
    private final JwtUtil jwtUtil;

    // Use @Lazy for AuthenticationManager to break the cycle during bean initialization
    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder,
                           @Lazy AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepo.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists: " + userDTO.getUsername());
        }
        if (userRepo.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + userDTO.getEmail());
        }

        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole() != null ? userDTO.getRole().toUpperCase() : "USER");

        User savedUser = userRepo.save(user);
        UserDTO responseDTO = modelMapper.map(savedUser, UserDTO.class);
        responseDTO.setPassword(null);
        return responseDTO;
    }

    @Override
    public AuthResponseDTO authenticateUser(AuthRequestDTO authRequest) {
        // This is where AuthenticationManager is used, which is now lazily initialized
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateToken(authRequest.getUsername());

        User user = userRepo.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found after authentication: " + authRequest.getUsername()));

        return new AuthResponseDTO(token, user.getId(), user.getUsername(), user.getRole());
    }

    // ... (rest of the UserServiceImpl methods remain the same) ...

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        UserDTO responseDTO = modelMapper.map(user, UserDTO.class);
        responseDTO.setPassword(null);
        return responseDTO;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        UserDTO responseDTO = modelMapper.map(user, UserDTO.class);
        responseDTO.setPassword(null);
        return responseDTO;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(user -> {
                    UserDTO dto = modelMapper.map(user, UserDTO.class);
                    dto.setPassword(null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        if (userDTO.getEmail() != null) existingUser.setEmail(userDTO.getEmail());
        if (userDTO.getFirstName() != null) existingUser.setFirstName(userDTO.getFirstName());
        if (userDTO.getLastName() != null) existingUser.setLastName(userDTO.getLastName());
        if (userDTO.getContactNumber() != null) existingUser.setContactNumber(userDTO.getContactNumber());
        if (userDTO.getRole() != null) existingUser.setRole(userDTO.getRole().toUpperCase());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        User updatedUser = userRepo.save(existingUser);
        UserDTO responseDTO = modelMapper.map(updatedUser, UserDTO.class);
        responseDTO.setPassword(null);
        return responseDTO;
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepo.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().split(","))
                .build();
    }
}