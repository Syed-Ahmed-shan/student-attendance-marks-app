package com.example.backend.controller;

import com.example.backend.entity.Student;
import com.example.backend.service.AttendanceService;
import com.example.backend.service.MarkService;
import com.example.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class StudentDashboardController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private MarkService markService;
    
    @Autowired
    private StudentRepository studentRepository;
    
    // Get complete dashboard data for a student (for charts and tables)
    @GetMapping("/student/{rollNumber}")
    public ResponseEntity<?> getStudentDashboard(@PathVariable String rollNumber) {
        try {
            // Find student
            Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new RuntimeException("Student not found"));
            
            // Get attendance stats
            Map<String, Object> attendanceStats = attendanceService.getAttendanceStats(student.getId());
            
            // Get all attendance records (for line chart)
            var attendanceRecords = attendanceService.getAttendanceByStudent(student.getId());
            
            // Get all marks (for marks table)
            var marks = markService.getMarksByStudent(student.getId());
            
            // Build response
            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("student", Map.of(
                "id", student.getId(),
                "rollNumber", student.getRollNumber(),
                "name", student.getName(),
                "email", student.getEmail()
            ));
            dashboard.put("attendanceStats", attendanceStats);
            dashboard.put("attendanceRecords", attendanceRecords);
            dashboard.put("marks", marks);
            
            return ResponseEntity.ok(dashboard);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get only attendance stats (for pie chart)
    @GetMapping("/student/{rollNumber}/attendance")
    public ResponseEntity<?> getStudentAttendance(@PathVariable String rollNumber) {
        try {
            Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new RuntimeException("Student not found"));
            
            Map<String, Object> stats = attendanceService.getAttendanceStats(student.getId());
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get only marks (for marks table)
    @GetMapping("/student/{rollNumber}/marks")
    public ResponseEntity<?> getStudentMarks(@PathVariable String rollNumber) {
        try {
            Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new RuntimeException("Student not found"));
            
            var marks = markService.getMarksByStudent(student.getId());
            return ResponseEntity.ok(marks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
}
