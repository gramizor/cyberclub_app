package com.example.course.repositories;

import com.example.course.enities.Patient;
import com.example.course.enities.Procedure;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProcedureRepo extends CrudRepository<Procedure, Long> {
    List<Procedure> findAllByNameContainsOrderByNameAsc(String name);
    Procedure findByName(String name);

    @Modifying
    @Transactional
    @Query("delete from Procedure b where b.id=:id")
    void deleteProcedure(@Param("id") Long id);

    List<Procedure> findByNameContainingIgnoreCase(String str);

    List<Procedure> findByReception_Registration_Id(Long id);
}
