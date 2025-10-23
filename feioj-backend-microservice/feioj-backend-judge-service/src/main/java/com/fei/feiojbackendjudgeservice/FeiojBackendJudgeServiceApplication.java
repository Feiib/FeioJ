package com.fei.feiojbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.fei")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.fei.feiojbackendserviceclient.service"})
public class FeiojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeiojBackendJudgeServiceApplication.class, args);
    }

}
