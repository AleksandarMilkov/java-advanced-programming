# Input/Output – Movie Theater

## Overview

This problem demonstrates:

- File/InputStream reading
- BufferedReader usage
- Parsing structured input
- Java Streams & Comparator sorting
- Basic OOP modeling

The system reads movie data from an input stream, calculates the average rating,
stores the movies, and prints them sorted using different criteria.

---

## Data Model

Each movie contains:

- Title
- Genre
- Year
- Average rating (calculated from multiple ratings)

Class structure:

- `Movie`
- `MovieTheater`
- `MovieTheaterTester`

---

## Implemented Features

### 1. Reading from InputStream

Movies are read in structured format:

- Number of movies
- Title
- Genre
- Year
- Space-separated ratings

---

### 2. Sorting with Streams

Movies are printed sorted by:

- Rating (descending), then title
- Genre, then title
- Year, then title


---

## Concepts Practiced

- Java I/O (InputStream, BufferedReader)
- Data parsing
- Streams API
- Comparator chaining
- Clean separation of concerns

