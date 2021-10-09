package com.reactive.main;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class SchedulerFlux {
    public static void main(String[] args) {
        Scheduler pubScheduler = Schedulers.newSingle("Pub");
        Scheduler subScheduler = Schedulers.newSingle("Sub");

        Flux.range(1, 10)
            .publishOn(pubScheduler)
            .log()
            .subscribeOn(subScheduler)
            .log()
            .doOnComplete(() -> {
                pubScheduler.dispose();
                subScheduler.dispose();
            })
            .subscribe(System.out::println);

        log.debug("exit");
    }
}
