package com.hystrix.turbine.web.service;

import java.util.concurrent.ThreadLocalRandom;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * ConsumerService
 */
@Slf4j
@Service
public class ConsumerService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(
        // 실패시 간주하는 통계 명, default: 메소드 명
        commandKey = "hello"
        // 실패시 실행하는 메소드 명, 리턴 타입이 동일해야 한다.
        , fallbackMethod = "defaultHello"
        // 
        , commandProperties = {
            // 실패로 간주하는 타임아웃 시간을 어떻게 설정할 것인지(ms), default: 1000
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "150")
            // 정해진 시간에 N % 의 요청이 실패라면 서킷을 열지, default: 50
            , @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
            // N 번 이상 호출되었을 때, 서킷을 열기 위한 카운트를 계산 할 것인지, default: 20
            , @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5")
            // 열린 서킷을 얼마나 유지할 것인지(ms), default: 5000
            , @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
        }
    )
    public String hello() {
        try {
            int randomNumber = ThreadLocalRandom.current().nextInt(100, 200 + 1);
            Thread.sleep(randomNumber);
        } catch (Exception e) { }
        return restTemplate.getForObject("http://localhost:8100/helloSuplier", String.class);
    }

    @SuppressWarnings("unused")
    private String defaultHello(final Throwable throwable) {
        log.info(throwable.getMessage());
        return "default hello";
    }
}