package com.example.backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "subject")
public class Subject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    @JsonIgnore  // Prevent circular reference
    private Lecturer lecturer;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @JsonIgnore  // Prevent circular reference
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @JsonIgnore  // Prevent circular reference
    private List<Mark> marks;

    // Constructors, getters, setters (keep existing)
    public Subject() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Lecturer getLecturer() { return lecturer; }
    public void setLecturer(Lecturer lecturer) { this.lecturer = lecturer; }

    public List<Attendance> getAttendances() { return attendances; }
    public void setAttendances(List<Attendance> attendances) { this.attendances = attendances; }

    public List<Mark> getMarks() { return marks; }
    public void setMarks(List<Mark> marks) { this.marks = marks; }
}
