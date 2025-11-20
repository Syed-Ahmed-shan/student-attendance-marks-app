package com.example.backend.service;

import com.example.backend.entity.Subject;
import com.example.backend.entity.Lecturer;
import com.example.backend.repository.SubjectRepository;
import com.example.backend.repository.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private LecturerRepository lecturerRepository;
    
    // Get all subjects
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }
    
    // Get subject by ID
    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }
    
    // Get subjects by lecturer
    public List<Subject> getSubjectsByLecturer(Long lecturerId) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
            .orElseThrow(() -> new RuntimeException("Lecturer not found"));
        return subjectRepository.findByLecturer(lecturer);
    }
    
    // Create new subject
    public Subject createSubject(String name, String code, Long lecturerId) {
        if (subjectRepository.existsByCode(code)) {
            throw new RuntimeException("Subject with this code already exists");
        }
        
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
            .orElseThrow(() -> new RuntimeException("Lecturer not found with id: " + lecturerId));
        
        Subject subject = new Subject();
        subject.setName(name);
        subject.setCode(code);
        subject.setLecturer(lecturer);
        return subjectRepository.save(subject);
    }
    
    // Update subject
    public Subject updateSubject(Long id, String name, String code) {
        Subject subject = subjectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
        
        subject.setName(name);
        subject.setCode(code);
        
        return subjectRepository.save(subject);
    }
    
    // Delete subject
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
        subjectRepository.delete(subject);
    }
}
