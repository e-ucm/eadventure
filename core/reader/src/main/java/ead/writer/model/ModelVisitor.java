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

package ead.writer.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.assets.AssetDescriptor;
import ead.common.model.elements.EAdElement;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.extra.EAdMap;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.params.EAdParam;
import ead.common.model.params.variables.EAdVarDef;
import ead.reader.DOMTags;
import ead.tools.reflection.ReflectionProvider;
import ead.tools.xml.XMLDocument;
import ead.tools.xml.XMLNode;
import ead.tools.xml.XMLParser;
import ead.writer.model.writers.ListWriter;
import ead.writer.model.writers.MapWriter;
import ead.writer.model.writers.ObjectWriter;
import ead.writer.model.writers.ParamWriter;

public class ModelVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger("ModelVisitor");

	private ReflectionProvider reflectionProvider;

	private XMLParser xmlParser;

	private boolean addedToRoot;

	private XMLNode classes;

	private ParamWriter paramWriter;

	private ListWriter listWriter;

	private MapWriter mapWriter;

	private ObjectWriter objectWriter;

	private XMLDocument currentDocument;

	private List<WriterStep> stepsQueue;

	private Map<Class<?>, String> translations;

	private XMLNode root;

	public ModelVisitor(ReflectionProvider reflectionProvider, XMLParser parser) {
		this.reflectionProvider = reflectionProvider;
		this.reflectionProvider = reflectionProvider;
		this.xmlParser = parser;

		this.stepsQueue = new ArrayList<WriterStep>();
		this.translations = new LinkedHashMap<Class<?>, String>();
		// writers
		paramWriter = new ParamWriter(this);
		listWriter = new ListWriter(this);
		mapWriter = new MapWriter(this);
		objectWriter = new ObjectWriter(this);
	}

	public void writeElement(Object object, VisitorListener listener) {
		stepsQueue.add(new WriterStep(object, listener));
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
			node = newNode(DOMTags.PARAM_TAG);
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

		if (node != null) {
			listener.load(node, o);
			if (!addedToRoot) {
				root.append(node);
				addedToRoot = true;
			}
		}

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
				logger.debug("+{}", e.getKey());
				for (Entry<EAdVarDef<?>, EAdField<?>> v : e.getValue()
						.entrySet()) {
					logger.debug("     |-- {}", v.getKey().getName());
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
		this.currentDocument = xmlParser.createDocument();
		root = currentDocument.newNode(DOMTags.ROOT_TAG);
		currentDocument.appendChild(root);
		classes = currentDocument.newNode(DOMTags.CLASSES_TAG);
		root.append(classes);
		addedToRoot = false;
		objectWriter.clear();
		translations.clear();
	}

	public String translateClass(Class<?> clazz) {
		String value = translations.get(clazz);
		if (value == null) {
			value = Integer.toHexString(translations.size());
			translations.put(clazz, value);
			XMLNode entry = currentDocument.newNode(DOMTags.ENTRY_TAG);
			entry.setAttribute(DOMTags.KEY_AT, value);
			entry.setAttribute(DOMTags.VALUE_AT, clazz.getName());
			classes.append(entry);
		}
		return value;
	}

	public XMLNode newNode(String tag) {
		return currentDocument.newNode(tag);
	}

	public XMLDocument getDocument() {
		return currentDocument;
	}

	public static class WriterStep {
		private VisitorListener listener;
		private Object object;

		public WriterStep(Object object, VisitorListener listener) {
			super();
			this.listener = listener;
			this.object = object;
		}

		public VisitorListener getListener() {
			return listener;
		}

		public Object getObject() {
			return object;
		}

	}

	public static interface VisitorListener {

		void load(XMLNode node, Object object);

	}

}
