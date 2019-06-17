package com.xie.test.b;

import com.xie.message.client.annotation.EnableScanTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication()
@EnableAsync
@EnableScanTopic
public class ServerBApp {

    // 入口
    public static void main(String[] args) {
        SpringApplication.run(ServerBApp.class, args);
    }


}
