# Instrumentation Object Size

Examine the size of objects at runtime, to analyze memory impact of data
structures.

The algorithm will look a the size of objects, then recurisvely descend
the tree formed by instances referenced in fields, to evaluate the total
size which the structure occupies in memory.

Example with different map structures containing 16 entries with keys and
values which are strings of 32 characters.

| Structure        | Size |
|------------------|------|
| `LinkedHashMap`  | 4168 |
| `HashMap`        | 4032 |
| `TreeMap`        | 4016 |
| `SimpleArrayMap` | 3600 |
