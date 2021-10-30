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

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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

    // MediaType.TEXT_EVENT_STREAM_VALUE을 통해 스트림으로 데이터를 받을 수 있게 한다.
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> events() {
        Event event1 = new Event(1L, "event1");
        Event event2 = new Event(2L, "event2");
        List<Event> list = Arrays.asList(event1, event2);
        return Flux.fromIterable(list);
    }

    @GetMapping(value = "/streams", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> eventsStream() {
        Stream stream = Stream.generate(() -> new Event(System.currentTimeMillis(),"value")).limit(10);
        return Flux.fromStream(stream)
            .delayElements(Duration.ofSeconds(1));
        // 백그라운드 스레드에서 딜레이 처리 (해당 스레드에서 blocking)
    }

    @GetMapping(value = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> flux() {
        Flux<Event> es = Flux
            .<Event, Long>generate(() -> 1L, (id, sink) -> {
                sink.next(new Event(id, "value"+ id));
                return id+1;
            })
            .delayElements(Duration.ofSeconds(1))
            .take(10);
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        // Tuple로 만들어준다
        // interval 데이터 딜레이가 있어서 기다리면서 tuple 생성도 딜레이가 생김
        return Flux.zip(es, interval).map(t -> t.getT1());
    }

    public static void main(String[] args) {
        SpringApplication.run(FluxApplication.class);
    }
}
