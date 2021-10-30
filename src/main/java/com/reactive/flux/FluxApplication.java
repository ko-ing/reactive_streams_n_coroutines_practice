package com.reactive.flux;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Slf4j
@RestController
public class FluxApplication {
    @Data
    @AllArgsConstructor
    public static class Event {
        private long id;
        private String value;
    }

    @GetMapping("/event/{id}")
    Mono<List<Event>> event(@PathVariable long id) {
        Event event1 = new Event(1L, "event1");
        Event event2 = new Event(2L, "event2");
        List<Event> list = Arrays.asList(event1, event2);
        return Mono.just(list);
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> events() {
        Event event1 = new Event(1L, "event1");
        Event event2 = new Event(2L, "event2");
        List<Event> list = Arrays.asList(event1, event2);
        return Flux.fromIterable(list);
    }

    public static void main(String[] args) {
        SpringApplication.run(FluxApplication.class);
    }
}
