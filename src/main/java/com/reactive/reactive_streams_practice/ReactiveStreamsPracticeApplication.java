package com.reactive.reactive_streams_practice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
@Slf4j
public class ReactiveStreamsPracticeApplication {
    static final String URL1 = "http://localhost:8081/service?req={req}";
    static final String URL2 = "http://localhost:8081/service2?req={req}";

    @Autowired
    MyService myService;

    // RestTemplate과 다른 점: 기본적으로 async로, mono/flux를 리턴한다
    WebClient client = WebClient.create();

    @Service
    public static class MyService {
        public String work(String req) {
            return req + "/asyncwork";
        }
    }

    @GetMapping("/rest")
    // Mono를 리턴하면서 Spring Boot가 알아서 subscribe한다.
    public Mono<String> rest(int idx) {
        // 정의하는 것만으로는 api호출이 되지 않는다
        // Mono는 Publisher이다. publisher는 누군가 subscribe를 하지 않으면 데이터를 쏘지 않는다.
        Mono<ClientResponse> res = client.get().uri(URL1, idx).exchange();
        return res.flatMap(r -> r.bodyToMono(String.class));
    }

    public static void main(String[] args) {
        System.setProperty("reactor.ipc.netty.workerCount", "2");
        System.setProperty("reactor.ipc.netty.pool.maxConnections", "2000");
        SpringApplication.run(ReactiveStreamsPracticeApplication.class, args);
    }

}

