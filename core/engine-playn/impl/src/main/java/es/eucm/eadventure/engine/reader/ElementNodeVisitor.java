package es.eucm.eadventure.engine.reader;

import java.util.logging.Logger;

import com.google.gwt.xml.client.Node;
import com.gwtent.reflection.client.Field;

import es.eucm.eadventure.common.model.EAdElement;

/**
 * Visitor for the element. The element should be {@code <element id="ID"
 *  type="ENGINE_TYPE"
 *  class="EDITOR_TYPE"></element>}.
 */
public class ElementNodeVisitor extends NodeVisitor<EAdElement> {
	
	public static final String TAG = "element";

	protected static final Logger logger = Logger.getLogger("ElementNodeVisitor");

	@Override
	public EAdElement visit(Node node, Field field, Object parent) {
		EAdElement element = (EAdElement) ObjectFactory.getObject(GWTReader.getNodeText(node), EAdElement.class);
		if (element != null) {
			setValue(field, parent, element);
			return element;
		}
		
		if (node == null || node.getAttributes() == null)
			logger.severe("Unexpected null");

		Node n = node.getAttributes().getNamedItem(DOMTags.UNIQUE_ID_AT);
		String uniqueId = n != null ? n.getNodeValue() : null;
		n = node.getAttributes().getNamedItem(DOMTags.ID_AT);
		String id = n != null ? n.getNodeValue() : null;

		n = node.getAttributes().getNamedItem(loaderType);
		String clazz = null;
		if (n != null) {
			clazz = node.getAttributes().getNamedItem(loaderType).getNodeValue();
			clazz = translateClass(clazz);
		} else {
			System.out.println("wired null");
		}
		
		element = ModelElementFactory.getInstance(clazz, id);
		
		if (element != null)
			ObjectFactory.addElement(uniqueId, element);
		setValue(field, parent, element);

		readFields(element, node);
		
		return element;
	}

	@Override
	public String getNodeType() {
		return TAG;
	}

}
