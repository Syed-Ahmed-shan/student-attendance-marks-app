package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
public class Mark {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    private int marks;
    private String examType; // e.g. "Midterm", "Final"
    // getters & setters
}
