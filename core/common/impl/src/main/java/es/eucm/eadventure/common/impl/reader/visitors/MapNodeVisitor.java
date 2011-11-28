package es.eucm.eadventure.common.impl.reader.visitors;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.model.extra.impl.EAdMapImpl;

public class MapNodeVisitor extends NodeVisitor<Map<Object, Object>> {

	@Override
	public Map<Object, Object> visit(Node node, Field field, Object parent, Class<?> listClass) {
		NodeList nl = node.getChildNodes();
		
		EAdMap<Object, Object> map = null;
		
		if (field == null || parent == null) {
			map = createNewMap(node);
		} else {
			map = getMapFromParent(field, parent);
		}
		
		String type;
		for(int i=0, cnt=nl.getLength(); i<cnt; i+=2)
		{
			type = nl.item(i).getNodeName();
			Object key = VisitorFactory.getVisitor(type).visit(nl.item(i), null, null, map.getKeyClass());
			type = nl.item(i+1).getNodeName();
			Object value = VisitorFactory.getVisitor(type).visit(nl.item(i+1), null, null, map.getValueClass());
			map.put(key, value);
		}

		return map;
	}
	
	private EAdMap<Object, Object> createNewMap(Node node) {
		//FIXME map doesn't store its classes...
		Node n = node.getAttributes().getNamedItem(DOMTags.KEY_CLASS_AT);
		String clazz = n.getNodeValue();
		clazz = translateClass(clazz);
		
		n = node.getAttributes().getNamedItem(DOMTags.VALUE_CLASS_AT);
		String value_clazz = n.getNodeValue();
		value_clazz = translateClass(clazz);

		Class<?> keyClass = null;
		Class<?> valueClass = null;
		try {
			keyClass = ClassLoader.getSystemClassLoader().loadClass(clazz);
			valueClass =  ClassLoader.getSystemClassLoader().loadClass(value_clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return new EAdMapImpl<Object, Object>(keyClass, valueClass);
	}

	@SuppressWarnings("unchecked")
	private EAdMap<Object, Object> getMapFromParent(Field field, Object parent) {
		boolean accessible = field.isAccessible();
		EAdMap<Object, Object> list = null;
		try {
			field.setAccessible(true);
			list = (EAdMap<Object, Object>) field.get(parent);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			field.setAccessible(accessible);
		}
		return list;
	}
	
	@Override
	public String getNodeType() {
		return DOMTags.MAP_TAG;
	}

}
