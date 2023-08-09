package com.example.trello.repository;

import com.example.trello.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByExpiredFalseAndEndDateBefore(LocalDate currentDate);
}
