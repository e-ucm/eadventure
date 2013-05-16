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

package ead.reader.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.assets.AssetDescriptor;
import ead.common.model.elements.BasicElement;
import ead.common.model.params.variables.VarDef;
import ead.reader.DOMTags;
import ead.reader.model.readers.ListReader;
import ead.reader.model.readers.MapReader;
import ead.reader.model.readers.ObjectReader;
import ead.reader.model.readers.ParamReader;
import ead.reader.model.translators.StringTranslator;
import ead.tools.reflection.ReflectionProvider;
import ead.tools.xml.XMLNode;

public class XMLVisitor {

	private static final Logger logger = LoggerFactory.getLogger("XMLVisitor");

	private static final int MAX_LOOPS_WITH_SAME_SIZE = 1000;

	private List<VisitorStep> stepsQueue;

	private List<MapKeyValue> mapKeysValues;

	private List<StringTranslator> clazzTranslators;

	private List<StringTranslator> fieldsTranslators;

	private List<StringTranslator> paramsTranslators;

	private ObjectsFactory elementsFactory;

	private ParamReader paramReader;

	private ListReader listReader;

	private MapReader mapReader;

	private ObjectReader objectReader;

	private int lastSize = 0;

	private int loopsWithSameSize = 0;

	public XMLVisitor(ReflectionProvider reflectionProvider) {
		stepsQueue = new ArrayList<VisitorStep>();
		mapKeysValues = new ArrayList<MapKeyValue>();
		clazzTranslators = new ArrayList<StringTranslator>();
		fieldsTranslators = new ArrayList<StringTranslator>();
		paramsTranslators = new ArrayList<StringTranslator>();
		elementsFactory = new ObjectsFactory(reflectionProvider, this);
		paramReader = new ParamReader(elementsFactory, this);
		listReader = new ListReader(elementsFactory, this);
		mapReader = new MapReader(elementsFactory, this);
		objectReader = new ObjectReader(elementsFactory, this);
	}

	public void addClazzTranslator(StringTranslator t) {
		this.clazzTranslators.add(t);
	}

	public void addFieldsTranslator(StringTranslator t) {
		this.fieldsTranslators.add(t);
	}

	public void addParamsTranslator(StringTranslator t) {
		this.paramsTranslators.add(t);
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

		// First look for null in the node
		// <p/> or <e/> or <a/> symbolizes null element
		if (!node.hasChildNodes() && node.getAttributesLength() == 0) {
			listener.loaded(node, null, true);
		} else {
			Object result = null;
			boolean error = false;
			if (node.getNodeName().equals(DOMTags.PARAM_TAG)) {
				result = paramReader.read(node);
			} else if (node.getNodeName().equals(DOMTags.LIST_TAG)) {
				result = listReader.read(node);
			} else if (node.getNodeName().equals(DOMTags.ASSET_TAG)) {
				objectReader.setAsset(true);
				result = objectReader.read(node);
			} else if (node.getNodeName().equals(DOMTags.ELEMENT_TAG)) {
				objectReader.setAsset(false);
				result = objectReader.read(node);
			} else if (node.getNodeName().equals(DOMTags.MAP_TAG)) {
				result = mapReader.read(node);
			} else {
				logger.warn(" could not read node {} with name {}", node
						.getNodeName());
				error = true;
			}

			// Sometimes we can't generate elements reference because the
			// original
			// object didn't appear yet. When that happens, we obtain a null
			// result.
			// We send the step to the end of the queue, for later
			if (!listener.loaded(node, result, false) && !error) {
				stepsQueue.add(step);
			}
		}

		if (stepsQueue.isEmpty()) {
			// We assign the pending keys and values to their maps
			for (MapKeyValue mkv : mapKeysValues) {
				mkv.execute();
			}
			return true;
		}

		// We check queue size, looking for elements impossible to read
		// (usually, references to ids not defined)
		int newSize = stepsQueue.size();
		if (newSize == lastSize) {
			loopsWithSameSize++;
		} else {
			lastSize = newSize;
			loopsWithSameSize = 0;
		}

		// If we've been to much loops with the same size, we end the reading
		// and report the remaining nodes
		if (loopsWithSameSize >= MAX_LOOPS_WITH_SAME_SIZE) {
			for (VisitorStep s : stepsQueue) {
				XMLNode n = s.getNode();
				// If it's a reference, we create a BasicElement with the id.
				if (n.getNodeName().equals("e") && n.getAttributesLength() < 2) {
					String id = n.getNodeText();
					if (id != null) {
						s.getListener().loaded(n, new BasicElement(id), false);
						logger
								.warn(
										"{} is not present in the model. A BasicElement reference was generated",
										n.getNodeText());
					}
				} else {
					logger.warn("{} node was impossilbe to read.", n
							.getNodeText());
				}

			}
			return true;
		}
		return false;
	}

	public static interface VisitorListener {

		/**
		 * Returns if the object was correctly processed
		 * 
		 * @param node
		 * @param object
		 * @param isNullInOrigin
		 *            Says if the object is null because data.xml says so, not
		 *            because it is not still loaded
		 * @return
		 */
		boolean loaded(XMLNode node, Object object, boolean isNullInOrigin);
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
		private VarDef var;
		private String value;

		public MapKeyValue(VarDef var, String value) {
			this.var = var;
			this.value = value;
		}

		void execute() {
			Object o = elementsFactory.getObject(value, var.getType());
			if (o == null) {
				logger.warn("{} not found while setting as initial value",
						value);
			}
			var.setInitialValue(o);
		}
	}

	private String translate(String string, List<StringTranslator> translators) {
		for (StringTranslator t : translators) {
			String translation = t.translate(string);
			if (translation != null) {
				return translation;
			}
		}
		return string;
	}

	public String translateClazz(String clazz) {
		return translate(clazz, clazzTranslators);
	}

	public String translateField(String field) {
		return translate(field, fieldsTranslators);
	}

	public String translateParam(String param) {
		return translate(param, paramsTranslators);
	}

	public void init() {
		clazzTranslators.clear();
		elementsFactory.clear();
		mapKeysValues.clear();
		stepsQueue.clear();
		fieldsTranslators.clear();
		paramsTranslators.clear();
		objectReader.clear();
	}

	public void addLoadInitalValue(VarDef<?> v, String value) {
		mapKeysValues.add(new MapKeyValue(v, value));

	}

	public Collection<AssetDescriptor> getAssets() {
		return this.elementsFactory.getAssets();
	}

}
