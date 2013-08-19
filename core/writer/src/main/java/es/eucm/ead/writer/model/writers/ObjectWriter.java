/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.ead.writer.model.writers;

import es.eucm.ead.model.interfaces.Element;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.interfaces.features.Identified;
import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.params.variables.EAdVarDef;
import es.eucm.ead.reader.DOMTags;
import es.eucm.ead.tools.EAdUtils;
import es.eucm.ead.tools.reflection.ReflectionClass;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import es.eucm.ead.tools.reflection.ReflectionField;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.writer.model.ModelVisitor;
import es.eucm.ead.writer.model.writers.simplifiers.Simplifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ObjectWriter extends AbstractWriter<Identified> {

	private static final Logger logger = LoggerFactory
			.getLogger("ObjectWriter");

	public static final String ASSETS_PREFIX = "@";

	public static final String ELEMENT_PREFIX = "";

	private int assetOrdinal = 0;

	private int elementOrdinal = 0;

	private boolean asset;

	/**
	 * Sometimes, some elements will be simplified and its ids will change. We
	 * need to keep track of these changes, because they can affect to
	 * references (through BasicElement)
	 */
	private Map<String, String> idTranslations;

	/**
	 * List with references to resolve
	 */
	private List<Reference> references;

	// PROFILING

	/**
	 * List with ids of elements already written
	 */
	private List<String> elements;
	/**
	 * List with ids of assets already written
	 */
	private List<String> assets;

	private Map<Class<?>, Integer> classProfilerAssets;
	private Map<Class<?>, Integer> classProfilerElements;

	private Simplifier simplifier;

	public ObjectWriter(ModelVisitor modelVisitor) {
		super(modelVisitor);
		elements = new ArrayList<String>();
		assets = new ArrayList<String>();
		simplifier = new Simplifier();
		classProfilerAssets = new HashMap<Class<?>, Integer>();
		classProfilerElements = new HashMap<Class<?>, Integer>();
		idTranslations = new HashMap<String, String>();
		references = new ArrayList<Reference>();
	}

	@Override
	public XMLNode write(Identified object) {
		if (object == null) {
			return null;
		}

		String id = object.getId();
		if (id == null) {
			object.setId(generateNewId());

			// If simplifications are enabled, well, we simplify
			if (modelVisitor.isSimplifcationsEnabled()) {
				Identified simplified = (Identified) simplifier
						.simplify(object);

				if (simplified != null && simplified != object) {
					if (simplified.getId() == null) {
						simplified.setId(generateNewId());
					}
					// Keep track if the id changed
					if (!object.getId().equals(simplified.getId())) {
						idTranslations.put(object.getId(), simplified.getId());
					}
					object = simplified;
				}
			}
			id = object.getId();
		}
		XMLNode node;

		if (asset) {
			node = new XMLNode(DOMTags.ASSET_TAG);
		} else {
			node = new XMLNode(DOMTags.ELEMENT_TAG);
		}

		// If the object gets here with a taken Id, its a repeated element, so we create a reference
		if (idTaken(id)) {
			node.setText(id);
		} else {
			ReflectionClass<?> clazz = ReflectionClassLoader
					.getReflectionClass(object.getClass());

			if (asset) {
				if (assets.contains(id)) {
					logger.error("Id repeated: {}", id);
				}
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
					// When an element is defined with BasicElement, it is a
					// reference to another element, so its real content is
					// stored somewhere else.
					// The node will wait until all elements are resolved to set
					// the id
					if (c.getType() == BasicElement.class) {
						references.add(new Reference(node, id));
						return node;
					}
					if (elements.contains(id)) {
						logger.error("Id repeated: {}", id);
					}
					elements.add(id);
					clazz = c;
				}
			}
			node.setAttribute(DOMTags.ID_AT, id);
			node
					.setAttribute(DOMTags.CLASS_AT, translateClass(clazz
							.getType()));
			increment(clazz.getType());
			while (clazz != null) {
				for (ReflectionField f : clazz.getFields()) {
					// Only store fields annotated with param
					if (f.getAnnotation(Param.class) != null) {
						Object value = f.getFieldValue(object);
						if (value != null) {
							modelVisitor.writeElement(value, object,
									new ObjectWriterListener(translateField(f
											.getName()), node));
						}
					}
				}
				clazz = clazz.getSuperclass();
			}
		}
		return node;
	}

	private boolean idTaken(String id) {
		return (asset ? assets.contains(id) : elements.contains(id));
	}

	public String generateNewId() {
		boolean takenId = true;
		String id = null;
		while (takenId) {
			id = EAdUtils.generateId(asset ? ASSETS_PREFIX : ELEMENT_PREFIX,
					asset ? assetOrdinal++ : elementOrdinal++);
			takenId = idTaken(id);
			if (takenId) {
				logger.debug("Id {} is taken. Generating a new id...", id);
			}
		}
		return id;
	}

	public void setAsset(boolean asset) {
		this.asset = asset;
	}

	public void clear() {
		asset = false;
		assets.clear();
		classProfilerAssets.clear();
		classProfilerElements.clear();
		elements.clear();
		idTranslations.clear();
		references.clear();
		assetOrdinal = 0;
		elementOrdinal = 0;
		simplifier.clear();
	}

	public int getSimplifications() {
		return simplifier.getSimplifications();
	}

	private void increment(Class<?> c) {
		Map<Class<?>, Integer> classProfiler = (asset ? classProfilerAssets
				: classProfilerElements);
		Integer i = classProfiler.get(c);
		if (i == null) {
			i = 0;
		}
		i++;
		classProfiler.put(c, i);
	}

	public Map<Class<?>, Integer> getClassProfilerAssets() {
		return classProfilerAssets;
	}

	public Map<Class<?>, Integer> getClassProfilerElements() {
		return classProfilerElements;
	}

	public Map<Object, Map<EAdVarDef<?>, EAdField<?>>> getFields() {
		return simplifier.getFields();
	}

	public static class ObjectWriterListener implements
			ModelVisitor.VisitorListener {

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

	public void resolveReferences() {
		for (Reference r : references) {
			r.resolve();
		}
	}

	public Simplifier getSimplifier() {
		return simplifier;
	}

	public class Reference {

		private XMLNode node;

		private String id;

		public Reference(XMLNode node, String id) {
			super();
			this.node = node;
			this.id = id;
		}

		public void resolve() {
			String newId = idTranslations.get(id);
			if (newId != null) {
				node.setText(newId);
			} else {
				node.setText(id);
			}
		}

	}

}
