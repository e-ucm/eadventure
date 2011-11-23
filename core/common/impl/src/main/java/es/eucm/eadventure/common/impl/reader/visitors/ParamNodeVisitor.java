package es.eucm.eadventure.common.impl.reader.visitors;

import java.lang.reflect.Field;
import java.util.logging.Level;

import org.w3c.dom.Node;

import es.eucm.eadventure.common.impl.DOMTags;
import es.eucm.eadventure.common.impl.reader.extra.ObjectFactory;

public class ParamNodeVisitor extends NodeVisitor<Object> {

	@Override
	public Object visit(Node node, Field field, Object parent) {
		if (node.getTextContent() != null) {
			String clazz = node.getAttributes().getNamedItem(DOMTags.CLASS_AT).getNodeValue();
			clazz = translateClass(clazz);
			try {
				Class<?> c = ClassLoader.getSystemClassLoader().loadClass(clazz);
				Object object = ObjectFactory.getObject(node.getTextContent(), c);

				setValue(field, parent, object);
				
				return object;
			} catch (ClassNotFoundException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			} catch (IllegalArgumentException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			} 
		}
		return null;
	}


	
	@Override
	public String getNodeType() {
		return "param";
	}

}
