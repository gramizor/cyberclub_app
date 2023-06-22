package com.example.course.repo;

import com.example.course.entities.Computer;
import com.example.course.entities.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Time;
import java.util.List;

public interface ReservationRepo extends CrudRepository<Reservation, Integer> {
}
