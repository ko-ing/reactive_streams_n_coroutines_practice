package com.reactive.play

fun main() {
//    val a = FluxExceptionTest()
//    a.main()
    val x = List(12) { 0.0 }.toTypedArray()
    listOf(1.0, *x).reduce { a, b ->
        val result = (a + b) * 1.03
        println(result)
        result
    }
}