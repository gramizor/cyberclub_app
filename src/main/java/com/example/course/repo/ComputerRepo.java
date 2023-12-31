package com.example.course.repo;

import com.example.course.entities.Computer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ComputerRepo extends CrudRepository<Computer, Integer> {
    boolean existsByNumber(Integer number);
    Computer findByNumber(int number);

    @Query("SELECT c.number FROM Computer c order by c.number asc")
    List<Integer> getAllComputerNumbers();
    List<Computer> findByStatusOrderByNumberAsc(String status);
    @Query("SELECT DISTINCT c.status FROM Computer c")
    List<String> getAllStatus();
    @Query("SELECT DISTINCT c.cost FROM Computer c")
    List<String> getAllCost();
}
