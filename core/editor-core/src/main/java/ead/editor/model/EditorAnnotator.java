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

package ead.editor.model;

import com.google.inject.Singleton;

import es.eucm.ead.model.elements.EAdElement;
import ead.importer.annotation.ImportAnnotator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An annotator that can be used to build EditorNodes.
 * @author mfreire
 */
@Singleton
public class EditorAnnotator implements ImportAnnotator {
	private static final Logger logger = LoggerFactory
			.getLogger("EditorAnnotator");

	private ArrayList<EAdElement> stack = new ArrayList<EAdElement>();
	private HashMap<EAdElement, ArrayList<Annotation>> annotations = new HashMap<EAdElement, ArrayList<Annotation>>();
	private HashMap<EAdElement, HashSet<EAdElement>> children = new HashMap<EAdElement, HashSet<EAdElement>>();

	public EditorAnnotator() {
		logger.warn("warning level enabled");
		logger.info("info level enabled");
		logger.debug("debug level enabled");
	}

	public class Annotation {
		private ArrayList<EAdElement> context = new ArrayList<EAdElement>();
		private ImportAnnotator.Type type;
		private Object key;
		private Object value;

		private Annotation(ImportAnnotator.Type type, Object key, Object value) {
			context.addAll(stack);
			this.type = type;
			this.key = key;
			this.value = value;
		}

		public ArrayList<EAdElement> getContext() {
			return context;
		}

		public ImportAnnotator.Type getType() {
			return type;
		}

		public Object getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}
	}

	public static class PlaceHolder implements EAdElement {
		private String id;

		private PlaceHolder(String id) {
			this.id = id;
		}

		@Override
		public boolean equals(Object obj) {
			return (obj != null && ((PlaceHolder) obj).id.equals(id));
		}

		@Override
		public int hashCode() {
			return this.id.hashCode();
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public void setId(String id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "<" + id + ">";
		}
	}

	public void reset() {
		stack.clear();
		annotations.clear();
	}

	/**
	 * Ids may be changed by the editor; this makes sure that the 'get'
	 * operations works with updated ids.
	 */
	public void rebuild() {
		HashMap<EAdElement, ArrayList<Annotation>> annotationBackup = new HashMap<EAdElement, ArrayList<Annotation>>();
		annotationBackup.putAll(annotations);
		annotations = annotationBackup;
		HashMap<EAdElement, HashSet<EAdElement>> childrenBackup = new HashMap<EAdElement, HashSet<EAdElement>>();
		childrenBackup.putAll(children);
		for (Map.Entry<EAdElement, HashSet<EAdElement>> p : children.entrySet()) {
			HashSet<EAdElement> set = new HashSet<EAdElement>();
			set.addAll(p.getValue());
			p.setValue(set);
		}
	}

	private static final ArrayList<Annotation> emptyAnnotations = new ArrayList<Annotation>();

	public ArrayList<Annotation> get(EAdElement element) {
		ArrayList<Annotation> al = annotations.get(element);
		return (al != null) ? al : emptyAnnotations;
	}

	/**
	 * Returns all elements with 'element' as their direct
	 * context-ancestor
	 * @param element
	 * @return
	 */
	private static final HashSet<EAdElement> emptyChildren = new HashSet<EAdElement>();

	public HashSet<EAdElement> getChildren(EAdElement element) {
		HashSet<EAdElement> rc = children.get(element);
		return (rc != null) ? rc : emptyChildren;
	}

	/**
	 * Returns all values of annotations on an element with Key.Role
	 * starting with typePrefix.
	 * @param element
	 * @param typePrefix
	 * @return
	 */
	public HashSet<String> get(EAdElement element, String typePrefix) {
		HashSet<String> results = new HashSet<String>();
		for (Annotation a : get(element)) {
			String v = a.getValue().toString();
			if (a.getType().equals(Type.Entry)) {
				logger.debug("evaluating {}::{} vs {}", new Object[] {
						a.getKey(), a.getValue(), typePrefix });
				if (a.getKey().equals(Key.Role) && v.startsWith(typePrefix)) {
					results.add(v);
				}
			}
		}
		return results;
	}

	@Override
	public void annotate(Type key, Object... values) {
		annotate(stack.get(stack.size() - 1), key, values);
	}

	@Override
	public void annotate(EAdElement element, Type key, Object... values) {
		if (element == null) {
			element = new PlaceHolder(values[0].toString());
		}

		if (!stack.isEmpty()) {
			EAdElement parent = stack.get(stack.size() - 1);
			HashSet<EAdElement> cs = children.get(parent);
			if (cs == null) {
				cs = new HashSet<EAdElement>();
				children.put(parent, cs);
			}
			cs.add(element);
		}

		switch (key) {
		case Open: {
			logger.debug("Entering {}", element);
			stack.add(element);
			break;
		}
		case Close: {
			logger.debug("Exiting {}", element);
			int i = stack.lastIndexOf(element);
			if (i < 0) {
				logger.error(
						"Exiting {} -- no such element currently open in {}",
						new Object[] { element, stack });
			} else if (i != stack.size() - 1) {
				logger.error("Exiting {} -- element is not last in {}",
						new Object[] { element, stack });
				stack.remove(i);
			} else {
				stack.remove(i);
			}
			break;
		}
		case Comment: {
			if (!annotations.containsKey(element)) {
				annotations.put(element, new ArrayList<Annotation>());
			}
			for (Object o : values) {
				annotations.get(element).add(
						new Annotation(key, Type.Comment.name(), o.toString()));
			}
			logger.debug("Commenting {}({}) with {}: {} --> {}", new Object[] {
					element, stack, key, Type.Comment.name(), values[0] });
			break;
		}
		default: {
			if (!annotations.containsKey(element)) {
				annotations.put(element, new ArrayList<Annotation>());
			}
			annotations.get(element).add(
					new Annotation(key, values[0], values[1]));
			logger.debug("Annotating {}({}) with {}: {} --> {}", new Object[] {
					element, stack, key, values[0], values[1] });
		}
		}
	}
}
