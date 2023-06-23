package com.example.course.repo;

import com.example.course.entities.PaymentHistory;
import com.example.course.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepo extends CrudRepository<PaymentHistory, Integer> {
    @Override
    PaymentHistory save(PaymentHistory payment);
    List<PaymentHistory> findByUserOrderByDateDesc(User user);
    List<PaymentHistory> findByUserIsNotNullOrderByDateDesc();
}