package es.eucm.eadventure.common.impl.reader.visitors;

import java.lang.reflect.Field;
import java.util.logging.Level;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;

public class ListNodeVisitor extends NodeVisitor<EAdList<Object>> {

	@Override
	public EAdList<Object> visit(Node node, Field field, Object parent, Class<?> listClass) {
		NodeList nl = node.getChildNodes();
		
		EAdList<Object> list = null;
		
		if (field == null || parent == null) {
			list = createNewList(node);
		} else {
			list = getListFromParent(field, parent);
		}
		
		String type;
		for(int i=0, cnt=nl.getLength(); i<cnt; i++)
		{
			type = nl.item(i).getNodeName();
			Object object = VisitorFactory.getVisitor(type).visit(nl.item(i), null, null, list.getValueClass());
			list.add(object);
		}
		return list;
	}
	
	private EAdList<Object> createNewList(Node node) {
		Node n = node.getAttributes().getNamedItem(DOMTags.CLASS_AT);
		String clazz = n.getNodeValue();
		clazz = translateClass(clazz);
		
		Class<?> c = null;
		try {
			c = ClassLoader.getSystemClassLoader().loadClass(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return new EAdListImpl<Object>(c);
	}

	private EAdList<Object> getListFromParent(Field field, Object parent) {
		boolean accessible = field.isAccessible();
		EAdList<Object> list = null;
		try {
			field.setAccessible(true);
			list = (EAdList<Object>) field.get(parent);
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
		return DOMTags.LIST_TAG;
	}

}
