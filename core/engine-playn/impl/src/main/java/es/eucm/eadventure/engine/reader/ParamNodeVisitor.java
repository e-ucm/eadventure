package es.eucm.eadventure.engine.reader;

import com.gwtent.reflection.client.Field;

import java.util.logging.Level;

import com.google.gwt.xml.client.Node;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.params.EAdFontImpl;
import es.eucm.eadventure.common.params.EAdParam;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.params.EAdURIImpl;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.fills.impl.EAdLinearGradient;
import es.eucm.eadventure.common.params.fills.impl.EAdPaintImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;

public class ParamNodeVisitor extends NodeVisitor<Object> {

	@Override
	public Object visit(Node node, Field field, Object parent, Class<?> listClass) {
		String textContent = GWTReader.getNodeText(node);
		Object object = null;
		if (textContent != null && !textContent.equals("")) {
			Class<?> c = listClass;
			if (c == null || node.getAttributes().getNamedItem(DOMTags.CLASS_AT) != null) {
				String clazz = node.getAttributes().getNamedItem(DOMTags.CLASS_AT).getNodeValue();
				clazz = translateClass(clazz);
				c = ObjectFactory.getClassFromName(clazz);
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
