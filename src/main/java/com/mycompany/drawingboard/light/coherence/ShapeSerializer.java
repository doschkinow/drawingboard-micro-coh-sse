package com.mycompany.drawingboard.light.coherence;

import java.io.IOException;

import com.mycompany.drawingboard.light.Shape;
import com.mycompany.drawingboard.light.Shape.ShapeColor;
import com.mycompany.drawingboard.light.Shape.ShapeType;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PofWriter;

public class ShapeSerializer implements PofSerializer {

	@Override
	public Object deserialize(PofReader reader) throws IOException {
		Shape shape = new Shape();
		shape.setType(ShapeType.valueOf(reader.readString(0)));
        shape.setColor(ShapeColor.valueOf(reader.readString(1)));
        shape.setX(reader.readInt(2));
        shape.setY(reader.readInt(3));
		reader.readRemainder();
        
		return shape;
	}

	@Override
	public void serialize(PofWriter writer, Object o) throws IOException {
		Shape shape = (Shape) o;
		writer.writeString(0, shape.getType().toString());
        writer.writeString(1, shape.getColor().toString());
        writer.writeInt(2, shape.getX());
        writer.writeInt(3, shape.getY());
        writer.writeRemainder(null);
        
		return;
	}
}
