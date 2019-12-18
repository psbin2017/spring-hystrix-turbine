package com.hystrix.turbine.web.controller;

import com.hystrix.turbine.web.service.ConsumerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ConsumerController
 */
@RestController
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @GetMapping("/helloConsumer")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body(consumerService.hello());
    }

}