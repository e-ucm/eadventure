package es.eucm.eadventure.engine.reader;

import java.util.logging.Level;

import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.TypeOracle;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;

public class ListNodeVisitor extends NodeVisitor<EAdList<Object>> {

	@SuppressWarnings("unchecked")
	@Override
	public EAdList<Object> visit(Node node, Field field, Object parent, Class<?> listClass) {
		NodeList nl = node.getChildNodes();
		
		EAdList<Object> list = null;
		
		if (field == null || parent == null) {
			list = createNewList(node);
		} else {
			try {
				list = (EAdList<Object>) field.getFieldValue(parent);
				list.clear();
			} catch (ClassCastException e) {
				logger.log(Level.WARNING, "Fail to cast as list, field: " + field.getName() + " in " + parent);
			}
		}
		
		//TODO do for more tyes?
		if (list.getValueClass() == Integer.class ||
				list.getValueClass() == Float.class ||
				list.getValueClass() == EAdPosition.class || list.getValueClass() == EAdPositionImpl.class) {
			String value = GWTReader.getNodeText(node);
			if (value != null && !value.equals("")) {
				String[] values = value.split("\\|");
				for (int i = 0; i < values.length; i++) 
					list.add(ObjectFactory.getObject(values[i], list.getValueClass()));
			}
		} else {
			String type;
			for(int i=0, cnt=nl.getLength(); i<cnt; i++)
			{
				type = nl.item(i).getNodeName();
				Object object = VisitorFactory.getVisitor(type).visit(nl.item(i), null, null, list.getValueClass());
				if (object instanceof ProxyElement) {
					((ProxyElement) object).setList(list, i);
				}
				list.add(object);
			}
		}
		return list;
		
	}
	
	private EAdList<Object> createNewList(Node node) {
		Node n = node.getAttributes().getNamedItem(DOMTags.CLASS_AT);
		String clazz = n.getNodeValue();
		clazz = translateClass(clazz);
		
		ClassType<?> classType = TypeOracle.Instance.getClassType(clazz);

		return new EAdListImpl<Object>(classType.getClass());
	}
	
	@Override
	public String getNodeType() {
		return DOMTags.LIST_TAG;
	}

}
