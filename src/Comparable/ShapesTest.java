package Comparable;

import java.util.*;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable{
    void scale(float scaleFactor);
}
interface Stackable{
    float weight();
}

abstract class Shape implements Stackable, Scalable, Comparable<Shape>{
    protected Color color;
    protected String id;
    public Shape(Color color, String id) {
        this.color = color;
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
class Circle extends Shape{
    private float radius;

    public Circle(Color color, String id, float radius) {
        super(color, id);
        this.radius = radius;
    }


    @Override
    public void scale(float scaleFactor) {
        this.radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float)Math.PI*radius*radius;
    }

    @Override
    public int compareTo(Shape o) {
        return Comparator.comparing(Shape::weight).reversed().thenComparing(Shape::getId).compare(this, o);
    }
    @Override
    public String toString() {
        return String.format("C: %-5s %-10s %10.2f",this.getId(), this.color, this.weight());
    }
}

class Rectangle extends Shape{
    private float width;
    private float height;
    public Rectangle(Color color, String id, float width, float height) {
        super(color, id);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        this.width *= scaleFactor;
        this.height *= scaleFactor;
    }

    @Override
    public float weight() {
        return width*height;
    }

    @Override
    public int compareTo(Shape o) {
        return Comparator.comparing(Shape::weight).reversed().thenComparing(Shape::getId).compare(this, o);
    }
    @Override
    public String toString() {
        return String.format("R: %-5s %-10s %10.2f",this.getId(), this.color, this.weight());
    }
}


class Canvas{
    private Set<Shape> shapes;

    public Canvas(){
        shapes = new TreeSet<>();
    }
    public void add(String id, Color color, float radius){
        shapes.add(new Circle(color, id, radius));
    }
    public void add(String id, Color color, float width, float height){
        shapes.add(new Rectangle(color, id, width, height));
    }
    public void scale(String id, float scaleFactor){

        for(Shape shape : shapes){
            if(shape.getId().equals(id)){
                shapes.remove(shape);
                shape.scale(scaleFactor);
                shapes.add(shape);
                break;
            }
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Shape shape : shapes){
            sb.append(shape.toString()).append("\n");
        }
        return sb.toString();
    }

}




public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}
