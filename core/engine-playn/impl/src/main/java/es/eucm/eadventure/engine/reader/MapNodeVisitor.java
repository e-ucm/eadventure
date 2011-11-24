package es.eucm.eadventure.engine.reader;

import com.gwtent.reflection.client.Field;
import java.util.Map;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import es.eucm.eadventure.common.model.extra.impl.EAdMapImpl;

public class MapNodeVisitor extends NodeVisitor<Map<Object, Object>> {

	@SuppressWarnings("unchecked")
	@Override
	public Map<Object, Object> visit(Node node, Field field, Object parent) {
		NodeList nl = node.getChildNodes();
		
		Map<Object, Object> map = null;
		
		if (field == null || parent == null) {
			map = createNewMap(node);
		} else {
			map = (Map<Object, Object>) field.getFieldValue(parent);
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
		return new EAdMapImpl<Object, Object>(Object.class, Object.class);
	}
	
	@Override
	public String getNodeType() {
		return "map";
	}

}
