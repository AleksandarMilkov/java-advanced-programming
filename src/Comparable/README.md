# Comparable – Shapes & Canvas

## Overview

This problem demonstrates:

- `Comparable<T>`
- `Comparator` chaining
- `TreeSet` ordering
- OOP (abstraction, inheritance, polymorphism)
- Interfaces and enums

The system models shapes placed on a canvas.  
Shapes can be added, scaled, and are automatically sorted.

---

## Key Design

- `Shape` (abstract class)
- `Circle` and `Rectangle`
- `Scalable` and `Stackable` interfaces
- `Color` enum
- `Canvas` uses `TreeSet<Shape>`

---

## Sorting Logic

Shapes are sorted by:

1. Weight (descending)
2. ID (ascending)

```java
Comparator.comparing(Shape::weight)
          .reversed()
          .thenComparing(Shape::getId);
```

---

## Important Detail

When scaling a shape, it must be removed and reinserted into the `TreeSet`
because its weight (sorting key) changes.

---

## Concepts Practiced

- Comparable vs Comparator
- Ordered collections
- Object mutation inside `TreeSet`
- Clean OOP modeling
