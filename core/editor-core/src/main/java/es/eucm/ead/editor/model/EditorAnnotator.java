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

package es.eucm.ead.editor.model;

import com.google.inject.Singleton;
import es.eucm.ead.importer.annotation.ImportAnnotator;
import es.eucm.ead.model.elements.BasicElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * An annotator that can be used to build EditorNodes.
 * @author mfreire
 */
@Singleton
public class EditorAnnotator implements ImportAnnotator {
	private static final Logger logger = LoggerFactory
			.getLogger(EditorAnnotator.class);

	private ArrayList<BasicElement> stack = new ArrayList<BasicElement>();
	private HashMap<BasicElement, ArrayList<Annotation>> annotations = new HashMap<BasicElement, ArrayList<Annotation>>();
	private HashMap<BasicElement, HashSet<BasicElement>> children = new HashMap<BasicElement, HashSet<BasicElement>>();

	public EditorAnnotator() {
		logger.warn("warning level enabled");
		logger.info("info level enabled");
		logger.debug("debug level enabled");
	}

	public class Annotation {
		private ArrayList<BasicElement> context = new ArrayList<BasicElement>();
		private ImportAnnotator.Type type;
		private Object key;
		private Object value;

		private Annotation(ImportAnnotator.Type type, Object key, Object value) {
			context.addAll(stack);
			this.type = type;
			this.key = key;
			this.value = value;
		}

		public ArrayList<BasicElement> getContext() {
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

	public static class PlaceHolder extends BasicElement {
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
		HashMap<BasicElement, ArrayList<Annotation>> annotationBackup = new HashMap<BasicElement, ArrayList<Annotation>>();
		annotationBackup.putAll(annotations);
		annotations = annotationBackup;
		HashMap<BasicElement, HashSet<BasicElement>> childrenBackup = new HashMap<BasicElement, HashSet<BasicElement>>();
		childrenBackup.putAll(children);
		for (Map.Entry<BasicElement, HashSet<BasicElement>> p : children
				.entrySet()) {
			HashSet<BasicElement> set = new HashSet<BasicElement>();
			set.addAll(p.getValue());
			p.setValue(set);
		}
	}

	private static final ArrayList<Annotation> emptyAnnotations = new ArrayList<Annotation>();

	public ArrayList<Annotation> get(BasicElement element) {
		ArrayList<Annotation> al = annotations.get(element);
		return (al != null) ? al : emptyAnnotations;
	}

	/**
	 * Returns all elements with 'element' as their direct
	 * context-ancestor
	 * @param element
	 * @return
	 */
	private static final HashSet<BasicElement> emptyChildren = new HashSet<BasicElement>();

	public HashSet<BasicElement> getChildren(BasicElement element) {
		HashSet<BasicElement> rc = children.get(element);
		return (rc != null) ? rc : emptyChildren;
	}

	/**
	 * Returns all values of annotations on an element with Key.Role
	 * starting with typePrefix.
	 * @param element
	 * @param typePrefix
	 * @return
	 */
	public HashSet<String> get(BasicElement element, String typePrefix) {
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
	public void annotate(BasicElement element, Type key, Object... values) {
		if (element == null) {
			element = new PlaceHolder(values[0].toString());
		}

		if (!stack.isEmpty()) {
			BasicElement parent = stack.get(stack.size() - 1);
			HashSet<BasicElement> cs = children.get(parent);
			if (cs == null) {
				cs = new HashSet<BasicElement>();
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
