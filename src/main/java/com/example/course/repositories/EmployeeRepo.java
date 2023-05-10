package com.example.course.repositories;

import com.example.course.enities.Dentistry;
import com.example.course.enities.Employee;
import com.example.course.enities.Schedule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface EmployeeRepo extends CrudRepository<Employee, Integer> {
    List<Employee> findAllByDentistryOrderByNameAsc(Dentistry dentistry);
    List<Employee> findAllByNameStartingWithOrderByNameAsc(String name);
    @Modifying
    @Transactional
    @Query("delete from Employee b where b.id=:id")
    void deleteEmployee(@Param("id") Integer id);

    Employee findByLogin(String login);

    boolean existsByLogin(String login);

    Employee findByName(String name);

    List<Employee> findEmployeesBySchedule_DateOrderBySchedule_TimeAsc(LocalDate date);
}
