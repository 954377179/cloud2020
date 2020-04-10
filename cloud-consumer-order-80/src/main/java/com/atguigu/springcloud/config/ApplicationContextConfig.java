package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author wck
 * @date 2020/4/6
 **/
@Configuration
public class ApplicationContextConfig {
    @Bean
    @LoadBalanced // 负载均衡
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
