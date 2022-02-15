package com.reactive.main

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

fun main() {
    Flux.fromIterable(listOf("A","B","C","D"))
        .publishOn(Schedulers.boundedElastic())
        .log()
        .map {
            println("Character:$it")
            "$it converted"
        }
        .log()
        .blockLast()

    Mono.just("Hello Hongseok")
        .publishOn(Schedulers.boundedElastic())
        .log()
        .block()
}
