package com.example.backend.security;

import com.example.backend.entity.Lecturer;
import com.example.backend.entity.Student;
import com.example.backend.repository.LecturerRepository;
import com.example.backend.repository.StudentRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final StudentRepository studentRepo;
    private final LecturerRepository lecturerRepo;

    public UserDetailsServiceImpl(StudentRepository studentRepo, LecturerRepository lecturerRepo) {
        this.studentRepo = studentRepo; this.lecturerRepo = lecturerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try student by roll number
        return studentRepo.findByRollNumber(username).map(
            s -> new MyUserDetails(s.getRollNumber(), s.getPassword(), "STUDENT")
        ).orElseGet(() ->
           lecturerRepo.findByEmail(username)
             .map(l -> new MyUserDetails(l.getEmail(), l.getPassword(), "LECTURER"))
             .orElseThrow(() -> new UsernameNotFoundException("User not found"))
        );
    }
}
