package com.mycompany.drawingboard.light;

import java.io.Serializable;
import java.util.List;

/**
 * POJO representing a drawing.
 */
public class Drawing implements Serializable, Comparable<Drawing> {
	
	/** Drawing ID. */
    private int id;
    
    /** Drawing name. */
    private String name;
    
    /** 
     * List of shapes the drawing consists of (or {@code null} if the drawing
     * is empty.
     */
    private List<Shape> shapes;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }
    
    public int compareTo(Drawing o) {    
        return Integer.compare(this.id, o.getId());    
    }
}
