# News System (Observer Pattern)

## Overview

This problem implements a **news system** where users can subscribe to authors and categories. When a new article is published, all subscribers are notified automatically.

It demonstrates the **Observer Design Pattern**, allowing a one-to-many dependency between objects so that when one object changes state, its dependents are notified.

---

## Pattern Used

### Observer Pattern

- `Subscriber` -> interface for users interested in updates
- `Publisher` -> interface for authors or categories providing updates
- `User` -> concrete subscriber
- `Author` and `Category` -> concrete publishers
- `Article` -> the object containing news content and metadata

---

## Features

- Users can subscribe/unsubscribe to authors and categories
- Articles have metadata: category, author, content, timestamp
- Publishing an article notifies all relevant subscribers
- Display a user’s collected news in chronological order

---

## Concepts Practiced

- Observer Design Pattern
- Event-driven notifications
- Composition and interfaces
- Date and time handling
- Collections and sorting
