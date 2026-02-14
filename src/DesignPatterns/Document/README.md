# Document Viewer (Decorator Pattern)

## Overview

This problem implements a document viewer with optional enhancements like line numbers, word counts, and redaction of forbidden words.

It demonstrates the **Decorator Design Pattern**, allowing dynamic behavior addition to `Document` objects without modifying their base implementation.

---

## Pattern Used

### Decorator Pattern

- `Document` -> base interface
- `SimpleDocument` -> concrete component
- `DocumentDecorator` -> abstract decorator
- `NumberedDocument`, `CountDecorator`, `RedactedDecorator` -> concrete decorators

Enhancements can be stacked in any order at runtime.

---

## Features

- Add multiple documents
- Enable line numbers dynamically
- Enable word count dynamically
- Redact forbidden words
- Display enhanced document output

---

## Concepts Practiced

- Decorator Design Pattern
- Dynamic behavior extension
- Composition over inheritance
- String manipulation
- Streamlined document rendering
