package es.eucm.eadventure.engine.reader;

import java.util.logging.Logger;

import com.google.gwt.xml.client.Node;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.TypeOracle;

import es.eucm.eadventure.common.model.DOMTags;
import es.eucm.eadventure.common.model.EAdElement;

/**
 * Visitor for the element. The element should be {@code <element id="ID"
 *  type="ENGINE_TYPE"
 *  class="EDITOR_TYPE"></element>}.
 */
public class ElementNodeVisitor extends NodeVisitor<EAdElement> {
	
	protected static final Logger logger = Logger.getLogger("ElementNodeVisitor");

	@Override
	public EAdElement visit(Node node, Field field, Object parent, Class<?> listClass) {
		EAdElement element = null;
		if (node.getChildNodes().getLength() == 1 && !node.getChildNodes().item(0).hasChildNodes()) {
			element = (EAdElement) ObjectFactory.getObject(GWTReader.getNodeText(node), EAdElement.class);
			if (element != null && !(element instanceof ProxyElement)) {
				setValue(field, parent, element);
				return element;
			} else if (element != null) {
				((ProxyElement) element).setField(field);
				((ProxyElement) element).setParent(parent);
				return element;
			}
		}

		Node n = node.getAttributes().getNamedItem(DOMTags.UNIQUE_ID_AT);
		String uniqueId = n != null ? n.getNodeValue() : null;
		n = node.getAttributes().getNamedItem(DOMTags.ID_AT);
		String id = n != null ? n.getNodeValue() : null;

		n = node.getAttributes().getNamedItem(loaderType);
		String clazz = null;
		if (n != null) {
			clazz = n.getNodeValue();
			clazz = translateClass(clazz);
		} else {
			logger.info("Null element for: " + (parent != null ? parent.getClass() : node.getNodeName()));
		}
		
		if (clazz != null) {
			ClassType<?> classType = TypeOracle.Instance.getClassType(clazz);
			if (classType.findConstructor() != null) {
				element = (EAdElement) classType.findConstructor().newInstance();
				element.setId(id);
			}
		}

		if (element != null)
			ObjectFactory.addElement(uniqueId, element);
		
		setValue(field, parent, element);

		if (element != null)
			readFields(element, node);
		
		return element;
	}

	@Override
	public String getNodeType() {
		return DOMTags.ELEMENT_AT;
	}

}
