package com.example.course.repositories;

import com.example.course.enities.Dentistry;
import com.example.course.enities.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepo extends CrudRepository<Employee, Integer> {
    List<Employee> findAllByDentistryOrderByNameAsc(Dentistry dentistry);
    @Modifying
    @Transactional
    @Query("delete from Employee b where b.id=:id")
    void deleteEmployee(@Param("id") Integer id);
}
