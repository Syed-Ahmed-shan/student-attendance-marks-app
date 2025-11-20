package com.example.backend.service;

import com.example.backend.entity.Attendance;
import com.example.backend.entity.Student;
import com.example.backend.entity.Subject;
import com.example.backend.repository.AttendanceRepository;
import com.example.backend.repository.StudentRepository;
import com.example.backend.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class AttendanceService {
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    // Mark attendance
    public Attendance markAttendance(Long studentId, Long subjectId, LocalDate date, Boolean present) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new RuntimeException("Subject not found"));
        
        // Check if attendance already exists for this date
        var existingAttendance = attendanceRepository
            .findByStudentAndSubjectAndDate(student, subject, date);
        
        if (existingAttendance.isPresent()) {
            // Update existing attendance
            Attendance attendance = existingAttendance.get();
            attendance.setPresent(present);
            return attendanceRepository.save(attendance);
        } else {
            // Create new attendance record
            Attendance attendance = new Attendance();
            attendance.setStudent(student);
            attendance.setSubject(subject);
            attendance.setDate(date);
            attendance.setPresent(present);
            return attendanceRepository.save(attendance);
        }
    }
    
    // Get attendance for a student
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        return attendanceRepository.findByStudent(student);
    }
    
    // Get attendance percentage for a student
    public Map<String, Object> getAttendanceStats(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        long totalDays = attendanceRepository.countByStudent(student);
        long presentDays = attendanceRepository.countByStudentAndPresentTrue(student);
        
        double percentage = totalDays > 0 ? (presentDays * 100.0 / totalDays) : 0.0;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDays", totalDays);
        stats.put("presentDays", presentDays);
        stats.put("absentDays", totalDays - presentDays);
        stats.put("percentage", Math.round(percentage * 100.0) / 100.0);
        
        return stats;
    }
    
    // Get all attendance records
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
}
