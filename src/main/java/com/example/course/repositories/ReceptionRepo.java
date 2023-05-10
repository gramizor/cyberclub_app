package com.example.course.repositories;

import com.example.course.enities.Patient;
import com.example.course.enities.Reception;
import com.example.course.enities.Registration;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReceptionRepo extends CrudRepository<Reception, Long> {
    Reception findByRegistration(Registration registration);
    boolean existsByRegistration(Registration registration);
}
