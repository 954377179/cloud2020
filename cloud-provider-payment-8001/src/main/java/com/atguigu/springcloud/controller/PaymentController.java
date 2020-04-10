package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    /**
     * 服务发现
     */
    @Resource
    private DiscoveryClient discoveryClient;

    @Value("${server.port}")
    private String ServerPort;

    @PostMapping("/create")
    public CommonResult create(@RequestBody Payment payment){
        int result =  paymentService.create(payment);
        if (result > 0){
            return new CommonResult(200,"新增成功",payment);
        }else {
            return new CommonResult(500,"新增失败",null);
        }
    }

    @GetMapping("/get/{id}")
    public CommonResult getPaymentById(@PathVariable Long id){
        Payment payment = paymentService.getPaymentById(id);
        if (payment != null){
            return new CommonResult(200,"查询成功",payment);
        }else {
            return new CommonResult(500,"查询失败",null);
        }
    }

    @GetMapping(value = "/discovery")
    public CommonResult discovery(){
        /**
         * 获取Eureka Server上的服务清单
         */
        List<String> services = discoveryClient.getServices();
        return new CommonResult(200,"查询成功",services);
    }

    @GetMapping(value = "/serverPort")
    public CommonResult getServerPort(){
        return new CommonResult(200,"查询成功",ServerPort);
    }

    /**
     * OpenFeign超时控制测试
     * 默认等待1秒钟
     */
    @GetMapping(value = "/timeout")
    public CommonResult timeout(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new CommonResult(200,"查询成功",null);
    }
}
