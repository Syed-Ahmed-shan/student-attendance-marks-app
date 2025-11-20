package com.example.backend.controller;

import com.example.backend.entity.Subject;
import com.example.backend.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectController {
    
    @Autowired
    private SubjectService subjectService;
    
    // Get all subjects
    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }
    
    // Get subject by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSubjectById(@PathVariable Long id) {
        try {
            Subject subject = subjectService.getSubjectById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
            return ResponseEntity.ok(subject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get subjects by lecturer
    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<List<Subject>> getSubjectsByLecturer(@PathVariable Long lecturerId) {
        return ResponseEntity.ok(subjectService.getSubjectsByLecturer(lecturerId));
    }
    
    // Create subject
    @PostMapping
    public ResponseEntity<?> createSubject(@RequestBody Map<String, Object> request) {
        try {
            String name = (String) request.get("name");
            String code = (String) request.get("code");
            Long lecturerId = Long.valueOf(request.get("lecturerId").toString());
            
            Subject subject = subjectService.createSubject(name, code, lecturerId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Subject created successfully");
            response.put("subject", subject);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Update subject
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubject(@PathVariable Long id,
                                          @RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String code = request.get("code");
            
            Subject updated = subjectService.updateSubject(id, name, code);
            return ResponseEntity.ok(Map.of("message", "Subject updated", "subject", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Delete subject
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable Long id) {
        try {
            subjectService.deleteSubject(id);
            return ResponseEntity.ok(Map.of("message", "Subject deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
}
