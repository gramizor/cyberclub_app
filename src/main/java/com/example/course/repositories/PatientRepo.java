package com.example.course.repositories;

import com.example.course.enities.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PatientRepo extends CrudRepository<Patient, Long> {
    List<Patient> findPatientsByNameContainingIgnoreCase(String str);
    boolean existsByName(String name);
}
