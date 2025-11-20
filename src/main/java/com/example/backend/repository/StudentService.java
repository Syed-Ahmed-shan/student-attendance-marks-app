package com.example.backend.service;
import com.example.backend.entity.Student;
import com.example.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository repo;
    public StudentService(StudentRepository repo) { this.repo = repo; }
    // business logic methods here
    
}
