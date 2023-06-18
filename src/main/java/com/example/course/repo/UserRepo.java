package com.example.course.repo;

import com.example.course.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Integer> {
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
