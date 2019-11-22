package com.github.martindevi.instrumentation

import androidx.collection.SimpleArrayMap
import java.util.*
import kotlin.collections.HashMap

fun main() {
    val map = mutableMapOf<String, String>().also {
        fill { key, value -> it[key] = value }
    }
    val mapSize = evaluateSize("map", map)

    println()

    val hashMap = HashMap<String, String>().also {
        fill { key, value -> it[key] = value }
    }
    val hashMapSize = evaluateSize("hash map", hashMap)

    println()

    val treeMap = TreeMap<String, String>().also {
        fill { key, value -> it[key] = value }
    }
    val treeMapSize = evaluateSize("tree map", treeMap)

    println()

    val simpleArrayMap = SimpleArrayMap<String, String>().apply {
        fill { key, value -> put(key, value)}
    }
    val simpleArrayMapSize = evaluateSize("simple array map", simpleArrayMap)

    println()

    println("Map size: $mapSize")
    println("Hash Map size: $hashMapSize")
    println("Tree Map size: $treeMapSize")
    println("Simple Array Map size: $simpleArrayMapSize")
}

private inline fun fill(block: (String, String) -> Unit) {
    val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVZXYZ0123456789"
    val random = Random(37848389588346L)
    repeat(256) {
        val key = String(CharArray(32) { alphabet[random.nextInt(alphabet.length)] })
        val value = String(CharArray(32) { alphabet[random.nextInt(alphabet.length)] })
        block(key, value)
    }
}

private fun evaluateSize(name: String, instance: Any): Long {
    println("==> Evaluating $name size")
    val size = instance.getObjectSize(name)
    println("<== Evaluating $name size")
    return size
}
