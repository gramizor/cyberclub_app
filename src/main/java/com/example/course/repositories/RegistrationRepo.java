package com.example.course.repositories;

import com.example.course.enities.Registration;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RegistrationRepo extends CrudRepository<Registration, Long> {
    List<Registration> findByDateAndEmployee_NameOrderByTimeAsc(LocalDate date, String name);
    List<Registration> findByPatient_IdOrderByDateDesc(Long id);

    List<Registration> findByDateOrderByTimeAsc(LocalDate date);

    boolean existsById(Long id);

    @Modifying
    @Transactional
    @Query("delete from Registration b where b.id=:id")
    void deleteRegistration(@Param("id") Long id);
}
