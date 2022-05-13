package com.coroutines.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

fun main() {
    runBlocking {
        println("runblocking: $coroutineContext")
        val result = testThreeSuspend()
        println("result: $result")
    }
}

suspend fun hey(): String {
    println("HEY")
    return Mono.just(
        "WORLD"
    ).awaitSingle()
}

/**
    테스트해볼 것 아래의 함수와 같은 경우
    suspend fun A(): X {
        return Mono.create {...}
            .awaitSingle()
    }

    1. Mono.subscribeOn()을 메인 쓰레드에서
    2. Mono.subscribeOn()을 새로운 쓰레드에서
    3. .onNext()가 여러번 반복 될 때 subscribeOn()을 여러번 하면
    4. .awaitSingle vs .block
 */

suspend fun testOneSuspend(): String {
    println("Test One Suspend Start")
    return Mono.defer {
        val a = Mono.just("JUST MONO")
        println("Test One MonoDefer Passed")
        a
    }
        .log()
        .awaitSingle()
}

suspend fun testTwoSuspend(): String {
    println("Test Two Suspend Start")
    return Mono.defer {
        val a = Mono.just("JUST MANO")
        println("Test Two MonoDefer Passed")
        a
    }
        .subscribeOn(Schedulers.boundedElastic())
        .awaitSingle()
}

suspend fun testThreeSuspend(): String {
    return Mono.defer {
        val a = Mono.just("JUST MANA")
        println("Test Three MonoDefer Passed")
        a
    }
        .subscribeOn(Schedulers.newSingle("Three First"))
        .doOnNext {
            println("DO ON NEXT!")
            it
        }
        .subscribeOn(Schedulers.newSingle("Three Second"))
        .awaitSingle()
}

suspend fun testFourSuspend(): String {
    return Mono.defer {
        val a = Mono.just("JUST HANA")
        println("Test Four MonoDefer Passed")
        a
    }.block()
}