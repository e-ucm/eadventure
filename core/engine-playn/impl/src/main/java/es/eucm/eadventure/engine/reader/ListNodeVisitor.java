package es.eucm.eadventure.engine.reader;

import java.util.logging.Level;

import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.TypeOracle;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;

public class ListNodeVisitor extends NodeVisitor<EAdList<Object>> {

	@SuppressWarnings("unchecked")
	@Override
	public EAdList<Object> visit(Node node, Field field, Object parent) {
		NodeList nl = node.getChildNodes();
		
		EAdList<Object> list = null;
		
		if (field == null || parent == null) {
			list = createNewList(node);
		} else {
			try {
				list = (EAdList<Object>) field.getFieldValue(parent);
			} catch (ClassCastException e) {
				logger.log(Level.WARNING, "Fail to cast as list, field: " + field.getName() + " in " + parent);
				list = createNewList(node);
			}
		}
		
		String type;
		for(int i=0, cnt=nl.getLength(); i<cnt; i++)
		{
			type = nl.item(i).getNodeName();
			Object object = VisitorFactory.getVisitor(type).visit(nl.item(i), null, null);
			list.add(object);
		}
		return list;
	}
	
	private EAdList<Object> createNewList(Node node) {
		if (node == null || node.getAttributes() == null)
			logger.severe("Unexpected null");

		Node n = node.getAttributes().getNamedItem(DOMTags.CLASS_AT);
		String clazz = n.getNodeValue();
		clazz = translateClass(clazz);
		
		ClassType<?> classType = TypeOracle.Instance.getClassType(clazz);

		return new EAdListImpl<Object>(classType.getClass());
	}
	
	@Override
	public String getNodeType() {
		return "list";
	}

}
