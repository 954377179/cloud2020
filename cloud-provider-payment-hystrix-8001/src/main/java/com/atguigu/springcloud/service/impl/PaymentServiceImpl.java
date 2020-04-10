package com.atguigu.springcloud.service.impl;

import cn.hutool.core.util.IdUtil;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author wck
 * @date 2020/4/6
 **/
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Override
    public CommonResult success() {
        String result = String.format("线程池：%s 查询成功!", Thread.currentThread().getName());
        return new CommonResult(200, result);
    }
    // ------------ 服务降级 ---------------------------------------------------------

    /**
     * 接口处理超时时间设置为3秒钟,超过3秒钟则使用fallback函数
     * 注意：
     * 超时后抛出了异常：java.lang.InterruptedException: sleep interrupted,并且处理的线程名称为 HystrixTimer-1
     * 程序错误时,处理线程名称为 hystrix-PaymentServiceImpl-2
     */
    @Override
    @HystrixCommand(fallbackMethod = "timeoutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public CommonResult timeout() {
        // 1. 模拟请求处理超时
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        // 2. 程序错误
//        int i = 10/0;
        String result = String.format("线程池：%s 查询超时!", Thread.currentThread().getName());
        return new CommonResult(200, result);
    }

    /**
     * 服务降级函数
     */
    private CommonResult timeoutHandler() {
        String result = String.format("线程池：%s 查询超时,启动备用处理! -- 服务提供方", Thread.currentThread().getName());
        return new CommonResult(200, result);
    }

    // ------------ 服务熔断 ---------------------------------------------------------

    /**
     * 流程：服务降级 --> 进而服务熔断 --> 恢复链路
     * 测试:
     * 多次id为负的请求,会导致断路器开启,断路器开启后,当前服务熔断,所有请求都会走服务降级
     * 等时间窗口期达到之后,断路器会处于半开阶段,接下来若服务请求成功,则关闭断路器
     */
    @Override
    @HystrixCommand(fallbackMethod = "circuitBreakerHandler", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), // 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), // 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"), // 时间窗口期,时间到达之后,断路器半开
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60") // 错误百分比达到多少后断路
    })
    public CommonResult circuitBreaker(Integer id) {
        if (id < 0) {
            throw new RuntimeException("id不能为负数!");
        }
        String serialNumber = IdUtil.simpleUUID();
        String result = String.format("线程：%s 查询成功,序列号：%s", Thread.currentThread().getName(), serialNumber);
        return new CommonResult(200, result);
    }
    /**
     * 服务降级函数,切记参数要一致
     */
    private CommonResult circuitBreakerHandler(Integer id) {
        String result = String.format("线程：%s 查询异常,启动备用处理", Thread.currentThread().getName());
        return new CommonResult(200, result);
    }
}
