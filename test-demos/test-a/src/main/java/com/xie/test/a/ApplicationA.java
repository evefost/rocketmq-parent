package com.xie.test.a;

import com.xie.message.client.annotation.EnableScanTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication()
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableAsync
@EnableScanTopic
//@EnableScheduling
public class ApplicationA {
    // 入口
    public static void main(String[] args) {

        SpringApplication.run(ApplicationA.class, args);
    }

//    @Bean("taskExecutor")
//    public Executor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setThreadNamePrefix("Anno-Executor");
//        executor.setMaxPoolSize(10);
//
//        // 设置拒绝策略
//        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
//            @Override
//            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//                // .....
//            }
//        });
//        // 使用预定义的异常处理类
//        // executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//
//        return executor;
//    }


}
