package com.example.backend.controller;
import com.example.backend.entity.Student;
import com.example.backend.service.StudentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService service;
    public StudentController(StudentService service) { this.service = service; }
    // endpoints
}
