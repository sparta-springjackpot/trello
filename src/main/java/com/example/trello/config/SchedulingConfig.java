package com.example.trello.config;

import com.example.trello.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Autowired
    private TaskService taskService;

    @Scheduled(cron = "0 0 * * * *")
    public void processExpiredTasks() {
        taskService.processExpiredTasks();
    }
}
