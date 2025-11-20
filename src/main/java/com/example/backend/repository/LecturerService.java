package com.example.backend.service;

import com.example.backend.repository.LecturerRepository;
import org.springframework.stereotype.Service;

@Service
public class LecturerService {

    private final LecturerRepository repo;

    public LecturerService(LecturerRepository repo) {
        this.repo = repo;
    }

    // future methods:
    // save lecturer
    // get lecturer by id
    // assign subjects
    // update lecturer details
}
