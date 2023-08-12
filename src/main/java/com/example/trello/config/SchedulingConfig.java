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

    // 스케줄러로 0초 1분 *시 *일 *달 *년마다 자동 실행됨
    @Scheduled(cron = "0 0/1 * * * *")
    public void processExpiredTasks() {
        //processExpiredTasks로 주입
        taskService.processExpiredTasks();
    }
}
