package com.example.backend.controller;

import com.example.backend.entity.Attendance;
import com.example.backend.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    // Mark attendance
    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(@RequestBody Map<String, Object> request) {
        try {
            Long studentId = Long.valueOf(request.get("studentId").toString());
            Long subjectId = Long.valueOf(request.get("subjectId").toString());
            LocalDate date = LocalDate.parse(request.get("date").toString());
            Boolean present = Boolean.valueOf(request.get("present").toString());
            
            Attendance attendance = attendanceService.markAttendance(studentId, subjectId, date, present);
            
            return ResponseEntity.ok(Map.of(
                "message", "Attendance marked successfully",
                "attendance", attendance
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get attendance for a student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getAttendanceByStudent(@PathVariable Long studentId) {
        try {
            List<Attendance> attendance = attendanceService.getAttendanceByStudent(studentId);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get attendance stats
    @GetMapping("/stats/{studentId}")
    public ResponseEntity<?> getAttendanceStats(@PathVariable Long studentId) {
        try {
            Map<String, Object> stats = attendanceService.getAttendanceStats(studentId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get all attendance
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }
}
