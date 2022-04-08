package com.reactive.play

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

fun main() {
    val mono = Mono.just("1111마음 울적한 날에").log()
    val mono2 = Mono.just("2222말리지 마").log()

    val list = listOf("거리를 걸어보고", "향기로운 칵테일에 취해도 보고", "한 편의 시가 있는 전시회장도 가고")

    val flux = Mono.zip(mono, mono2)
        .map { tuple ->
            println("HEY")
            val a = list.map {
                it + tuple.t1 + tuple.t2
            }
            a
        }
        .flatMapMany { Flux.fromIterable(it) }.log()

    flux.flatMap { fluxString ->
        Mono.zip(Mono.just(fluxString), mono, mono2)
    }.map { t ->
        println("YA")
        t.t1 + t.t2 + t.t3
    }
//        .log()
        .subscribe()
}

