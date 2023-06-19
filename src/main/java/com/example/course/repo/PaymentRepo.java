package com.example.course.repo;

import com.example.course.entities.PaymentHistory;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepo extends CrudRepository<PaymentHistory, Integer> {
    @Override
    PaymentHistory save(PaymentHistory payment);
}
