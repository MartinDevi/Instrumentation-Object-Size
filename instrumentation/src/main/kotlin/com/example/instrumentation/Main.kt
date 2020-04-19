package com.example.instrumentation

import androidx.collection.ArrayMap
import androidx.collection.SimpleArrayMap
import com.example.instrumentation.agent.InstrumentationAgent
import java.io.File
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

fun main(args: Array<String>) {
    printObjectSizes()
    println()

    printStringSize()
    println()

    val dotOutputDir = File(args.single()).apply { mkdirs() }
    printCollectionSizes(dotOutputDir)
    println()

    printLargeMapSize()
    println()

    printObjectVsMap()
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
    printObjectSize(
        object {
            val any: Any? = null
        },
        "object { val any: Any? = null }"
    )
    printObjectSize(
        object {
            val any1: Any? = null
            val any2: Any? = null
        },
        "object { val any1: Any? = null; val any2: Any? = null }"
    )
    printObjectSize(
        object {
            val any1: Any? = null
            val any2: Any? = null
            val any3: Any? = null
        },
        "object { val any1: Any? = null; /* ... */ val any3: Any? = null }"
    )
    printObjectSize(
        object {
            val any1: Any? = null
            val any2: Any? = null
            val any3: Any? = null
            val any4: Any? = null
        },
        "object { val any1: Any? = null; /* ... */ val any4: Any? = null }"
    )
}

fun printStringSize() {
    val s = "Hello, World!"
    println(s)
    println("${s.javaClass.name}@${Integer.toHexString(System.identityHashCode(s))}: ${InstrumentationAgent.getSize(s)}")
    val array =
        s.javaClass.declaredFields.first { it.name == "value" }.apply { isAccessible = true }.get(s) as CharArray
    println("${array.javaClass.name}@${Integer.toHexString(System.identityHashCode(array))}: ${InstrumentationAgent.getSize(array)}")
    val size = s.getObjectGraphSize()
    println(size)
}

fun printObjectSize(any: Any, description: String) {
    println(description)
    println(InstrumentationAgent.getSize(any))
}

@Suppress("unused")
private fun printCollectionSizes(dotOutputDir: File) {
    val linkedHashMap = LinkedHashMap<String, String>().also {
        fill { key, value -> it[key] = value }
    }
    evaluateSize("LinkedHashMap", linkedHashMap, dotOutputDir)

    val any = object {
        val mpevtg: String = "ANKZVT"
        val tomjta: String = "GONITO"
        val fvxexi: String = "PAOBRW"
        val augnqe: String = "KDUNYJ"
    }
    evaluateSize("object", any, dotOutputDir)

    val hashMap = HashMap<String, String>().also {
        fill { key, value -> it[key] = value }
    }
    evaluateSize("HashMap", hashMap, dotOutputDir)

    val treeMap = TreeMap<String, String>().also {
        fill { key, value -> it[key] = value }
    }
    evaluateSize("TreeMap", treeMap, dotOutputDir)

    val arrayMap = ArrayMap<String, String>().also {
        fill { key, value -> it[key] = value }
    }
    evaluateSize("ArrayMap", arrayMap, dotOutputDir)

    val simpleArrayMap = SimpleArrayMap<String, String>().also {
        fill { key, value -> it.put(key, value) }
    }
    evaluateSize("SimpleArrayMap", simpleArrayMap, dotOutputDir)

    val map = mapOf(
        "mpevtg" to "ANKZVT",
        "tomjta" to "GONITO",
        "fvxexi" to "PAOBRW",
        "augnqe" to "KDUNYJ"
    )
    evaluateSize("mapOf", map, dotOutputDir)
}

private inline fun fill(block: (String, String) -> Unit) {
    block("mpevtg", "ANKZVT")
    block("tomjta", "GONITO")
    block("fvxexi", "PAOBRW")
    block("augnqe", "KDUNYJ")
}

private fun evaluateSize(name: String, instance: Any, dotOutputDir: File) {
    val size = instance.getObjectGraphSize()
    File(dotOutputDir, "$name.dot").writer().use {
        it.appendObjectGraph(instance)
    }
    println("$name: $size")
}

private const val SAMPLE_MAP_COUNT = 500
private const val SAMPLE_MAP_SIZE = 30
private const val SAMPLE_MAP_KEY_LENGTH = 10
private const val SAMPLE_MAP_VALUE_LENGTH = 20

fun printLargeMapSize() {
    val map = mutableMapOf<String, String>().apply {
        repeat(SAMPLE_MAP_SIZE) {
            val key = randomString(SAMPLE_MAP_KEY_LENGTH)
            val value = randomString(SAMPLE_MAP_VALUE_LENGTH)
            put(key, value)
        }
    }
    val size = map.getObjectGraphSize()
    println("Map size: $SAMPLE_MAP_SIZE of strings of length 10")
    println("Key size: $SAMPLE_MAP_KEY_LENGTH")
    println("Value size: $SAMPLE_MAP_VALUE_LENGTH")
    println(size)
    println("Map count: $SAMPLE_MAP_COUNT")
    val numberFormat = NumberFormat.getNumberInstance(Locale.US).apply { maximumFractionDigits = 2 }
    println(numberFormat.format((SAMPLE_MAP_COUNT * size).toFloat() / 1_000_000) + "Mb")
}

private val alphabet = ('0'..'9').toList() + ('a'..'z').toList()
private val random = Random(1587132898038L)

private fun randomString(length: Int) =
    String(CharArray(length) { alphabet[random.nextInt(alphabet.size)] })
