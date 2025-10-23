package com.fei.feiojbackendserviceclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.fei.feiojbackendserviceclient.service"})
public class FeiojBackendServiceClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeiojBackendServiceClientApplication.class, args);
    }

}
