package com.reactive.play

import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

fun main() {
    Mono.fromCallable { listOf("오 놀라워라", "그댈 향한 내 마음", "오 새로워라", "처음 보는 내 모습", "매일 이렇다면", "모진 이 세상도", "참 살아갈만 할 거예요")}
        .subscribeOn(Schedulers.boundedElastic())
        .log()
        .flatMapIterable { it }
        .log()
        .collectList()
        .block()

    println("--------------")

    Mono.fromCallable { listOf("울지마 울지마", "어린 아이같이", "내가 꾹 참고", "맘을 다 잡고", "네게 웃어줄게") }
        .flatMapIterable {
            println("|||${it}")
            it
        }
        .log()
        .subscribeOn(Schedulers.boundedElastic())
        .log()
        .collectList()
        .block()

}