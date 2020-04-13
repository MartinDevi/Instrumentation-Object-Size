package com.example.instrumentation

import com.example.instrumentation.agent.InstrumentationAgent
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.lang.reflect.Modifier

fun Any.getObjectGraphSize(): Long =
    getObjectGraphSize(mutableSetOf())

private fun Any.getObjectGraphSize(visited: MutableSet<Int>): Long =
    if (visited.add(System.identityHashCode(this))) {
        InstrumentationAgent.getSize(this) + getReferencesSize(visited)
    } else {
        0L
    }

private fun Any.getReferencesSize(visited: MutableSet<Int>): Long =
    if (javaClass.isArray) {
        getArrayReferencesSize(visited)
    } else {
        getFieldReferencesSize(visited)
    }

private fun Any.getArrayReferencesSize(visited: MutableSet<Int>): Long =
    if (javaClass.componentType.isPrimitive) {
        0L
    } else {
        List(Array.getLength(this)) { Array.get(this, it) }
            .filterNotNull()
            .map { it.getObjectGraphSize(visited) }
            .sum()
    }

private fun Any.getFieldReferencesSize(visited: MutableSet<Int>): Long =
    javaClass.classHierarchy.flatMap { it.declaredFields.asSequence() }
        .onEach { it.isAccessible = true }
        .filterNot { it.isStatic }
        .filterNot { it.type.isPrimitive }
        .mapNotNull { it.get(this) }
        .map { it.getObjectGraphSize(visited) }
        .sum()

private val Class<*>.classHierarchy: Sequence<Class<*>>
    get() = generateSequence(this) { it.superclass }

private val Field.isStatic: Boolean
    get() = (modifiers and Modifier.STATIC) != 0
