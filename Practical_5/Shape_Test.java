package Practical_5;

abstract class Shape {
    String name;
    Shape(String name) {
        this.name = name;
    }

    abstract double area();
}

class Circle extends Shape {
    private double radius;
    Circle(String name, double radius) {
        super(name);
        this.radius = radius;
    }

    double area() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    private double length;
    private double width;
    Rectangle(String name, double length, double width) {
        super(name);
        this.length = length;
        this.width = width;
    }

    double area() {
        return length * width;
    }
}

class Triangle extends Shape {
    private double base;
    private double height;
    Triangle(String name, double base, double height) {
        super(name);
        this.base = base;
        this.height = height;
    }

    double area() {
        return 0.5 * base * height;
    }
}

public class Shape_Test {
    public static void main(String[] args) {

        Shape[] shapes = {
            new Circle("Circle", 5),
            new Rectangle("Rectangle", 10, 4),
            new Triangle("Triangle", 8, 6),
            new Circle("Circle", 3),
            new Rectangle("Rectangle", 7, 5)
        };

        double total = 0;
        double largest = 0;

        System.out.println("Shape Areas:");
        for (Shape shape : shapes) {
            double currentArea = shape.area();
            System.out.println("Area of " + shape.name + " = " + String.format("%.2f", currentArea)
            );
            total += currentArea;

            if (currentArea > largest) {
                largest = currentArea;
            }
        }
        System.out.println("------------------------------");
        System.out.println("Total Area = " + String.format("%.2f", total));
        System.out.println("Largest Area = " + String.format("%.2f", largest));
    }
}