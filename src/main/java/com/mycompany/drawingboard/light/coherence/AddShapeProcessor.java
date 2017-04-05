package com.mycompany.drawingboard.light.coherence;

import java.io.Serializable;

import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;

import com.mycompany.drawingboard.light.Drawing;
import com.mycompany.drawingboard.light.Shape;

public class AddShapeProcessor implements EntryProcessor<Integer, Drawing, Boolean>,  Serializable {

	/**
	 * generated 
	 */
	private static final long serialVersionUID = 1L;
	
	private Shape shape;

	public AddShapeProcessor() {
        
    }
    
    public AddShapeProcessor(Shape shape) {
        this.shape = shape;
    }
    
    public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	@Override
	public Boolean process(MutableEntry<Integer, Drawing> entry, Object... arguments) throws EntryProcessorException {
		
		if (entry.exists()) {
			Drawing drawing = entry.getValue();
            drawing.getShapes().add(shape);
            entry.setValue(drawing);
            System.out.println("Version1: Entryprocessor called to " + drawing.getName());
            
            return true;
        } else {
            return false;
        }
		
	}

}
