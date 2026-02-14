package DesignPatterns.Bucket;

import java.util.*;

interface Component {
    void addComponent(String[] comps);
    void removeComponent(String[] comps);
    boolean hasChildren();
    String toStringHelper(int level);
}

class FileComponent implements Component {
    private final String name;

    public FileComponent(String name) {
        this.name = name;
    }

    @Override
    public void addComponent(String[] comps) {
        throw new IllegalArgumentException("Cannot add to a file");
    }

    @Override
    public void removeComponent(String[] comps) {
        //Nothing to remove
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public String toStringHelper(int level) {
        return "    ".repeat(level) + name + "\n";
    }
}

class DirectoryComponent implements Component {
    private final String name;
    private final Map<String, Component> children;

    public DirectoryComponent(String name) {
        this.name = name;
        this.children = new HashMap<>();
    }

    @Override
    public void addComponent(String[] comps) {
        if (comps.length == 0) return;

        String current = comps[0];
        if (comps.length == 1 && current.contains(".")) {
            children.put(current, new FileComponent(current));
            return;
        }

        children.putIfAbsent(current, new DirectoryComponent(current));
        children.get(current).addComponent(Arrays.copyOfRange(comps, 1, comps.length));
    }

    @Override
    public void removeComponent(String[] comps) {
        if (comps.length == 0) return;

        String current = comps[0];
        if (!children.containsKey(current)) {
            throw new RuntimeException("Object not found: " + current);
        }

        if (comps.length == 1 && current.contains(".")) {
            children.remove(current);
            return;
        }

        children.get(current).removeComponent(Arrays.copyOfRange(comps, 1, comps.length));
        if (!children.get(current).hasChildren()) {
            children.remove(current);
        }
    }

    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override
    public String toStringHelper(int level) {
        StringBuilder sb = new StringBuilder();
        sb.append("    ".repeat(level)).append(name).append("/\n");
        children.values().stream()
                .sorted(Comparator.comparing(c -> c.toStringHelper(0)))
                .forEach(child -> sb.append(child.toStringHelper(level + 1)));
        return sb.toString();
    }
}

class Bucket {
    private final String name;
    private final Map<String, Component> rootComponents;

    public Bucket(String name) {
        this.name = name;
        this.rootComponents = new HashMap<>();
    }

    public void addObject(String key) {
        String[] parts = key.split("/");
        if (parts.length == 0) return;

        rootComponents.putIfAbsent(parts[0], new DirectoryComponent(parts[0]));
        rootComponents.get(parts[0]).addComponent(Arrays.copyOfRange(parts, 1, parts.length));
    }

    public void removeObject(String key) {
        String[] parts = key.split("/");
        if (!rootComponents.containsKey(parts[0])) {
            throw new RuntimeException("Object not found: " + parts[0]);
        }

        rootComponents.get(parts[0]).removeComponent(Arrays.copyOfRange(parts, 1, parts.length));
        if (!rootComponents.get(parts[0]).hasChildren()) {
            rootComponents.remove(parts[0]);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("/\n");
        rootComponents.values().stream()
                .sorted(Comparator.comparing(c -> c.toStringHelper(0)))
                .forEach(comp -> sb.append(comp.toStringHelper(1)));
        return sb.toString();
    }
}

public class BucketTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bucket bucket = new Bucket("bucket");

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String command = parts[0];

            switch (command.toUpperCase()) {
                case "ADD":
                    bucket.addObject(parts[1]);
                    break;
                case "REMOVE":
                    bucket.removeObject(parts[1]);
                    break;
                case "PRINT":
                    System.out.print(bucket);
                    break;
            }
        }
    }
}
