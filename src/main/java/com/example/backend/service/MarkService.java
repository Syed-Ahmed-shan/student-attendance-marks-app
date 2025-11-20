package com.example.backend.service;

import com.example.backend.entity.Mark;
import com.example.backend.entity.Student;
import com.example.backend.entity.Subject;
import com.example.backend.repository.MarkRepository;
import com.example.backend.repository.StudentRepository;
import com.example.backend.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MarkService {
    
    @Autowired
    private MarkRepository markRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    // Add or update marks
    public Mark addOrUpdateMark(Long studentId, Long subjectId, Integer marks, String examType) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new RuntimeException("Subject not found"));
        
        // Check if mark already exists
        var existingMark = markRepository
            .findByStudentAndSubjectAndExamType(student, subject, examType);
        
        if (existingMark.isPresent()) {
            // Update existing mark
            Mark mark = existingMark.get();
            mark.setMarks(marks);
            return markRepository.save(mark);
        } else {
            // Create new mark
            Mark mark = new Mark();
            mark.setStudent(student);
            mark.setSubject(subject);
            mark.setMarks(marks);
            mark.setExamType(examType);
            return markRepository.save(mark);
        }
    }
    
    // Get marks for a student
    public List<Mark> getMarksByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        return markRepository.findByStudent(student);
    }
    
    // Get all marks
    public List<Mark> getAllMarks() {
        return markRepository.findAll();
    }
    
    // Delete mark
    public void deleteMark(Long markId) {
        Mark mark = markRepository.findById(markId)
            .orElseThrow(() -> new RuntimeException("Mark not found"));
        markRepository.delete(mark);
    }
}
