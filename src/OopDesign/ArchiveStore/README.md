# OOP Design – Archive Store

## Overview

This problem models an archive management system using proper OOP design principles.

It demonstrates:

- Abstract classes
- Inheritance
- Polymorphism
- Custom exceptions
- Logging system behavior
- Working with `LocalDate`

---

## Architecture

### Abstract Class

`Archive`
- id
- dateArchived
- abstract method `open(LocalDate date)`

---

### Concrete Implementations

#### 1. LockedArchive
- Cannot be opened before a specific date.
- Prints message if opened too early.

#### 2. SpecialArchive
- Can only be opened a limited number of times.
- Tracks number of openings.
- Blocks access after limit is reached.

---

### ArchiveStore

Responsibilities:

- Stores archived items
- Archives items with a date
- Opens items by ID
- Logs all operations
- Throws `NonExistingItemException` if item is missing

---

## Concepts Practiced

- OOP modeling
- Polymorphism through abstract methods
- Custom exception handling
- State tracking inside objects
- Clean separation of responsibilities
