package com.github.martindevi.instrumentation

import com.github.martindevi.instrumentation.agent.InstrumentationAgent
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.lang.reflect.Modifier

fun Any.getObjectSize(name: String): Long =
    getObjectSize(name = name, indent = "", visited = mutableSetOf())

private fun Any.getObjectSize(name: String, indent: String, visited: MutableSet<Int>): Long =
    if (visited.add(System.identityHashCode(this))) {
        val objectSize = InstrumentationAgent.getObjectSize(this)
        println("$indent$objectSize $name: $javaClass")
        objectSize +
                if (javaClass.isArray) {
                    if (javaClass.componentType.isPrimitive) {
                        // Array elements size is already considered by `getObjectSize` since they aren't boxed
                        0L
                    } else {
                        getArrayElementsSize("$indent  ", visited)
                    }
                } else {
                    getFieldsSize("${indent.replace('─', ' ').replace('├', '|')}├── ", visited)
                }
    } else {
        println("$indent* $name: $javaClass")
        0L
    }

private fun Any.getArrayElementsSize(indent: String, visited: MutableSet<Int>): Long =
    List(Array.getLength(this)) {
        Array.get(this, it)?.getObjectSize("[$it]", indent, visited) ?: 0
    }.sum()

private fun Any.getFieldsSize(indent: String, visited: MutableSet<Int>): Long =
    getAllFields()
        .map { field ->
            field.isAccessible = true
            val type = field.type
            if (type.isPrimitive) {
                // Primitive fields size is already considered by `getObjectSize` since there's no reference to them
                println("${indent}# ${field.name}: $type")
                0L
            } else {
                val value = field.get(this)
                if (value == null) {
                    println("${indent}! ${field.name}: null $type")
                    0L
                } else {
                    value.getObjectSize(field.name, indent, visited)
                }
            }
        }
        .sum()

private fun Any.getAllFields() =
    generateSequence<Class<*>>(javaClass) { it.superclass }.flatMap { it.declaredFields.asSequence() }.filterNot { it.isStatic }

private val Field.isStatic
    get() = (modifiers and Modifier.STATIC) != 0
