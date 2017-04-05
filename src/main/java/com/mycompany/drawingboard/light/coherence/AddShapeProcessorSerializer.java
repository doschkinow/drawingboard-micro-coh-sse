package com.mycompany.drawingboard.light.coherence;

import java.io.IOException;

import com.mycompany.drawingboard.light.Shape;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PofWriter;

public class AddShapeProcessorSerializer implements PofSerializer {

		@Override
		public Object deserialize(PofReader reader) throws IOException {
			Shape shape = (Shape) reader.readObject(0);
			reader.readRemainder();
			
			return new AddShapeProcessor(shape);
		}
		
		@Override
		public void serialize(PofWriter writer, Object o) throws IOException {
			AddShapeProcessor addShapeProcessor = (AddShapeProcessor) o;
			writer.writeObject(0, addShapeProcessor.getShape());
			writer.writeRemainder(null);
			
			return;
		}
}
