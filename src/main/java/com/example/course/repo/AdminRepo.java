package com.example.course.repo;

import com.example.course.entities.Admin;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepo extends CrudRepository<Admin, Integer> {
    boolean existsByUsername(String username);
    Admin findByUsername(String username);
}
