package com.reactive.reactive_streams_practice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@SpringBootApplication
@RestController
@Slf4j
@EnableAsync
public class ReactiveStreamsPracticeApplication {
    static final String URL1 = "http://localhost:8081/service?req={req}";
    static final String URL2 = "http://localhost:8081/service2?req={req}";

    @Autowired
    MyService myService;

    // RestTemplate과 다른 점: 기본적으로 async로, mono/flux를 리턴한다
    WebClient client = WebClient.create();

    @Service
    public static class MyService {
        @Async
        public CompletableFuture<String> work(String req) {
            return CompletableFuture.completedFuture(req + "/asyncwork");
        }
    }

    @GetMapping("/rest")
    // Mono를 리턴하면서 Spring Boot가 알아서 subscribe한다.
    public Mono<String> rest(int idx) {
        // 정의하는 것만으로는 api호출이 되지 않는다
        // Mono는 Publisher이다. publisher는 누군가 sbscribe를 하지 않으면 데이터를 쏘지 않는다.
        // exchange를 통해 요청을 보내고 응답을 받음
        return client.get().uri(URL1, idx).exchangeToMono(r -> r.bodyToMono(String.class)) // netty worker 스레드에서 작동?
            .doOnNext(log::info)
            .flatMap(res1 ->
                client.get().uri(URL2, res1).exchangeToMono(r -> r.bodyToMono(String.class))
            )
            .doOnNext(log::info)
            .flatMap(res -> Mono.fromCompletionStage(myService.work(res)));
    }

    public static void main(String[] args) {
        System.setProperty("reactor.ipc.netty.workerCount", "2");
        System.setProperty("reactor.ipc.netty.pool.maxConnections", "2000");
        SpringApplication.run(ReactiveStreamsPracticeApplication.class, args);
    }

}

