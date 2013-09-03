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

package es.eucm.ead.reader2.model;

import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.reader.DOMTags;
import es.eucm.ead.reader.model.translators.StringTranslator;
import es.eucm.ead.reader2.model.readers.ListReader;
import es.eucm.ead.reader2.model.readers.MapReader;
import es.eucm.ead.reader2.model.readers.ObjectReader;
import es.eucm.ead.reader2.model.readers.ParamReader;
import es.eucm.ead.tools.reflection.ReflectionProvider;
import es.eucm.ead.tools.xml.XMLNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ReaderVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger(ReaderVisitor.class);

	private static final int MAX_LOOPS_WITH_SAME_SIZE = 1000;

	private ObjectsFactory objectsFactory;

	private ParamReader paramReader;

	private ListReader listReader;

	private MapReader mapReader;

	private ObjectReader objectReader;

	private List<VisitorStep> stepsQueue;

	private List<FinalStep> finalStepsQueue;

	private List<StringTranslator> clazzTranslators;

	private List<StringTranslator> fieldsTranslators;

	private List<StringTranslator> paramsTranslators;

	private int lastSize = 0;

	private int loopsWithSameSize = 0;

	public ReaderVisitor(ReflectionProvider reflectionProvider) {
		stepsQueue = new ArrayList<VisitorStep>();
		finalStepsQueue = new ArrayList<FinalStep>();
		clazzTranslators = new ArrayList<StringTranslator>();
		fieldsTranslators = new ArrayList<StringTranslator>();
		paramsTranslators = new ArrayList<StringTranslator>();
		objectsFactory = new ObjectsFactory(reflectionProvider, this);
		paramReader = new ParamReader(objectsFactory, this);
		listReader = new ListReader(objectsFactory, this);
		mapReader = new MapReader(objectsFactory, this);
		objectReader = new ObjectReader(objectsFactory, this);
	}

	public void addFinalStep(FinalStep step) {
		this.finalStepsQueue.add(step);
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
		boolean done = false;

		VisitorStep step = stepsQueue.remove(0);
		XMLNode node = step.getNode();
		VisitorListener listener = step.getListener();

		if (node == null) {
			// First look for null in the node
			// <p/> or <e/> or <a/> symbolizes null element
			logger.warn("null element read; ignoring");
		} else if (!node.hasChildNodes()
				&& node.getAttributesLength() == 0
				&& ("".equals(node.getNodeText()) || node.getNodeText() == null)) {
			listener.loaded(node, null, true);
		} else {
			Object result = null;
			boolean error = false;
			if (node.getNodeName().equals(DOMTags.PARAM_TAG)) {
				result = paramReader.read(node);
			} else if (node.getNodeName().equals(DOMTags.LIST_TAG)) {
				result = listReader.read(node);
			} else if (node.getNodeName().equals(DOMTags.ELEMENT_TAG)) {
				result = objectReader.read(node);
			} else if (node.getNodeName().equals(DOMTags.MAP_TAG)) {
				result = mapReader.read(node);
			} else {
				logger.warn(" could not read node {} with name {}", node, node
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

		// We check queue size, looking for elements impossible to read
		// (usually, references to ids not defined)
		int newSize = stepsQueue.size();
		if (newSize == lastSize) {
			loopsWithSameSize++;
		} else {
			lastSize = newSize;
			loopsWithSameSize = 0;
		}

		// If we've been too much loops with the same size, we end the reading
		// and report the remaining nodes
		if (loopsWithSameSize >= MAX_LOOPS_WITH_SAME_SIZE) {
			ArrayList<String> l = new ArrayList<String>();
			for (VisitorStep s : stepsQueue) {
				XMLNode n = s.getNode();
				// If it's a reference, we create a BasicElement with the id.
				if (n.getNodeName().equals("e") && n.getAttributesLength() < 2) {
					String id = n.getNodeText();
					if (id != null) {
						s.getListener().loaded(n, new BasicElement(id), false);
						if (!l.contains(id)) {
							l.add(id);
						}
					}
				} else {
					logger.warn("{} node was impossilbe to read.", n
							.getNodeText());
				}

			}
			logger.debug("{} references created: {}", l.size(), l.toString());
			done = true;
		}
		done = done || stepsQueue.isEmpty();
		if (done) {
			logger.debug("Executing {} final steps", finalStepsQueue.size());
			for (FinalStep f : finalStepsQueue) {
				f.execute();
			}
		}
		return done;
	}

	public List<VisitorStep> getRefForId(String id) {
		ArrayList<VisitorStep> list = new ArrayList<VisitorStep>();
		for (VisitorStep s : stepsQueue) {
			XMLNode n = s.getNode();
			if (n.getNodeName().equals("e") && n.getAttributesLength() < 2) {
				String i = n.getNodeText();
				if (i.equals(id)) {
					list.add(s);
				}
			}
		}
		return list;
	}

	public List<VisitorStep> getNodeWithId(String id) {
		ArrayList<VisitorStep> list = new ArrayList<VisitorStep>();
		for (VisitorStep s : stepsQueue) {
			XMLNode n = s.getNode();
			if (id.equals(n.getAttributeValue("i"))) {
				list.add(s);
			}
		}
		return list;
	}

	public void finish() {
		while (!step())
			;
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

	private String translate(String string, List<StringTranslator> translators) {
		for (StringTranslator t : translators) {
			String translation = t.translate(string);
			if (translation != null) {
				return translation;
			}
		}
		return string;
	}

	public String translateClass(String clazz) {
		return translate(clazz, clazzTranslators);
	}

	public String translateField(String field) {
		return translate(field, fieldsTranslators);
	}

	public String translateParam(String param) {
		return translate(param, paramsTranslators);
	}

	public void init() {
		objectsFactory.clear();
		stepsQueue.clear();
	}

	public static interface FinalStep {
		void execute();
	}

}
