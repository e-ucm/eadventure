package ead.writer.model.writers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.interfaces.features.Identified;
import ead.reader.DOMTags;
import ead.tools.reflection.ReflectionClass;
import ead.tools.reflection.ReflectionClassLoader;
import ead.tools.reflection.ReflectionField;
import ead.tools.xml.XMLNode;
import ead.writer.model.ModelVisitor;
import ead.writer.model.ModelVisitor.VisitorListener;

public class ObjectWriter extends AbstractWriter<Identified> {

	private static final Logger logger = LoggerFactory
			.getLogger("ObjectWriter");

	private boolean asset;

	/**
	 * List with ids of elements already written
	 */
	private List<String> elements;

	/**
	 * List with ids of assets already written
	 */
	private List<String> assets;

	public ObjectWriter(ModelVisitor modelVisitor) {
		super(modelVisitor);
		elements = new ArrayList<String>();
		assets = new ArrayList<String>();
	}

	@Override
	public XMLNode write(Identified object) {
		if (object == null) {
			return null;
		}

		XMLNode node = null;
		String id = object.getId();
		if (asset) {
			node = modelVisitor.newNode(DOMTags.ASSET_TAG);
		} else {
			node = modelVisitor.newNode(DOMTags.ELEMENT_TAG);
		}

		if ((asset && assets.contains(id)) || (!asset && elements.contains(id))) {
			node.setText(id);
		} else {
			ReflectionClass<?> clazz = ReflectionClassLoader
					.getReflectionClass(object.getClass());

			if (asset) {
				assets.add(id);
			} else {
				ReflectionClass<?> c = ReflectionClassLoader
						.getReflectionClass(object.getClass());
				while (c != null && !c.hasAnnotation(Element.class)) {
					c = c.getSuperclass();
				}

				if (c == null) {
					logger
							.warn(
									"{} (and any of its superclasses) is not annotated with elements. The object is stored as null (an empty node).",
									object.getClass());
					return node;
				} else {
					elements.add(id);
					clazz = c;
				}
			}
			node.setAttribute(DOMTags.ID_AT, id);
			node
					.setAttribute(DOMTags.CLASS_AT, translateClass(clazz
							.getType()));
			while (clazz != null) {
				for (ReflectionField f : clazz.getFields()) {
					// Only store fields annotated with param
					if (f.getAnnotation(Param.class) != null) {
						Object value = f.getFieldValue(object);
						if (value != null) {
							modelVisitor
									.writeElement(value,
											new ObjectWriterListener(f
													.getName(), node));
						}
					}
				}
				clazz = clazz.getSuperclass();
			}
		}
		return node;
	}

	public void setAsset(boolean asset) {
		this.asset = asset;
	}

	public void clear() {
		asset = false;
		assets.clear();
		elements.clear();
	}

	public static class ObjectWriterListener implements VisitorListener {

		private String fieldName;

		private XMLNode parent;

		public ObjectWriterListener(String fieldName, XMLNode parent) {
			this.fieldName = fieldName;
			this.parent = parent;
		}

		@Override
		public void load(XMLNode node, Object object) {
			node.setAttribute(DOMTags.FIELD_AT, fieldName);
			parent.append(node);
		}

	}

}
