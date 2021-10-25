package com.reactive.mono;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
@Slf4j
public class MonoApplication {
    @GetMapping("/")
    Mono<String> hello() {
        // Spring이 알아서 subscribe를 호출해준다.
        log.info("Before");
        Mono mono = Mono.just(getHey()).log();
        log.info("After");
        return mono;
    }

    private String getHey() {
        log.info("method getHey()");
        return "Hey Hey Hey";
    }

    public static void main(String[] args) {
        SpringApplication.run(MonoApplication.class, args);
    }
}
