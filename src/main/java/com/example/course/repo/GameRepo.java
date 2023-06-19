package com.example.course.repo;

import com.example.course.entities.Computer;
import com.example.course.entities.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameRepo extends CrudRepository<Game, Integer> {
    List<Game> findAll();
    List<Game> findByName(String nameGame);
}
