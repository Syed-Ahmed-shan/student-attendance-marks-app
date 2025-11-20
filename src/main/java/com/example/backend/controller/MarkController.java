package com.example.backend.controller;

import com.example.backend.entity.Mark;
import com.example.backend.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marks")
@CrossOrigin(origins = "*")
public class MarkController {
    
    @Autowired
    private MarkService markService;
    
    // Add or update marks
    @PostMapping
    public ResponseEntity<?> addOrUpdateMark(@RequestBody Map<String, Object> request) {
        try {
            Long studentId = Long.valueOf(request.get("studentId").toString());
            Long subjectId = Long.valueOf(request.get("subjectId").toString());
            Integer marks = Integer.valueOf(request.get("marks").toString());
            String examType = (String) request.get("examType");
            
            Mark mark = markService.addOrUpdateMark(studentId, subjectId, marks, examType);
            
            return ResponseEntity.ok(Map.of(
                "message", "Marks saved successfully",
                "mark", mark
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get marks for a student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getMarksByStudent(@PathVariable Long studentId) {
        try {
            List<Mark> marks = markService.getMarksByStudent(studentId);
            return ResponseEntity.ok(marks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get all marks
    @GetMapping
    public ResponseEntity<List<Mark>> getAllMarks() {
        return ResponseEntity.ok(markService.getAllMarks());
    }
    
    // Delete mark
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMark(@PathVariable Long id) {
        try {
            markService.deleteMark(id);
            return ResponseEntity.ok(Map.of("message", "Mark deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
}
