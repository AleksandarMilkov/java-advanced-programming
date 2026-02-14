# Faculty Management System

## Overview

This problem models a faculty system for managing students, courses, grades, and graduation.

It demonstrates:

- Complex OOP modeling
- Bidirectional relationships (Student - Course)
- Custom exceptions
- Streams & advanced sorting
- Business rules & constraints
- State mutation (graduation removal)

---

## Main Components

### Student
- Tracks courses and grades
- Maximum 3 grades per term
- Validates term limits
- Calculates term and overall averages
- Checks graduation condition

### Course
- Maintains enrolled students
- Computes average grade
- Tracks total students

### Faculty
- Registers students
- Adds grades with validation
- Automatically removes graduated students
- Maintains graduation logs
- Prints ranked students and course statistics

---

## Concepts Practiced

- Object relationships and aggregation
- Exception handling (custom checked exception)
- Stream API (sorting, limiting, collecting)
- Comparator chaining
- Business rule validation
- Data integrity enforcement
- System-level coordination class (Faculty)
