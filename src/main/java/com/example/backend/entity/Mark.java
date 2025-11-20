package com.example.backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mark")
public class Mark {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore  // Prevent circular reference
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    @JsonIgnore  // Prevent circular reference
    private Subject subject;

    @Column(nullable = false)
    private Integer marks;

    @Column(nullable = false)
    private String examType;

    // Constructors, getters, setters (keep existing)
    public Mark() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public Integer getMarks() { return marks; }
    public void setMarks(Integer marks) { this.marks = marks; }

    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
}
