package com.example.course.repositories;

import com.example.course.enities.Procedure;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProcedureRepo extends CrudRepository<Procedure, Long> {
    List<Procedure> findAllByNameContains(String name);
    Procedure findByName(String name);
}
