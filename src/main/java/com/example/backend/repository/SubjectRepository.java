package com.example.backend.repository;

import com.example.backend.entity.Subject;
import com.example.backend.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByCode(String code);
    List<Subject> findByLecturer(Lecturer lecturer);
    boolean existsByCode(String code);
}
