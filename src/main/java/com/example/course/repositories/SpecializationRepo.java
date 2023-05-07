package com.example.course.repositories;

import com.example.course.enities.Specialization;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpecializationRepo extends CrudRepository<Specialization, Long> {
    Specialization findByName(String name);
    List<Specialization> findAllByNameStartingWith(String str);
}
