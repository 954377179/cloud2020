package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wck
 * @date 2020/4/6
 **/
@SuppressWarnings("all")
@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/success")
    public CommonResult success(){
        return paymentService.success();
    }

    @GetMapping("/timeout")
    public CommonResult timeout(){
        return paymentService.timeout();
    }

    @GetMapping("/circuitBreaker/{id}")
    public CommonResult circuitBreaker(@PathVariable("id") Integer id){
        return paymentService.circuitBreaker(id);
    }
}
