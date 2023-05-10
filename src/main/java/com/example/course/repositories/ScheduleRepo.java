package com.example.course.repositories;

import com.example.course.enities.Schedule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ScheduleRepo extends CrudRepository<Schedule, Long> {
    List<Schedule> findByDate(LocalDate date);
}
