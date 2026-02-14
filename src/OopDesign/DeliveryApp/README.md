# Delivery App

## Overview

This problem models a food delivery platform.

It demonstrates:

- Interface with default methods
- Anonymous classes
- OOP system design
- Collections (Map, HashMap)
- Streams & complex sorting
- Business logic modeling

---

## Main Components

### Location (Interface)
- Represents coordinates (x, y)
- Includes default Manhattan distance calculation

### DeliveryPerson
- Tracks current location
- Calculates delivery fee based on distance
- Tracks total deliveries and earnings

### Restaurant
- Tracks total orders
- Calculates average earnings per order

### User
- Supports multiple addresses
- Tracks total orders and total spent

### DeliveryApp
- Registers users, restaurants, delivery persons
- Assigns nearest available delivery person
- Uses streams for sorting and selection

---

## Concepts Practiced

- Real-world OOP modeling
- Polymorphism via interfaces
- Default interface methods
- Anonymous inner classes
- Stream API (min, sorting, comparators)
- Business rule implementation
- Aggregation and state tracking
