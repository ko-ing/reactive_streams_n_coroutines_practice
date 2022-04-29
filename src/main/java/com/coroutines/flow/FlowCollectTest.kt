package com.coroutines.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

private val threadPoolSize = 5
private val threadPool = Executors.newFixedThreadPool(threadPoolSize)

suspend fun hey(int: Int) {
    val random = Math.random().times(1000).toLong()
    delay(random)
    println("${Thread.currentThread().name} [$int]: {$random} HEYEYE")
}

fun main() {
    runBlocking {
        flow {
            (1..16)
//                .chunked(5)
                .forEach {
                    emit(it)
                }
        }
            .onEach {
                hey(it)
            }
            .flowOn(threadPool.asCoroutineDispatcher())
            .collect()
//            .collect {
////                async { println("[${Thread.currentThread().name}] $it.toString()") }
//            }
    }
}