# Bucket (Composite Pattern)

## Overview

This problem implements a simplified cloud storage bucket system with directories and files.

It demonstrates the **Composite Design Pattern**, where files and directories are treated uniformly through a common interface.

---

## Pattern Used

### Composite Pattern

- `Component` -> common interface
- `FileComponent` -> leaf node
- `DirectoryComponent` -> composite node (can contain children)
- `Bucket` -> root container

Both files and directories implement the same interface, allowing recursive operations.

---

## Features

- Add nested files/directories using path strings (e.g., folder1/folder2/file.txt)
- Remove files or directories
- Automatically remove empty directories
- Recursive tree printing with indentation
- Sorted output using Streams & Comparator

---

## Concepts Practiced

- Composite Design Pattern
- Recursion
- Tree structure modeling
- Map-based child management
- Stream sorting
- Clean separation of responsibilities
