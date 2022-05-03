package com.reactive.play

import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import java.lang.IllegalArgumentException

class FluxExceptionTest {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun main() {
        Flux.fromIterable(listOf("A", "B", "C", "D", "E"))
            .doOnNext { string ->
                if (string == "C") throw IllegalArgumentException("[IllegalArgument] Try Other Ways")
                println("HELLO $string")
            }
            .onErrorContinue { e, _ ->
                logger.error(e.message, e)
            }
            .doOnNext { string ->
                if (string == "D") throw IllegalArgumentException("[Second IllegalArgument] Are You Sure?")
                println("WORLD $string")
            }
            .onErrorContinue { e, _ ->
                logger.error(e.message, e)
            }
            .collectList()
            .block()
    }
}
