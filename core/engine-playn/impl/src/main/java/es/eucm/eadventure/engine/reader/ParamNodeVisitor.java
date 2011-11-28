package es.eucm.eadventure.engine.reader;

import com.gwtent.reflection.client.Field;

import java.util.logging.Level;

import com.google.gwt.xml.client.Node;

import es.eucm.eadventure.common.model.DOMTags;

public class ParamNodeVisitor extends NodeVisitor<Object> {

	@Override
	public Object visit(Node node, Field field, Object parent, Class<?> listClass) {
		String textContent = GWTReader.getNodeText(node);
		Object object = null;
		if (textContent != null && !textContent.equals("")) {
			Class<?> c = listClass;
			if (c == null && field != null)
				c = field.getType().getClass();
			if (c == null || node.getAttributes().getNamedItem(DOMTags.CLASS_AT) != null) {
				String clazz = node.getAttributes().getNamedItem(DOMTags.CLASS_AT).getNodeValue();
				clazz = translateClass(clazz);
				try {
				c = ObjectFactory.getClassFromName(clazz);
				} catch (NullPointerException e) {
					logger.severe(e.getMessage());
				}
			}
			
			String value = textContent;
			if ( ObjectFactory.getParamsMap().containsKey(value)){
				object = ObjectFactory.getParamsMap().get(value);
				logger.finest(value + " of value " + object.toString() + " and type "  + object.getClass() + " was compressed." );
			}
			else {
				object = ObjectFactory.getObject(value, c);
				ObjectFactory.getParamsMap().put("param"+ObjectFactory.getParamsMap().keySet().size(), object);
			}
			 

			setValue(field, parent, object);

			try {
				setValue(field, parent, object);
			} catch (IllegalArgumentException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			} 
		} 
		return object;
	}
	
	@Override
	public String getNodeType() {
		return DOMTags.PARAM_AT;
	}

}
