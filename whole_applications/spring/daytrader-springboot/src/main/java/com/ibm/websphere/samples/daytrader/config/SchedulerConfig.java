package com.ibm.websphere.samples.daytrader.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableAsync
public class SchedulerConfig {

    @Bean(name = "ManagedScheduledTaskExecutor")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskExecutor =
            new ThreadPoolTaskScheduler();
        threadPoolTaskExecutor.setThreadNamePrefix("msteex-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
