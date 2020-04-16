package com.example.instrumentation

import androidx.collection.ArrayMap
import androidx.collection.SimpleArrayMap
import com.example.instrumentation.agent.InstrumentationAgent
import java.util.*
import kotlin.collections.HashMap

fun main() {
    printObjectSizes()
    println()
    printStringSize()
    println()
    printCollectionSizes()
}

@Suppress("unused")
fun printObjectSizes() {
    printObjectSize(ByteArray(0), "ByteArray(0)")
    printObjectSize(ByteArray(1), "ByteArray(1)")
    printObjectSize(ByteArray(2), "ByteArray(2)")
    printObjectSize(ByteArray(8), "ByteArray(8)")
    printObjectSize(ByteArray(9), "ByteArray(9)")
    printObjectSize(IntArray(0), "IntArray(0)")
    printObjectSize(IntArray(1), "IntArray(1)")
    printObjectSize(IntArray(2), "IntArray(2)")
    printObjectSize(IntArray(3), "IntArray(3)")
    printObjectSize(IntArray(4), "IntArray(4)")
    printObjectSize(IntArray(5), "IntArray(5)")
    printObjectSize(object {}, "object {}")
    printObjectSize(
        object {
            val byte: Byte = 0
        },
        "object { val byte: Byte = 0 }"
    )
    printObjectSize(
        object {
            val byte1: Byte = 0
            val byte3: Byte = 0
        },
        "object { val byte1: Byte = 0; val byte2: Byte = 0 }"
    )
    printObjectSize(
        object {
            val byte1: Byte = 0
            val byte2: Byte = 0
            val byte3: Byte = 0
            val byte4: Byte = 0
        },
        "object { val byte1: Byte = 0; /* ... */ val byte4: Byte = 0 }"
    )
    printObjectSize(
        object {
            val byte1: Byte = 0
            val byte2: Byte = 0
            val byte3: Byte = 0
            val byte4: Byte = 0
            val byte5: Byte = 0
        },
        "object { val byte1: Byte = 0; /* ... */ val byte5: Byte = 0 }"
    )
    printObjectSize(
        object {
            val byte1: Byte = 0
            val byte2: Byte = 0
            val byte3: Byte = 0
            val byte4: Byte = 0
            val byte5: Byte = 0
            val byte6: Byte = 0
            val byte7: Byte = 0
            val byte8: Byte = 0
            val byte9: Byte = 0
            val byte10: Byte = 0
            val byte11: Byte = 0
            val byte12: Byte = 0
        },
        "object { val byte1: Byte = 0; /* ... */ val byte12: Byte = 0 }"
    )
    printObjectSize(
        object {
            val byte1: Byte = 0
            val byte2: Byte = 0
            val byte3: Byte = 0
            val byte4: Byte = 0
            val byte5: Byte = 0
            val byte6: Byte = 0
            val byte7: Byte = 0
            val byte8: Byte = 0
            val byte9: Byte = 0
            val byte10: Byte = 0
            val byte11: Byte = 0
            val byte12: Byte = 0
            val byte13: Byte = 0
        },
        "object { val byte1: Byte = 0; /* ... */ val byte13: Byte = 0 }"
    )
    printObjectSize(
        object {
            val int: Int = 0
        },
        "object { val int: Int = 0 }"
    )
    printObjectSize(
        object {
            val int1: Int = 0
            val int2: Int = 0
        },
        "object { val int1: Int = 0; val int2: Int = 0 }"
    )
    printObjectSize(
        object {
            val int1: Int = 0
            val int2: Int = 0
            val int3: Int = 0
        },
        "object { val int1: Int = 0; /* ... */ val int3: Int = 0 }"
    )
    printObjectSize(
        object {
            val int1: Int = 0
            val int2: Int = 0
            val int3: Int = 0
            val int4: Int = 0
        },
        "object { val int1: Int = 0; /* ... */ val int4: Int = 0 }"
    )
}

fun printStringSize() {
    val s = "Hello, World!"
    println(s.javaClass.name + "@" + Integer.toHexString(System.identityHashCode(s)))
    val array = s.javaClass.declaredFields.first { it.name == "value" }.apply { isAccessible = true }.get(s) as CharArray
    println(array.javaClass.name + "@" + Integer.toHexString(System.identityHashCode(array)))
    val size = evaluateSize(s, s)
    println(size)
}

fun printObjectSize(any: Any, description: String) {
    println(description)
    println(InstrumentationAgent.getSize(any))
}

private fun printCollectionSizes() {
    val map = mutableMapOf<String, String>().also {
        fill { key, value -> it[key] = value }
    }
    val mapSize = evaluateSize("map", map)

    println()

    val mapLazyInitialized = mutableMapOf<String, String>().also {
        fill { key, value -> it[key] = value }
        it.keys
        it.values
        it.entries
    }
    val mapLazyInitializedSize =
        evaluateSize("map lazy initialized", mapLazyInitialized)

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
        fill { key, value -> put(key, value) }
    }
    val simpleArrayMapSize =
        evaluateSize("simple array map", simpleArrayMap)

    println()

    val arrayMap = ArrayMap<String, String>().apply {
        fill { key, value -> put(key, value) }
    }
    val arrayMapSize = evaluateSize("array map", arrayMap)

    println()

    println("Map size: $mapSize")
    println("Map lazy initialized size: $mapLazyInitializedSize")
    println("Hash Map size: $hashMapSize")
    println("Tree Map size: $treeMapSize")
    println("Simple Array Map size: $simpleArrayMapSize")
    println("Array Map size: $arrayMapSize")
}

private inline fun fill(block: (String, String) -> Unit) {
    val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVZXYZ0123456789"
    val random = Random(37848389588346L)
    repeat(16) {
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
