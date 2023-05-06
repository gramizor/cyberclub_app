package com.example.course.repositories;

import com.example.course.enities.Access;
import org.springframework.data.repository.CrudRepository;

public interface AccessRepo extends CrudRepository<Access, Long> {
    Access findByName(String name);
}
