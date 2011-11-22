package es.eucm.eadventure.common.impl.DOMreader;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.eucm.eadventure.common.impl.DOMTags;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.extra.impl.EAdListImpl;

public class ListNodeVisitor extends NodeVisitor<Object> {

	@Override
	public Object visit(Node node) {
		NodeList nl = node.getChildNodes();
		
		Node n = node.getAttributes().getNamedItem(DOMTags.CLASS_AT);
		String clazz = n.getNodeValue();
		clazz = translateClass(clazz);
		
		Class<?> c = null;
		try {
			c = ClassLoader.getSystemClassLoader().loadClass(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		EAdList<Object> list = new EAdListImpl<Object>(c);
		
		String type;
		for(int i=0, cnt=nl.getLength(); i<cnt; i++)
		{
			type = nl.item(i).getNodeName();
			Object object = VisitorFactory.getVisitor(type).visit(nl.item(i));
			list.add(object);
		}
		return list;
	}

	@Override
	public String getNodeType() {
		return "list";
	}

}
