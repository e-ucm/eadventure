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

package es.eucm.ead.writer.model;

import es.eucm.ead.model.assets.AssetDescriptor;
import es.eucm.ead.model.elements.EAdElement;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.elements.operations.EAdField;
import es.eucm.ead.model.params.EAdParam;
import es.eucm.ead.model.params.variables.EAdVarDef;
import es.eucm.ead.reader.DOMTags;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import es.eucm.ead.tools.xml.XMLNode;
import es.eucm.ead.writer.model.writers.ListWriter;
import es.eucm.ead.writer.model.writers.MapWriter;
import es.eucm.ead.writer.model.writers.ObjectWriter;
import es.eucm.ead.writer.model.writers.ParamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ModelVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger("ModelVisitor");

	private ReflectionProvider reflectionProvider;

	private boolean addedToRoot;

	private XMLNode classes;

	private XMLNode fields;

	private XMLNode params;

	private ParamWriter paramWriter;

	private ListWriter listWriter;

	private MapWriter mapWriter;

	private ObjectWriter objectWriter;

	private List<WriterStep> stepsQueue;

	private Map<Class<?>, String> classTranslations;
	private Map<String, String> fieldsTranslations;
	private Map<String, String> paramsTranslations;

	private XMLNode root;

	private boolean simplificationsEnabled;

	public ModelVisitor(ReflectionProvider reflectionProvider) {
		this.reflectionProvider = reflectionProvider;
		this.reflectionProvider = reflectionProvider;

		this.stepsQueue = new ArrayList<WriterStep>();
		this.classTranslations = new LinkedHashMap<Class<?>, String>();
		this.fieldsTranslations = new LinkedHashMap<String, String>();
		this.paramsTranslations = new LinkedHashMap<String, String>();
		// writers
		paramWriter = new ParamWriter(this);
		listWriter = new ListWriter(this);
		mapWriter = new MapWriter(this);
		objectWriter = new ObjectWriter(this);

		simplificationsEnabled = true;
	}

	public void writeElement(Object object, Object parent,
			VisitorListener listener) {
		stepsQueue.add(new WriterStep(object, parent, listener));
	}

	@SuppressWarnings("rawtypes")
	public boolean step() {
		if (stepsQueue.isEmpty()) {
			return true;
		}

		WriterStep step = stepsQueue.remove(0);
		Object o = step.getObject();
		Class<?> clazz = o == null ? null : o.getClass();
		VisitorListener listener = step.getListener();

		XMLNode node = null;
		if (o == null) {
			// If the object is null, we don't care what tag to use. We just
			// create an empty param. While reading, a null will be retrieved
			node = new XMLNode(DOMTags.PARAM_TAG);
		} else if (reflectionProvider.isAssignableFrom(EAdParam.class, clazz)) {
			node = paramWriter.write(o);
		} else if (reflectionProvider.isAssignableFrom(AssetDescriptor.class,
				clazz)) {
			objectWriter.setAsset(true);
			node = objectWriter.write((AssetDescriptor) o);
		} else if (reflectionProvider.isAssignableFrom(EAdElement.class, clazz)) {
			objectWriter.setAsset(false);
			node = objectWriter.write((EAdElement) o);
		} else if (reflectionProvider.isAssignableFrom(EAdList.class, clazz)) {
			node = listWriter.write((EAdList) o);
		} else if (reflectionProvider.isAssignableFrom(EAdMap.class, clazz)) {
			node = mapWriter.write((EAdMap) o);
		} else {
			node = paramWriter.write(o);
		}

		if (!addedToRoot) {
			root.append(node);
			addedToRoot = true;
		}
		listener.load(node, o);

		if (stepsQueue.isEmpty()) {
			logger.debug("Resolving references...");
			objectWriter.resolveReferences();
		}
		// Debug output
		if (stepsQueue.isEmpty() && logger.isDebugEnabled()) {
			logger.debug("{} elements simplified.", objectWriter
					.getSimplifications());
			int total = 0;
			for (Entry<Object, Map<EAdVarDef<?>, EAdField<?>>> e : objectWriter
					.getFields().entrySet()) {
				for (Entry<EAdVarDef<?>, EAdField<?>> v : e.getValue()
						.entrySet()) {
				}
			}
			for (Entry<Class<?>, Integer> e : objectWriter
					.getClassProfilerAssets().entrySet()) {
				List l = objectWriter.getSimplifier().getIdentified().get(
						e.getKey());
				int value = l == null ? 0 : l.size();
				logger.debug("{}:{} | {}", new Object[] { e.getKey(),
						e.getValue(), value });
				total += e.getValue();
			}
			logger.debug("Total assets: {}", total);
			total = 0;
			for (Entry<Class<?>, Integer> e : objectWriter
					.getClassProfilerElements().entrySet()) {
				List l = objectWriter.getSimplifier().getIdentified().get(
						e.getKey());
				int value = l == null ? 0 : l.size();
				logger.debug("{}:{} | {}", new Object[] { e.getKey(),
						e.getValue(), value });
				total += e.getValue();
			}
			logger.debug("Total eadelements: {}", total);
			logger.debug("Total lists: " + listWriter.getTotal());
			logger.debug("Total maps: " + mapWriter.getTotal());
			logger.debug("Total params: " + paramWriter.getTotal());
		}
		return stepsQueue.isEmpty();
	}

	public void clear() {
		// Create a new document
		this.root = new XMLNode(DOMTags.ROOT_TAG);
		classes = new XMLNode(DOMTags.CLASSES_TAG);
		fields = new XMLNode(DOMTags.FIELDS_TAG);
		params = new XMLNode(DOMTags.PARAMS_ABB_TAG);
		root.append(classes);
		root.append(fields);
		root.append(params);
		addedToRoot = false;
		objectWriter.clear();
		paramWriter.clear();
		listWriter.clear();
		mapWriter.clear();
		stepsQueue.clear();
		classTranslations.clear();
		fieldsTranslations.clear();
		paramsTranslations.clear();
	}

	public String translateClass(Class<?> clazz) {
		String value = classTranslations.get(clazz);
		if (value == null) {
			value = Integer.toHexString(classTranslations.size());
			classTranslations.put(clazz, value);
			XMLNode entry = new XMLNode(DOMTags.ENTRY_TAG);
			entry.setAttribute(DOMTags.KEY_AT, value);
			entry.setAttribute(DOMTags.VALUE_AT, clazz.getName());
			classes.append(entry);
		}
		return value;
	}

	public String translateField(String field) {
		String value = fieldsTranslations.get(field);
		if (value == null) {
			value = Integer.toHexString(fieldsTranslations.size());
			fieldsTranslations.put(field, value);
			XMLNode entry = new XMLNode(DOMTags.ENTRY_TAG);
			entry.setAttribute(DOMTags.KEY_AT, value);
			entry.setAttribute(DOMTags.VALUE_AT, field);
			fields.append(entry);
		}
		return value;
	}

	public String translateParam(String param) {
		String value = paramsTranslations.get(param);
		if (value == null) {
			value = Integer.toHexString(paramsTranslations.size());
			paramsTranslations.put(param, value);
			XMLNode entry = new XMLNode(DOMTags.ENTRY_TAG);
			entry.setAttribute(DOMTags.KEY_AT, value);
			entry.setAttribute(DOMTags.VALUE_AT, param);
			params.append(entry);
		}
		return value;
	}

	public XMLNode getRoot() {
		return root;
	}

	public static class WriterStep {
		private VisitorListener listener;
		private Object object;
		private Object parent;

		public WriterStep(Object object, Object parent, VisitorListener listener) {
			super();
			this.listener = listener;
			this.object = object;
			this.parent = parent;
		}

		public VisitorListener getListener() {
			return listener;
		}

		public Object getObject() {
			return object;
		}

		public Object getParent() {
			return parent;
		}

	}

	public static interface VisitorListener {

		void load(XMLNode node, Object object);

	}

	public void setEnableSimplifcations(boolean enable) {
		this.simplificationsEnabled = enable;
	}

	public boolean isSimplifcationsEnabled() {
		return simplificationsEnabled;
	}

}
