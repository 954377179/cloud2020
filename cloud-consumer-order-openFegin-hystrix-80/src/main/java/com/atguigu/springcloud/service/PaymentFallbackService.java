package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.CommonResult;
import org.springframework.stereotype.Component;

/**
 * @author wck
 * @date 2020/4/8
 * 实现OpenFeign接口,并且在@FeignClient中指定特定的Class路径,即可实现服务降级
 * 从而将服务降级业务跟实际业务剥离,降低代码的耦合度
 **/
@Component
public class PaymentFallbackService implements PaymentService {

    /**
     * 服务降级处理函数
     */
    @Override
    public CommonResult success() {
        String result  = String.format("线程池：%s 查询异常 -- PaymentFallbackService 统一降级处理",Thread.currentThread().getName());
        return new CommonResult(200,result);
    }

    @Override
    public CommonResult timeout() {
        String result  = String.format("线程池：%s 查询异常 -- PaymentFallbackService 统一降级处理",Thread.currentThread().getName());
        return new CommonResult(200,result);
    }
}
