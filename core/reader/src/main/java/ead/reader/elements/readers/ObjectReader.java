package ead.reader.elements.readers;

import java.util.Collection;

import ead.common.interfaces.Param;
import ead.common.interfaces.features.Identified;
import ead.reader.adventure.DOMTags;
import ead.reader.elements.ElementsFactory;
import ead.reader.elements.XMLVisitor;
import ead.reader.elements.XMLVisitor.VisitorListener;
import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionField;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLNodeList;

public class ObjectReader extends AbstractReader<Identified> {

	public boolean asset;

	private static int idGenerator = 0;

	public ObjectReader(ElementsFactory elementsFactory, XMLVisitor xmlVisitor) {
		super(elementsFactory, xmlVisitor);
	}

	public void setAsset(boolean asset) {
		this.asset = asset;
	}

	@Override
	public Identified read(XMLNode node) {
		Identified element = null;

		if (node.getChildNodes().getLength() == 1
				&& !node.getChildNodes().item(0).hasChildNodes()
				&& node.getNodeText() != null) {
			if (asset) {
				element = elementsFactory.getAsset(node.getNodeText());
			} else {
				element = elementsFactory.getEAdElement(node.getNodeText());
			}

			return element;

		} else {
			Class<?> clazz = this.getNodeClass(node);
			element = (Identified) elementsFactory.createObject(clazz);
			String id = node.getAttributes().getValue(DOMTags.ID_AT);
			id = id != null && !asset ? id : asset ? "asset" + idGenerator++
					: "element" + idGenerator++;
			element.setId(id);

			String uniqueId = node.getAttributes().getValue(
					DOMTags.UNIQUE_ID_AT);
			if (uniqueId != null) {
				if (asset) {
					elementsFactory.putAsset(uniqueId, element);
				} else {
					elementsFactory.putEAdElement(uniqueId, element);
				}
			}

			XMLNodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				XMLNode child = children.item(i);
				String fieldName = child.getAttributes().getValue(
						DOMTags.PARAM_AT);
				ReflectionField field = getField(clazz, fieldName);

				if (field != null) {
					xmlVisitor.loadElement(child, new ObjectVisitorListener(
							element, field));
				} else {
					logger.warn(
							"{} param is not present in {}. It'll be ignored",
							new Object[] { fieldName, clazz });
				}
			}
			return element;
		}

	}

	public ReflectionField getField(Class<?> clazz, String fieldName) {
		ReflectionClass<?> reflectionClass = ReflectionClassLoader
				.getReflectionClass(clazz);
		while (reflectionClass != null) {
			Collection<ReflectionField> fields = reflectionClass.getFields();
			for (ReflectionField f : fields) {
				Param p = f.getAnnotation(Param.class);
				if (p != null && p.value().equals(fieldName)) {
					return f;
				}
			}
			reflectionClass = reflectionClass.getSuperclass();
		}
		return null;
	}

	public static class ObjectVisitorListener implements VisitorListener {

		private Object parent;

		private ReflectionField field;

		public ObjectVisitorListener(Object parent, ReflectionField field) {
			this.parent = parent;
			this.field = field;
		}

		@Override
		public void loaded(Object object) {
			field.setFieldValue(parent, object);
		}

	}

}
