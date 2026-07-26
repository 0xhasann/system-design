package com.redis.rate.limiter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/login")
    public String login() {

        log.info("login Controller test ::");

        return "hello";
    }

    @GetMapping("/payment")
    public String payment() {

        log.info("payment Controller test ::");

        return "hello";
    }

}
