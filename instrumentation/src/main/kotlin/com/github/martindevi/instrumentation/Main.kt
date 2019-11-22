package com.github.martindevi.instrumentation

import androidx.collection.SimpleArrayMap
import java.util.*
import kotlin.collections.HashMap

fun main() {
    val map = mapOf(
        "50e625e9-7297-4c55-a16c-57e1df843cf8" to "Ym0HI6C7TA04kHWnJwMoOnuqMG73kR9c",
        "a6ccf524-7f43-4923-a1c8-a19f6bdb1da8" to "W2b1KodO3PaO3l1Y8giQbVRrTEoROX9t",
        "2fe5dbcf-57a4-4981-840d-7dfcaad5887b" to "2CqUaLZXI7z5QncNJJM5aupH3XRhk6qQ",
        "c6aeb5e7-ea91-4651-84ad-2040c64dc3e9" to "XR79MyhbLYdIxMUi1YSgpJznjSEu6nkS"
    )
    evaluateSize("map", map)

    println()

    val hashMap = HashMap<String, String>().also {
        it["50e625e9-7297-4c55-a16c-57e1df843cf8"] = "Ym0HI6C7TA04kHWnJwMoOnuqMG73kR9c"
        it["a6ccf524-7f43-4923-a1c8-a19f6bdb1da8"] = "W2b1KodO3PaO3l1Y8giQbVRrTEoROX9t"
        it["2fe5dbcf-57a4-4981-840d-7dfcaad5887b"] = "2CqUaLZXI7z5QncNJJM5aupH3XRhk6qQ"
        it["c6aeb5e7-ea91-4651-84ad-2040c64dc3e9"] = "XR79MyhbLYdIxMUi1YSgpJznjSEu6nkS"
    }
    evaluateSize("hash map", hashMap)

    println()

    val treeMap = TreeMap<String, String>().also {
        it["50e625e9-7297-4c55-a16c-57e1df843cf8"] = "Ym0HI6C7TA04kHWnJwMoOnuqMG73kR9c"
        it["a6ccf524-7f43-4923-a1c8-a19f6bdb1da8"] = "W2b1KodO3PaO3l1Y8giQbVRrTEoROX9t"
        it["2fe5dbcf-57a4-4981-840d-7dfcaad5887b"] = "2CqUaLZXI7z5QncNJJM5aupH3XRhk6qQ"
        it["c6aeb5e7-ea91-4651-84ad-2040c64dc3e9"] = "XR79MyhbLYdIxMUi1YSgpJznjSEu6nkS"
    }
    evaluateSize("tree map", treeMap)

    println()

    val simpleArrayMap = SimpleArrayMap<String, String>().apply {
        put("50e625e9-7297-4c55-a16c-57e1df843cf8", "Ym0HI6C7TA04kHWnJwMoOnuqMG73kR9c")
        put("a6ccf524-7f43-4923-a1c8-a19f6bdb1da8", "W2b1KodO3PaO3l1Y8giQbVRrTEoROX9t")
        put("2fe5dbcf-57a4-4981-840d-7dfcaad5887b", "2CqUaLZXI7z5QncNJJM5aupH3XRhk6qQ")
        put("c6aeb5e7-ea91-4651-84ad-2040c64dc3e9", "XR79MyhbLYdIxMUi1YSgpJznjSEu6nkS")
    }
    evaluateSize("simple array map", simpleArrayMap)
}

private fun evaluateSize(name: String, instance: Any) {
    println("==> Evaluating $name size")
    val mapSize = instance.getObjectSize(name)
    println("<== Evaluating $name size")
    println("Total $name size: $mapSize")
}
