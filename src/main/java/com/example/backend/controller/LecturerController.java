package com.example.backend.controller;

import com.example.backend.entity.Lecturer;
import com.example.backend.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lecturers")
@CrossOrigin(origins = "*")
public class LecturerController {
    
    @Autowired
    private LecturerService lecturerService;
    
    // Get all lecturers
    @GetMapping
    public ResponseEntity<List<Lecturer>> getAllLecturers() {
        return ResponseEntity.ok(lecturerService.getAllLecturers());
    }
    
    // Get lecturer by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getLecturerById(@PathVariable Long id) {
        try {
            Lecturer lecturer = lecturerService.getLecturerById(id)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));
            return ResponseEntity.ok(lecturer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Create lecturer
    @PostMapping
    public ResponseEntity<?> createLecturer(@RequestBody Map<String, String> request) {
        try {
            Lecturer lecturer = new Lecturer();
            lecturer.setName(request.get("name"));
            lecturer.setEmail(request.get("email"));
            lecturer.setPassword(request.get("password"));
            
            Lecturer savedLecturer = lecturerService.createLecturer(lecturer);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Lecturer created successfully");
            response.put("lecturer", savedLecturer);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Update lecturer
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLecturer(@PathVariable Long id, 
                                           @RequestBody Lecturer lecturerDetails) {
        try {
            Lecturer updated = lecturerService.updateLecturer(id, lecturerDetails);
            return ResponseEntity.ok(Map.of("message", "Lecturer updated", "lecturer", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Delete lecturer
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLecturer(@PathVariable Long id) {
        try {
            lecturerService.deleteLecturer(id);
            return ResponseEntity.ok(Map.of("message", "Lecturer deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
}
