package com.example.course.repo;

import com.example.course.entities.Computer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ComputerRepo extends CrudRepository<Computer, Integer> {
    boolean existsByNumber(Integer number);
    List<Computer> findByNumber(Integer number);

    @Query("SELECT c.number FROM Computer c")
    List<Integer> getAllComputerNumbers();
<<<<<<< HEAD
=======
    @Query("SELECT DISTINCT c.status FROM Computer c")
    List<String> getAllStatus();
>>>>>>> f4fd03f (bruh3)
}
