package com.example.kastools.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


@Configuration
@EnableAsync
public class ThreadConf {
    @Bean("pool")
    public Executor task(){
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
executor.setCorePoolSize(5);  //核心线程数
executor.setMaxPoolSize(10);  //最大线程数
executor.setQueueCapacity(20);   //队列
executor.setKeepAliveSeconds(60);   //普通线程存活时间
executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());  // 拒绝策略：由调用线程执行任务
executor.initialize();
return executor;
    }
}
