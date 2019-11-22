package com.github.martindevi.instrumentation

fun main() {
    val map = mapOf(
        "50e625e9-7297-4c55-a16c-57e1df843cf8" to "Ym0HI6C7TA04kHWnJwMoOnuqMG73kR9c",
        "a6ccf524-7f43-4923-a1c8-a19f6bdb1da8" to "W2b1KodO3PaO3l1Y8giQbVRrTEoROX9t",
        "2fe5dbcf-57a4-4981-840d-7dfcaad5887b" to "2CqUaLZXI7z5QncNJJM5aupH3XRhk6qQ",
        "c6aeb5e7-ea91-4651-84ad-2040c64dc3e9" to "XR79MyhbLYdIxMUi1YSgpJznjSEu6nkS"
    )
    println("==> Evaluating map size")
    val objectSize = map.getObjectSize("map")
    println("<== Evaluating map size")
    println("Map size: $objectSize")
}
