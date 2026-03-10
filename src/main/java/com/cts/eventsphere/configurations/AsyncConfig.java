package com.cts.eventsphere.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * thread pool bean to manage thread spawning for notification service background run.
 * * @author 2479623
 *
 * @version 1.0
 * @since 10-03-2026
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);   // Minimum threads always running
        executor.setMaxPoolSize(10);  // Maximum threads allowed
        executor.setQueueCapacity(100); // Queue for tasks when threads are busy
        executor.setThreadNamePrefix("EmailThread-");
        executor.initialize();
        return executor;
    }
}