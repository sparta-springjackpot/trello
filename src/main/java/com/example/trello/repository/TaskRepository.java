package com.example.trello.repository;

import com.example.trello.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByExpiredFalseAndEndDateBefore(LocalDateTime currentDate);
    Optional<Task> findByCardId(Long cardId);
    Optional<Task> findByColumnId(Long columnId);
}
