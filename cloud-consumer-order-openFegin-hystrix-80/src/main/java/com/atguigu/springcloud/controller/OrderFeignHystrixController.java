package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @author wck
 * @date 2020/4/8
 **/
@RestController
/**
 * 全局默认HystrixFallback降级方法配置
 * 不需要为每个方法都配置Fallback方法,如果没配置则使用默认全局,有配置则使用配置的
 */
//@DefaultProperties(defaultFallback = "globalHandler")
public class OrderFeignHystrixController {
    @Resource
    private PaymentService paymentService;

    @GetMapping("/consumer/success")
//    @HystrixCommand // 使用默认全局降级方法
    public CommonResult success(){
//        int i = 10/0;
        return paymentService.success();
    }

    @GetMapping("/consumer/timeout")
//    @HystrixCommand(fallbackMethod = "timeoutHandler",commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
//    })
    public CommonResult timeout(){
//        int i = 10/0;
        return paymentService.timeout();
    }

    // 服务降级
    private CommonResult timeoutHandler(){
        String result  = String.format("线程池：%s 查询超时,启动备用处理! -- 服务调用方",Thread.currentThread().getName());
        return new CommonResult(200,result);
    }
    // 全局服务降级
    private CommonResult globalHandler(){
        String result  = String.format("线程池：%s 查询超时,启动全局备用处理! -- 服务调用方",Thread.currentThread().getName());
        return new CommonResult(200,result);
    }

}
