package es.eucm.eadventure.engine.reader;

import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.ReflectionRequiredException;
import com.gwtent.reflection.client.TypeOracle;

import java.util.logging.Level;

import com.google.gwt.xml.client.Node;

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

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(Node node, Field field, Object parent) {
		String textContent = GWTReader.getNodeText(node);
		Object object = null;
		if (textContent != null && !textContent.equals("")) {
			if (node == null || node.getAttributes() == null)
				logger.severe("Unexpected null");

			String clazz = node.getAttributes().getNamedItem(DOMTags.CLASS_AT).getNodeValue();
			clazz = translateClass(clazz);
			
			if ( ObjectFactory.getParamsMap().containsKey(textContent)){
				logger.info(textContent + " was compressed." );
				object =  ObjectFactory.getParamsMap().get(textContent);
			}
			else {
				EAdParam param = constructParam( textContent, clazz );
				if (param != null) {
					String key = "param"+ObjectFactory.getParamsMap().keySet().size();
					ObjectFactory.getParamsMap().put(key, param);
					object = param;
				} else {
					if (clazz.equals("java.lang.Class")) {
						object = getObjectFromName(textContent);
					} else {
						try {
							ClassType<?> classType = TypeOracle.Instance.getClassType(clazz);
							if (classType.isEnum() != null) {
								
								@SuppressWarnings({ "rawtypes" })
								Class<? extends Enum> enumClass = (Class<? extends Enum>) classType.isEnum().getDeclaringClass();
								object = Enum.valueOf(enumClass, textContent);
							}
							if (object == null) 
								object = ObjectFactory.getObject( textContent, classType.getDeclaringClass());
						} catch (ReflectionRequiredException e) {
							
						}
						if (object == null) {
							Class<?> c = (Class<?>) getObjectFromName(clazz);
							object = ObjectFactory.getObject( textContent, c);
						}

					}
				}

			}

			try {
				setValue(field, parent, object);
				
			} catch (IllegalArgumentException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			} 
		} else {
			logger.severe("Unexpected null textContent");
		}
		return object;
	}
	
	private Object getObjectFromName(String value) {
		if (value.equals("java.lang.Float"))
			return  Float.class;
		else if (value.equals("java.lang.Integer"))
			return Integer.class;
		else if (value.equals("java.lang.Boolean"))
			return Boolean.class;
		else
			return TypeOracle.Instance.getClassType(value).getDeclaringClass();
	}
	
	
	private EAdParam constructParam( String value, String clazz ) {
		
		if (clazz.equals(EAdString.class.getName()))
			return new EAdString(value);
		if (clazz.equals(EAdColor.class.getName()))
			return new EAdColor(value);
		if (clazz.equals(EAdLinearGradient.class.getName()))
			return new EAdLinearGradient(value);
		if (clazz.equals(EAdPaintImpl.class.getName()))
			return new EAdPaintImpl(value);
		if (clazz.equals(EAdFontImpl.class.getName()))
			return new EAdFontImpl(value);
		if (clazz.equals(EAdPositionImpl.class.getName()))
			return new EAdPositionImpl(value);
		if (clazz.equals(EAdRectangleImpl.class.getName())) 
			return new EAdRectangleImpl(value);
		if (clazz.equals(EAdURIImpl.class.getName())) 
			return new EAdURIImpl(value);
		
		return null;
	}


	
	@Override
	public String getNodeType() {
		return "param";
	}

}
