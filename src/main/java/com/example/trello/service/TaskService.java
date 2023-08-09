package com.example.trello.service;

import com.example.trello.entity.Task;
import com.example.trello.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public void processExpiredTasks() {
        LocalDate currentDate = LocalDate.now();
        List<Task> expiredTasks = taskRepository.findByExpiredFalseAndEndDateBefore(currentDate);

        for (Task task : expiredTasks) {
            task.setExpired(true);
            taskRepository.save(task);
        }
    }
}
