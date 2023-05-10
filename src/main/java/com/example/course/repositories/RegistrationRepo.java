package com.example.course.repositories;

import com.example.course.enities.Registration;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface RegistrationRepo extends CrudRepository<Registration, Long> {
    List<Registration> findByDateAndEmployee_NameOrderByTimeAsc(LocalDate date, String name);
    List<Registration> findByPatient_IdOrderByDateDesc(Long id);
}
