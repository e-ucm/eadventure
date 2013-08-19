package es.eucm.ead.reader2.model.readers;

import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.reader.DOMTags;
import es.eucm.ead.reader2.model.ObjectsFactory;
import es.eucm.ead.reader2.model.ReaderVisitor;
import es.eucm.ead.tools.reflection.ReflectionClass;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionField;
import es.eucm.ead.tools.xml.XMLNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectReader extends AbstractReader<Identified> {

	private static final Logger logger = LoggerFactory
			.getLogger("ObjectReader");

	public ObjectReader(ObjectsFactory objectsFactory,
			ReaderVisitor readerVisitor) {
		super(objectsFactory, readerVisitor);
	}

	@Override
	public Identified read(XMLNode node) {

		// If the node has text, it's a reference to an object already defined
		if (!"".equals(node.getNodeText())) {
			return (Identified) objectsFactory
					.getObjectById(node.getNodeText());
		} else {
			Class<?> clazz = this.getNodeClass(node);
			if (clazz != null) {
				String id = node.getAttributeValue(DOMTags.ID_AT);
				Identified object = (Identified) objectsFactory.createObject(
						clazz, id);
				if (node.hasChildNodes()) {
					for (XMLNode child : node.getChildren()) {
						String fieldName = child
								.getAttributeValue(DOMTags.FIELD_AT);
						fieldName = translateField(fieldName);
						ReflectionField field = getField(clazz, fieldName);

						if (field != null) {
							readerVisitor.loadElement(child,
									new ObjectVisitorListener(object, field));
						} else {
							logger
									.warn(
											"{} param is not present in {}. It'll be ignored",
											new Object[] { fieldName, clazz });
						}
					}
				}
				return object;
			}
			logger.warn("Node with no class {}", node);
			return null;
		}
	}

	/**
	 * Returns the class for the element contained in the given node
	 *
	 * @param node
	 * @return
	 */
	public Class<?> getNodeClass(XMLNode node) {
		String clazz = node.getAttributeValue(DOMTags.CLASS_AT);
		return clazz == null ? null : getNodeClass(clazz);
	}

	public ReflectionField getField(Class<?> clazz, String fieldName) {
		ReflectionClass<?> reflectionClass = ReflectionClassLoader
				.getReflectionClass(clazz);
		while (reflectionClass != null) {
			ReflectionField f = reflectionClass.getField(fieldName);
			if (f != null) {
				Param p = f.getAnnotation(Param.class);
				if (p != null) {
					return f;
				}
			}
			reflectionClass = reflectionClass.getSuperclass();
		}
		return null;
	}

	public Class<?> getNodeClass(String clazz) {
		clazz = translateClass(clazz);
		Class<?> c = null;
		try {
			c = objectsFactory.getClassFromName(clazz);
		} catch (NullPointerException e) {
			logger.error("Error resolving class {}", clazz, e);
		}
		return c;
	}

	public static class ObjectVisitorListener implements
			ReaderVisitor.VisitorListener {

		private Object parent;

		private ReflectionField field;

		public ObjectVisitorListener(Object parent, ReflectionField field) {
			this.parent = parent;
			this.field = field;
		}

		@Override
		public boolean loaded(XMLNode node, Object object,
				boolean isNullInOrigin) {
			if (object != null || isNullInOrigin) {
				field.setFieldValue(parent, object);
				return true;
			}
			return false;
		}

	}
}
