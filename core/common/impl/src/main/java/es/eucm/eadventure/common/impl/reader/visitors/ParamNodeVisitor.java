package es.eucm.eadventure.common.impl.reader.visitors;

import java.lang.reflect.Field;
import java.util.logging.Level;

import org.w3c.dom.Node;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.impl.reader.extra.ObjectFactory;

public class ParamNodeVisitor extends NodeVisitor<Object> {

	@Override
	public Object visit(Node node, Field field, Object parent, Class<?> listClass) {
		if (node.getTextContent() != null) {
			try {
				Class<?> c = listClass;
				if (c == null || node.getAttributes().getNamedItem(DOMTags.CLASS_AT) != null) {
					String clazz = node.getAttributes().getNamedItem(DOMTags.CLASS_AT).getNodeValue();
					clazz = translateClass(clazz);
					c = ClassLoader.getSystemClassLoader().loadClass(clazz);
				} 
				
				Object object = null;

				String value = node.getTextContent();
				if ( ObjectFactory.getParamsMap().containsKey(value)){
					object = ObjectFactory.getParamsMap().get(value);
					logger.info(value + " of value " + object.toString() + " and type "  + object.getClass() + " was compressed." );
				}
				else {
					object = ObjectFactory.getObject(value, c);
					ObjectFactory.getParamsMap().put("param"+ObjectFactory.getParamsMap().keySet().size(), object);
				}
				 

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
		return DOMTags.PARAM_AT;
	}

}
