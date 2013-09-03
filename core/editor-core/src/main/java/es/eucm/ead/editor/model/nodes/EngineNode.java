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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.model.nodes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import es.eucm.ead.editor.model.EditorModel;
import es.eucm.ead.editor.model.visitor.ModelVisitorDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.ead.model.assets.AssetDescriptor;
import es.eucm.ead.model.elements.EAdElement;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.extra.EAdMap;
import es.eucm.ead.model.params.EAdParam;
import es.eucm.ead.model.params.variables.VarDef;

/**
 * An engine-model node. Used as a base for the dependency-tracking mechanism
 * for the editor model.
 * @author mfreire
 */
public class EngineNode<T> extends DependencyNode<T> {

	private static Logger logger = LoggerFactory.getLogger(EngineNode.class);

	public static final int maxDependencies = 10;

	public EngineNode(int id, T content) {
		super(id, content);
	}

	/**
	 * Generates a one-line description with as much information as possible.
	 * @return a human-readable description of this node
	 */
	@Override
	public String getTextualDescription(EditorModel m) {
		StringBuilder sb = new StringBuilder();
		appendDescription(m, content, sb, 0, 3);
		return sb.toString();
	}

	public String formatIds(List<DependencyNode> list) {
		StringBuilder sb = new StringBuilder(" ");
		int remaining = maxDependencies;
		for (DependencyNode n : list) {
			remaining--;
			sb.append("(").append(n.getId()).append(") ");
			if (remaining == 0) {
				sb.append("...");
				break;
			}
		}
		return sb.toString();
	}

	private void appendDependencies(DependencyNode n, EditorModel m,
			StringBuilder sb) {
		List<DependencyNode> incoming = m.incomingDependencies(this);
		sb.append("Used from ").append(formatIds(incoming)).append("\n");
	}

	/**
	 * Returns a descriptive string for a given object.
	 * @param o object to drive into.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void appendDescription(EditorModel m, Object o, StringBuilder sb,
			int depth, int maxDepth) {
		if (maxDepth == depth) {
			return;
		}

		String indent = new String(new char[depth * 2]).replace('\0', ' ');

		if (o == null) {
			sb.append("(null)");
			return;
		}

		String id = "" + m.getEditorId(o);
		String cname = o.getClass().getSimpleName();
		if (o instanceof EAdElement) {
			if (o instanceof VarDef) {
				VarDef<?> v = ((VarDef) o);
				sb.append(indent + v.getType().getSimpleName() + " "
						+ v.getName() + " = " + v.getInitialValue() + "\n");
				if (depth != maxDepth) {
					appendDependencies(this, m, sb);
				}
			} else {
				sb.append(indent + cname + " (" + id + ")" + "\n");
				if (depth != maxDepth) {
					appendDependencies(this, m, sb);
				}
				appendParams(m, o, sb, depth, maxDepth);
			}
		} else if (o instanceof EAdList) {
			EAdList target = (EAdList) o;
			sb.append(indent + cname + " (" + id + ")" + "\n");
			if (target.size() == 0) {
				sb.append(indent + "  (empty)\n");
			} else if (depth == maxDepth - 1) {
				sb.append(indent + "  (" + target.size()
						+ " elements inside)\n");
			} else {
				for (int i = 0; i < target.size(); i++) {
					// visit all children-values of this list
					Object inner = target.get(i);
					if (inner != null) {
						appendDescription(m, inner, sb, depth + 1, maxDepth);
					}
				}
			}
		} else if (o instanceof EAdMap) {
			EAdMap<?, ?> target = (EAdMap<?, ?>) o;
			sb.append(indent + cname + " (" + id + ")" + "\n");
			int i = 0;
			if (target.size() == 0) {
				sb.append(indent + "  (empty)\n");
			} else if (depth == maxDepth - 1) {
				sb.append(indent + "  (" + target.size()
						+ " elements inside)\n");
			} else {
				for (Map.Entry<?, ?> e : target.entrySet()) {
					if (e.getKey() != null) {
						appendDescription(m, e.getKey(), sb, depth + 1,
								maxDepth);
					}
					sb.append(indent + "  -m->\n");
					if (e.getValue() != null) {
						appendDescription(m, e.getValue(), sb, depth + 1,
								maxDepth);
					}
					i++;
				}
			}
		} else if (o instanceof EAdParam) {
			sb.append(indent + ((EAdParam) o).toStringData());
		} else if (o instanceof AssetDescriptor) {
			sb.append(indent + "asset" + " (" + id + "): "
					+ ((AssetDescriptor) o).getId() + "\n");
			appendParams(m, o, sb, depth, maxDepth);
			if (depth != maxDepth) {
				appendDependencies(this, m, sb);
			}
		} else if (o instanceof Class) {
			sb.append(indent + ((Class<?>) o).getName() + "\n");
		} else {
			sb.append(indent + " (" + cname + "~" + o.toString() + ")\n");
		}
	}

	/**
	 * Iterates through params, providing a glimpse of their contents.
	 * @param m model
	 * @param o object from which to extract params, if any
	 * @param sb output
	 * @param depth current depth
	 * @param maxDepth max depth to reach
	 */
	private void appendParams(EditorModel m, Object o, StringBuilder sb,
			int depth, int maxDepth) {
		if (maxDepth == depth + 1) {
			return;
		}
		String indent = new String(new char[depth * 2]).replace('\0', ' ');
		Class<?> clazz = o.getClass();

		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers())) {
					// ignore static fields
					continue;
				}
				Object v = ModelVisitorDriver.readProperty(o, field.getName());
				if (!ModelVisitorDriver.isEmpty(v)) {
					sb.append(indent).append(field.getName()).append(" --> ");
					appendDescription(m, v, sb, depth + 1, maxDepth);
					sb.append("\n");
				}
			}
			clazz = clazz.getSuperclass();
		}
	}
}
