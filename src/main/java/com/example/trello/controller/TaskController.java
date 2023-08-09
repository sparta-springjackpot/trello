package com.example.trello.controller;

import com.example.trello.entity.Task;
import com.example.trello.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/api/cards/{id}/dates")
    @ResponseBody
    public Task getTask(@PathVariable Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @PostMapping("/card/{id}/update-dates")
    @ResponseBody
    public Task updateCardDates(@PathVariable Long id, @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            task.setStartDate(startDate);
            task.setEndDate(endDate);
            taskRepository.save(task);
        }
        return taskRepository.findById(id).orElse(null);
    }
}
