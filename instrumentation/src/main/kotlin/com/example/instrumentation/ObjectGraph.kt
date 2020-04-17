package com.example.instrumentation

import com.example.instrumentation.agent.InstrumentationAgent
import java.io.Writer
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.lang.reflect.Modifier

fun Writer.appendObjectGraph(any: Any, title: String) = apply {
    appendln("digraph {")
    appendln(" label=\"$title\";")
    appendln(" labelloc=\"t\";")
    appendln(" bgcolor=transparent;")
    appendln(" fontcolor=white;")
    appendln(" node[fontsize=12,shape=box,fixedsize=true,color=white;fontcolor=white];")
    appendln(" edge[fontsize=12,color=white,fontcolor=white];")
    appendObjectGraph(any, mutableSetOf())
    appendln("}")
}

private fun Writer.appendObjectGraph(any: Any, visited: MutableSet<Int>) {
    val identityHashCode = any.identityHashCode
    if (visited.add(identityHashCode)) {
        appendObjectNode(any)
        if (any.javaClass.isArray) {
            if (!any.javaClass.componentType.isPrimitive) {
                appendArrayReferences(any, visited)
            }
        } else {
            appendFieldReferences(any, visited)
        }
    }
}

private fun Writer.appendArrayReferences(
    any: Any,
    visited: MutableSet<Int>
) {
    repeat(Array.getLength(any)) { i ->
        Array.get(any, i)?.let {
            appendReferenceEdge(any, it, "[$i]", visited)
        }
    }
}

private fun Writer.appendFieldReferences(
    any: Any,
    visited: MutableSet<Int>
) {
    any.javaClass.classHierarchy.flatMap { it.declaredFields.asSequence() }
        .onEach { it.isAccessible = true }
        .filterNot { it.isStatic }
        .filterNot { it.type.isPrimitive }
        .mapNotNull { it.get(any)?.to(it) }
        .forEach { (it, field) ->
            appendReferenceEdge(any, it, field.name, visited)
        }
}

private const val NODE_SIZE_SCALE_FACTOR = 15

private fun Writer.appendObjectNode(any: Any) {
    val size = InstrumentationAgent.getSize(any)
    val nodeSize = size.toFloat() / NODE_SIZE_SCALE_FACTOR
    appendln(" ${any.identityHashCode} [label=\"${any.nodeLabel}\",width=$nodeSize${if (any.javaClass.isArray) ",fontname=\"Times-Italic\"" else ""}];")
}

private val Any.nodeLabel: String?
    get() =
        if (javaClass.isArray) {
            if (javaClass.componentType.isPrimitive) {
                when (javaClass.componentType) {
                    Byte::class.java -> "ByteArray"
                    Short::class.java -> "ShortArray"
                    Int::class.java -> "IntArray"
                    Long::class.java -> "LongArray"
                    Float::class.java -> "FloatArray"
                    Double::class.java -> "DoubleArray"
                    Boolean::class.java -> "BooleanArray"
                    Char::class.java -> "CharArray"
                    else -> error("Unrecognized primitive array component type ${javaClass.componentType}")
                }
            } else {
                "Array<${javaClass.componentType.name}>"
            }
        } else {
            if (javaClass.isAnonymousClass || javaClass.name.matches(Regex(".+\\$\\d+$"))) {
                // anonymous class
                "object"
            } else {
                javaClass.name
            }
        }

private fun Writer.appendReferenceEdge(
    from: Any,
    to: Any,
    label: String,
    visited: MutableSet<Int>
) {
    appendln(" ${from.identityHashCode} -> ${to.identityHashCode} [label=\"$label\"];")
    appendObjectGraph(to, visited)
}

private val Any.identityHashCode: Int
    get() = System.identityHashCode(this)

private val Class<*>.classHierarchy: Sequence<Class<*>>
    get() = generateSequence(this) { it.superclass }

private val Field.isStatic
    get() = (modifiers and Modifier.STATIC) != 0
