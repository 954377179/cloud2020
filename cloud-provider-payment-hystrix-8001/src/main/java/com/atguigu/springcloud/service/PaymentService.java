package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.CommonResult;

/**
 * @author wck
 * @date 2020/4/6
 **/
public interface PaymentService {

    CommonResult success();
    CommonResult timeout();
    CommonResult circuitBreaker(Integer id);
}
