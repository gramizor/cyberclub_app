package com.example.course.repositories;

import com.example.course.enities.Dentistry;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DentistryRepo extends CrudRepository<Dentistry, Long> {
    List<Dentistry> findAllByAddressIsStartingWith(String str);
    Dentistry findByAddress(String address);
}
