package com.example.course.repositories;

import com.example.course.enities.Access;
import com.example.course.enities.Specialization;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccessRepo extends CrudRepository<Access, Long> {
    Access findByName(String name);
    List<Access> findAllByNameStartingWith(String str);
}
