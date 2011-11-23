package es.eucm.eadventure.common.impl.reader.visitors;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.eucm.eadventure.common.impl.DOMTags;
import es.eucm.eadventure.common.model.extra.impl.EAdMapImpl;

public class MapNodeVisitor extends NodeVisitor<Map<Object, Object>> {

	@Override
	public Map<Object, Object> visit(Node node, Field field, Object parent) {
		NodeList nl = node.getChildNodes();
		
		Map<Object, Object> map = null;
		
		if (field == null || parent == null) {
			map = createNewMap(node);
		} else {
			map = getMapFromParent(field, parent);
		}
		
		String type;
		for(int i=0, cnt=nl.getLength(); i<cnt; i+=2)
		{
			type = nl.item(i).getNodeName();
			Object key = VisitorFactory.getVisitor(type).visit(nl.item(i), null, null);
			type = nl.item(i+1).getNodeName();
			Object value = VisitorFactory.getVisitor(type).visit(nl.item(i+1), null, null);
			map.put(key, value);
		}

		return map;
	}
	
	private Map<Object, Object> createNewMap(Node node) {
		//FIXME map doesn't store its classes...
		
		Node n = node.getAttributes().getNamedItem(DOMTags.CLASS_AT);
		String clazz = n.getNodeValue();
		clazz = translateClass(clazz);
		
		Class<?> c = null;
		try {
			c = ClassLoader.getSystemClassLoader().loadClass(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return new EAdMapImpl<Object, Object>(c, c);
	}

	private Map<Object, Object> getMapFromParent(Field field, Object parent) {
		boolean accessible = field.isAccessible();
		Map<Object, Object> list = null;
		try {
			field.setAccessible(true);
			list = (Map<Object, Object>) field.get(parent);
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
		return "map";
	}

}
