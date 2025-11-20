package com.example.backend.repository;

import com.example.backend.entity.Mark;
import com.example.backend.entity.Student;
import com.example.backend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
    
    // Find all marks for a student
    List<Mark> findByStudent(Student student);
    
    // Find all marks for a subject
    List<Mark> findBySubject(Subject subject);
    
    // Find marks for a specific student and subject
    List<Mark> findByStudentAndSubject(Student student, Subject subject);
    
    // Find marks for a specific exam type
    List<Mark> findByExamType(String examType);
    
    // Find marks for a student and exam type
    List<Mark> findByStudentAndExamType(Student student, String examType);
    
    // Check if mark exists for student, subject, and exam type
    Optional<Mark> findByStudentAndSubjectAndExamType(Student student, Subject subject, String examType);
}
