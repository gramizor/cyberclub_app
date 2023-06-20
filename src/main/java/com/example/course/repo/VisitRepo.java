package com.example.course.repo;

import com.example.course.entities.Computer;
import com.example.course.entities.Game;
import com.example.course.entities.User;
import com.example.course.entities.Visit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VisitRepo extends CrudRepository<Visit, Integer> {
    Visit save(Visit visit);
    boolean existsByUser(User user);
    //Visit  findByEndTime(String endTime);
    List<Visit> findByUserAndEndTimeIsNotNull(User user);
}
