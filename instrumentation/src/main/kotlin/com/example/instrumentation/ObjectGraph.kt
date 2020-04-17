package com.example.instrumentation

import com.example.instrumentation.agent.InstrumentationAgent
import java.io.Writer
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.lang.reflect.Modifier

fun Writer.appendObjectGraph(any: Any, title: String) = apply {
    appendln("digraph {")
    appendln("  label = \"$title\";")
    appendln("  labelloc = \"t\";")
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

private fun Writer.appendObjectNode(any: Any) {
    val size = InstrumentationAgent.getSize(any)
    appendln("  \"${any.nodeName}\" [ label = \"${any.nodeName}\\nsize=$size\"];")
}

private fun Writer.appendReferenceEdge(from: Any, to: Any, label: String, visited: MutableSet<Int>) {
    appendln("  \"${from.nodeName}\" -> \"${to.nodeName}\" [ label=\"$label\" ];")
    appendObjectGraph(to, visited)
}

private val Any.nodeName: String
    get() = "${javaClass.name}@$identityHashCode"

private val Any.identityHashCode: Int
    get() = System.identityHashCode(this)

private val Class<*>.classHierarchy: Sequence<Class<*>>
    get() = generateSequence(this) { it.superclass }

private val Field.isStatic
    get() = (modifiers and Modifier.STATIC) != 0
