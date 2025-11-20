package com.example.backend.controller;

import com.example.backend.security.JwtUtil;
import com.example.backend.security.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entity.Student;
import com.example.backend.repository.StudentRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")  // Allow all origins for testing (restrict in production)
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepo;

    public AuthController(
            JwtUtil jwtUtil,
            UserDetailsServiceImpl userDetailsService,
            PasswordEncoder passwordEncoder,
            StudentRepository studentRepo
    ) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.studentRepo = studentRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        try {
            String username = req.get("username");
            String password = req.get("password");

            // Validate input
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username is required"));
            }
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Password is required"));
            }

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Verify password
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(
                    userDetails.getUsername(),
                    userDetails.getAuthorities().iterator().next().getAuthority()
            );

            // Success response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
            response.put("username", userDetails.getUsername());
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "User not found"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An error occurred: " + e.getMessage()));
        }
    }

    @PostMapping("/student/register")
    public ResponseEntity<?> registerStudent(@RequestBody Map<String, String> req) {
        try {
            // Validate input
            String rollNumber = req.get("rollNumber");
            String name = req.get("name");
            String email = req.get("email");
            String password = req.get("password");

            if (rollNumber == null || rollNumber.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Roll number is required"));
            }
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Name is required"));
            }
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email is required"));
            }
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Password is required"));
            }

            // Check if student already exists
            if (studentRepo.findByRollNumber(rollNumber).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Student with this roll number already exists"));
            }

            // Create and save student
            Student student = new Student();
            student.setRollNumber(rollNumber);
            student.setName(name);
            student.setEmail(email);
            student.setPassword(passwordEncoder.encode(password));
            studentRepo.save(student);

            // Success response
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Student registered successfully");
            response.put("rollNumber", rollNumber);
            response.put("name", name);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    // Optional: Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "message", "Auth service is running"
        ));
    }
}
