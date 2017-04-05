package com.mycompany.drawingboard.light.coherence;

import com.mycompany.drawingboard.light.Drawing;
import com.mycompany.drawingboard.light.Shape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PofWriter;


public class DrawingSerializer implements PofSerializer {
	
	@Override
	@SuppressWarnings("unchecked")
	public Object deserialize(PofReader reader) throws IOException {
		Drawing drawing = new Drawing();
		drawing.setId(reader.readInt(0));
		drawing.setName(reader.readString(1));
		ArrayList<Shape> arrayList = new ArrayList<Shape>();
        drawing.setShapes((List<Shape>) reader.readCollection(2, arrayList));
		
        reader.readRemainder();
		
		return drawing;
	}

	@Override
	public void serialize(PofWriter writer, Object o) throws IOException {
		Drawing drawing = (Drawing) o;
		writer.writeInt(0, drawing.getId());
        writer.writeString(1, drawing.getName());
        writer.writeCollection(2, drawing.getShapes());
        writer.writeRemainder(null);
        
        return;
	}

}
