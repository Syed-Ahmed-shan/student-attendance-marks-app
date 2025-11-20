package com.example.backend.service;

import com.example.backend.entity.Lecturer;
import com.example.backend.repository.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LecturerService {
    
    @Autowired
    private LecturerRepository lecturerRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Get all lecturers
    public List<Lecturer> getAllLecturers() {
        return lecturerRepository.findAll();
    }
    
    // Get lecturer by ID
    public Optional<Lecturer> getLecturerById(Long id) {
        return lecturerRepository.findById(id);
    }
    
    // Get lecturer by email
    public Optional<Lecturer> getLecturerByEmail(String email) {
        return lecturerRepository.findByEmail(email);
    }
    
    // Create new lecturer
    public Lecturer createLecturer(Lecturer lecturer) {
        if (lecturerRepository.existsByEmail(lecturer.getEmail())) {
            throw new RuntimeException("Lecturer with this email already exists");
        }
        lecturer.setPassword(passwordEncoder.encode(lecturer.getPassword()));
        return lecturerRepository.save(lecturer);
    }
    
    // Update lecturer
    public Lecturer updateLecturer(Long id, Lecturer lecturerDetails) {
        Lecturer lecturer = lecturerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lecturer not found with id: " + id));
        
        lecturer.setName(lecturerDetails.getName());
        lecturer.setEmail(lecturerDetails.getEmail());
        
        if (lecturerDetails.getPassword() != null && !lecturerDetails.getPassword().isEmpty()) {
            lecturer.setPassword(passwordEncoder.encode(lecturerDetails.getPassword()));
        }
        
        return lecturerRepository.save(lecturer);
    }
    
    // Delete lecturer
    public void deleteLecturer(Long id) {
        Lecturer lecturer = lecturerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lecturer not found with id: " + id));
        lecturerRepository.delete(lecturer);
    }
}
