package com.example.instrumentation

import com.example.instrumentation.agent.InstrumentationAgent
import java.io.StringWriter
import java.io.Writer
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.lang.reflect.Modifier

fun printObjectGraph(any: Any) {
    val s = StringWriter().also { GraphGenerator(it).appendObject(any, mutableSetOf()) }.toString()
    println(s)
}

private class GraphGenerator(
    private val writer: Writer
) {
    fun appendObject(any: Any, visited: MutableSet<Int>) {
        val identityHashCode = System.identityHashCode(any)
        if (visited.add(identityHashCode)) {
            val name = "${any.javaClass.name}@$identityHashCode"
            val size = InstrumentationAgent.getSize(any)
            writer.appendln("\"$name\" [ label = \"$name\\nsize=$size\"];")
            if (any.javaClass.isArray) {
                if (!any.javaClass.componentType.isPrimitive) {
                    appendArrayReferences(any, name, visited)
                }
            } else {
                appendFieldReferences(any, name, visited)
            }
        }
    }

    private fun appendArrayReferences(
        any: Any,
        name: String,
        visited: MutableSet<Int>
    ) {
        repeat(Array.getLength(any)) { i ->
            Array.get(any, i)?.let {
                appendReference(name, it, "[$i]", visited)
            }
        }
    }

    private fun appendFieldReferences(
        any: Any,
        name: String,
        visited: MutableSet<Int>
    ) {
        any.javaClass.classHierarchy.flatMap { it.declaredFields.asSequence() }
            .onEach { it.isAccessible = true }
            .filterNot { it.isStatic }
            .filterNot { it.type.isPrimitive }
            .mapNotNull { it.get(any)?.to(it) }
            .forEach { (it, field) ->
                appendReference(name, it, field.name, visited)
            }
    }

    private fun appendReference(name: String, any: Any, label: String, visited: MutableSet<Int>) {
        writer.appendln("\"$name\" -> \"${any.javaClass.name}@${System.identityHashCode(any)}\" [ label=\"$label\" ];")
        appendObject(any, visited)
    }
}

private val Class<*>.classHierarchy: Sequence<Class<*>>
    get() = generateSequence(this) { it.superclass }

private val Field.isStatic
    get() = (modifiers and Modifier.STATIC) != 0
