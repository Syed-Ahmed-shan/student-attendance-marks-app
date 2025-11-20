package com.example.backend.repository;

import com.example.backend.entity.Attendance;
import com.example.backend.entity.Student;
import com.example.backend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    // Find all attendance records for a student
    List<Attendance> findByStudent(Student student);
    
    // Find all attendance records for a subject
    List<Attendance> findBySubject(Subject subject);
    
    // Find attendance for a specific student and subject
    List<Attendance> findByStudentAndSubject(Student student, Subject subject);
    
    // Find attendance for a specific date
    List<Attendance> findByDate(LocalDate date);
    
    // Check if attendance exists for student on a specific date and subject
    Optional<Attendance> findByStudentAndSubjectAndDate(Student student, Subject subject, LocalDate date);
    
    // Count total attendance records for a student
    long countByStudent(Student student);
    
    // Count present days for a student
    long countByStudentAndPresentTrue(Student student);
    
    // Count present days for a student in a specific subject
    long countByStudentAndSubjectAndPresentTrue(Student student, Subject subject);
    
    // Count total days for a student in a specific subject
    long countByStudentAndSubject(Student student, Subject subject);
    
    // Custom query to get attendance percentage
    @Query("SELECT (COUNT(a) * 100.0 / (SELECT COUNT(a2) FROM Attendance a2 WHERE a2.student = :student)) " +
           "FROM Attendance a WHERE a.student = :student AND a.present = true")
    Double getAttendancePercentage(@Param("student") Student student);
}
