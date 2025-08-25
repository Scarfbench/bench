package com.ibm.websphere.samples.daytrader.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "ManagedExecutorService")
    public Executor managedExecutorService() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor =
            new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("mes-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Bean(name = "ManagedScheduledTaskExecutor")
    public ScheduledExecutorService taskScheduler() {
        ScheduledExecutorService threadPoolTaskExecutor =
            new ScheduledExecutorService();
        threadPoolTaskExecutor.setThreadNamePrefix("mstex-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
