package com.mycompany.drawingboard.light;

import java.io.Serializable;

/**
 * POJO representing a shape.
 */
public class Shape implements Serializable {

    /**
     * Shape types.
     */
    public static enum ShapeType {

        BIG_CIRCLE,
        SMALL_CIRCLE,
        BIG_SQUARE,
        SMALL_SQUARE,
    }

    /**
     * Shape colors.
     */
    public static enum ShapeColor {

        RED,
        GREEN,
        BLUE,
        YELLOW,
    }

    /**
     * Type of the shape.
     */
    private ShapeType type;
    
    /**
     * Shape color.
     */
    private ShapeColor color;
    
    /**
     * Shape coordinates.
     */
    private int x, y;

    public ShapeType getType() {
        return type;
    }

    public void setType(ShapeType type) {
        this.type = type;
    }

    public ShapeColor getColor() {
        return color;
    }

    public void setColor(ShapeColor color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "Shape(" + x + ", " + y + ", " + type + ", " + color + ")";
    }
}
