package com.coroutines.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException
import java.util.concurrent.Executors
import kotlin.coroutines.coroutineContext

private val threadPoolSize = 5
private val threadPool = Executors.newFixedThreadPool(threadPoolSize)

fun hey(int: Int): Int {
    val random = Math.random().times(1000).toLong()
    delay(random)
    println(Thread.currentThread())
    println("${coroutineContext[Job]} [$int]: {$random} HEYEYE")
    return random.toInt()
}

fun main() {
//    test1()
    test2()
//    test3()
}

fun test1() {
    runBlocking {
        flow {
            (1..16)
//                .chunked(5)
                .forEach {
                    emit(it)
                }
        }
//            .onEach {
//                async {
//                    println("${Thread.currentThread().name}")
//                }
//
//            }
            .onEach {
                async {
                    hey(it)
                }
            }
            .flowOn(Dispatchers.Unconfined)
            .collect()
//            .collect {
////                async { println("[${Thread.currentThread().name}] $it.toString()") }
//            }
    }
}

fun test2() {
    runBlocking {
        try {
            (1..29)
//            .chunked(5)
                .forEach {
                    launch(Dispatchers.) {
//                        try {

                            hey(it)
//                        } catch (e: Exception) {
//                            println(e)
//                        }
                    }
//                    launch {
//                        if (it == 1) throw IllegalArgumentException("hey this is wrong")
//                        hey(it)
//                    }
                }
        } catch (e: Exception) {
            println(e)
        }
    }
}

fun test3() {
    runBlocking {
        try {
            (1..31)
                .map {
                    async {
                        if (it == 1) throw IllegalArgumentException("hey this is wrong")
                        hey(it)
                    }
                }
                .toList()
                .awaitAll()
//            println("I'm tmp ${tmp}")
        } catch (e: Exception) {
            println(e)
        }
    }
}