package es.eucm.eadventure.engine.reader;

import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.TypeOracle;

import java.util.Map;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.model.extra.EAdMap;
import es.eucm.eadventure.common.model.extra.impl.EAdMapImpl;

public class MapNodeVisitor extends NodeVisitor<Map<Object, Object>> {

	@SuppressWarnings("unchecked")
	@Override
	public Map<Object, Object> visit(Node node, Field field, Object parent, Class<?> listClass) {
		NodeList nl = node.getChildNodes();
		
		EAdMap<Object, Object> map = null;
		
		if (field == null || parent == null) {
			map = createNewMap(node);
		} else {
			map = (EAdMap<Object, Object>) field.getFieldValue(parent);
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
		Node n = node.getAttributes().getNamedItem(DOMTags.KEY_CLASS_AT);
		String clazz = n.getNodeValue();
		clazz = translateClass(clazz);
		
		ClassType<?> keyClassType = TypeOracle.Instance.getClassType(clazz);

		n = node.getAttributes().getNamedItem(DOMTags.VALUE_CLASS_AT);
		String value_clazz = n.getNodeValue();
		value_clazz = translateClass(clazz);

		ClassType<?> valueClassType = TypeOracle.Instance.getClassType(value_clazz);

		return new EAdMapImpl<Object, Object>(keyClassType.getClass(), valueClassType.getClass());
	}
	
	@Override
	public String getNodeType() {
		return DOMTags.MAP_TAG;
	}

}
