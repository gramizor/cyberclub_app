package com.example.course.models;

import com.example.course.enities.Employee;
import com.example.course.enities.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FreeRegModel {
    Employee employee;
    List<Schedule> scheduleList = new ArrayList<>();
}
