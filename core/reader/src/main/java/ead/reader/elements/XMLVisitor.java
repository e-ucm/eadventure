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

package ead.reader.elements;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.elements.extra.EAdMap;
import ead.reader.adventure.DOMTags;
import ead.reader.elements.readers.ListReader;
import ead.reader.elements.readers.MapReader;
import ead.reader.elements.readers.ObjectReader;
import ead.reader.elements.readers.ParamReader;
import ead.reader.elements.translators.ClassTranslator;
import ead.tools.xml.XMLNode;

public class XMLVisitor {

	private static final Logger logger = LoggerFactory.getLogger("XMLVisitor");

	private List<VisitorStep> stepsQueue;

	private List<MapKeyValue> mapKeysValues;

	private List<ClassTranslator> translators;

	private ElementsFactory elementsFactory;

	private ParamReader paramReader;

	private ListReader listReader;

	private MapReader mapReader;

	private ObjectReader objectReader;

	public XMLVisitor() {
		stepsQueue = new ArrayList<VisitorStep>();
		mapKeysValues = new ArrayList<MapKeyValue>();
		translators = new ArrayList<ClassTranslator>();
		elementsFactory = new ElementsFactory();
		paramReader = new ParamReader(elementsFactory, this);
		listReader = new ListReader(elementsFactory, this);
		mapReader = new MapReader(elementsFactory, this);
		objectReader = new ObjectReader(elementsFactory, this);
	}

	public void addTranslator(ClassTranslator t) {
		this.translators.add(t);
	}

	/**
	 * Adds an element to load to the stack
	 * 
	 * @param node
	 * @param listener
	 */
	public void loadElement(XMLNode node, VisitorListener listener) {
		stepsQueue.add(new VisitorStep(node, listener));
	}

	/**
	 * Takes a step in the reading process. Returns true if there is anything
	 * else to read
	 * 
	 * @return
	 */
	public boolean step() {
		if (stepsQueue.isEmpty()) {
			return true;
		}

		VisitorStep step = stepsQueue.remove(0);
		XMLNode node = step.getNode();
		VisitorListener listener = step.getListener();
		Object result = null;
		boolean error = false;
		if (node.getNodeName().equals(DOMTags.PARAM_AT)) {
			result = paramReader.read(node);
		} else if (node.getNodeName().equals(DOMTags.LIST_TAG)) {
			result = listReader.read(node);
		} else if (node.getNodeName().equals(DOMTags.ASSET_AT)) {
			objectReader.setAsset(true);
			result = objectReader.read(node);
		} else if (node.getNodeName().equals(DOMTags.ELEMENT_AT)) {
			objectReader.setAsset(false);
			result = objectReader.read(node);
		} else if (node.getNodeName().equals(DOMTags.MAP_TAG)) {
			result = mapReader.read(node);
		} else {
			logger.warn(" could not read node {} with name {}", node
					.getNodeName());
			error = true;
		}

		// Sometimes we can't generate elements reference because the original
		// object didn't appear yet. When that happens, we obtain a null result.
		// We send the step to the end of the queue, for later
		if (!listener.loaded(node, result) && !error) {
			stepsQueue.add(step);
		}

		if (stepsQueue.isEmpty()) {
			// We assign the pending keys and values to their maps
			for (MapKeyValue mkv : mapKeysValues) {
				mkv.execute();
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public void addMapKeyValue(EAdMap map, Object key, Object value,
			boolean keyReference, boolean valueReference) {
		mapKeysValues.add(new MapKeyValue(map, key, value, keyReference,
				valueReference));
	}

	public static interface VisitorListener {

		/**
		 * Returns if the object was correctly processed
		 * 
		 * @param node
		 * @param object
		 * @return
		 */
		boolean loaded(XMLNode node, Object object);
	}

	public static class VisitorStep {
		private XMLNode node;
		private VisitorListener listener;

		public VisitorStep(XMLNode node, VisitorListener listener) {
			this.node = node;
			this.listener = listener;
		}

		public XMLNode getNode() {
			return node;
		}

		public VisitorListener getListener() {
			return listener;
		}
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	public class MapKeyValue {
		private EAdMap map;
		private Object key;
		private Object value;
		private boolean keyReference;
		private boolean valueReference;

		public MapKeyValue(EAdMap map, Object key, Object value,
				boolean keyReference, boolean valueReference) {
			super();
			this.map = map;
			this.key = key;
			this.value = value;
			this.keyReference = keyReference;
			this.valueReference = valueReference;
		}

		void execute() {
			Object key = keyReference ? elementsFactory
					.getReferencedElement(this.key) : this.key;
			Object value = valueReference ? elementsFactory
					.getReferencedElement(this.value) : this.value;

			map.put(key, value);
		}
	}

	public String translate(String clazz) {
		for (ClassTranslator t : translators) {
			String translation = t.translate(clazz);
			if (translation != null) {
				return translation;
			}
		}
		return clazz;
	}

}
