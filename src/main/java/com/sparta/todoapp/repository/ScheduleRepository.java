package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Long findByUser(User user);

    List<Schedule> findAllByOrderByCreatedAtDesc();
}
