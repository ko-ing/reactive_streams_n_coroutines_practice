package com.reactive.play

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

fun main() {
    Mono.fromCallable{
        listOf("Hello", "Please", "No", "More", "Exceptions")
    }
//        .log()
        .subscribeOn(Schedulers.boundedElastic())
//        .log()
        .flatMapIterable { it }
//        .log()
        .doOnNext { a ->
            println("[doOnNext]: $a")
        }
        .log()
        .collectList()
//        .log()
        .block()!!

}