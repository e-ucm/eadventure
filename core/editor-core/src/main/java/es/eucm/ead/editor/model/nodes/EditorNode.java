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

package es.eucm.ead.editor.model.nodes;

import es.eucm.ead.editor.R;
import es.eucm.ead.editor.model.EditorModel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import es.eucm.ead.editor.model.ModelIndex;

/**
 * A pattern-node allows editing a set of "engine-model" EditorNodes as a unit.
 * There can be several pattern-nodes for a single editor-node (corresponding
 * to alternate view-points).
 * @author mfreire
 */
public class EditorNode extends DependencyNode<HashSet<DependencyNode<?>>> {

	private static Logger logger = LoggerFactory.getLogger(EditorNode.class);

	public static final int THUMBNAIL_SIZE = 128;
	private static final BufferedImage defaultThumbnail = new BufferedImage(
			THUMBNAIL_SIZE, 128, BufferedImage.TYPE_INT_ARGB);

	protected BufferedImage thumbnail;

	/**
	 * A list of indexed fields.
	 */
	protected String[] indexedFields;

	/**
	 * Builds a new editor-node.
	 * @param id
	 * @param indexedFields
	 */
	public EditorNode(int id, String... indexedFields) {
		super(id, new HashSet<DependencyNode<?>>());
		this.indexedFields = indexedFields;
		ModelIndex.addProperty(this, ModelIndex.editorIdFieldName, "" + id,
				false);
	}

	/**
	 * Returns all fields that should be visited to fully re-index this node.
	 * @return
	 */
	public String[] getIndexedFields() {
		return indexedFields;
	}

	public HashSet<DependencyNode<?>> getContents() {
		return content;
	}

	@Override
	public String getLinkIcon() {
		return R.Drawable.assets__editor_png;
	}

	/**
	 * Subclasses should update their thumbnail when this is called.
	 * It may be called more than necessary, so subclasses should check if the
	 * update is really required...
	 */
	protected void updateThumbnail() {
		// by default, do nothing
	}

	/**
	 * Sets an image as a thumbnail. The image is rescaled and centered as
	 * necessary.
	 * @param image
	 */
	public void setThumbnail(BufferedImage image) {
		int fw = image.getWidth();
		int fh = image.getHeight();
		double s = Math.min(THUMBNAIL_SIZE * 1.0 / fw, THUMBNAIL_SIZE * 1.0
				/ fh);

		thumbnail = new BufferedImage(THUMBNAIL_SIZE, THUMBNAIL_SIZE,
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = thumbnail.getGraphics();
		int tw = (int) (s * fw);
		int th = (int) (s * fh);
		g.drawImage(image, (THUMBNAIL_SIZE - tw) / 2,
				(THUMBNAIL_SIZE - th) / 2, tw, th, null);
	}

	public Image getThumbnail() {
		if (thumbnail == null) {
			updateThumbnail();
		}
		return thumbnail != null ? thumbnail : defaultThumbnail;
	}

	/**
	 * Many editor-nodes will only have a single child.
	 * @return
	 */
	public DependencyNode getFirst() {
		return content.iterator().next();
	}

	public boolean addChild(DependencyNode<?> node) {
		return content.add(node);
	}

	/**
	 * Generates an XML snippet with the contents of this EditorNode.
	 * Format is similar to
	 * <node class="ead.editor.model.MyClass" id="editor-id"
	 *		 contents="comma-separted-list-of-children">
	 *    (element details here)
	 * </node>
	 */
	public void write(StringBuilder sb) {
		sb.append("<node class='").append(getClass().getName())
				.append("' id='").append(getId()).append("' contents='");
		if (getContents().isEmpty()) {
			sb.append("\'");
		} else {
			for (DependencyNode<?> n : getContents()) {
				sb.append(n.getId()).append(",");
			}
			sb.setCharAt(sb.length() - 1, '\'');
		}
		sb.append(">\n\t");
		writeInner(sb);
		sb.append("\n</node>\n");
	}

	/**
	 * Writes inner contents to an XML snippet.
	 * @param sb
	 */
	public void writeInner(StringBuilder sb) {
		// by default, nothing to write
	}

	@SuppressWarnings("unchecked")
	public static EditorNode restore(Element element, EditorModel em) {
		String className = element.getAttribute("class");
		int id = Integer.parseInt(element.getAttribute("id"));
		String contentIdStrings[] = element.getAttribute("contents").split(",");

		EditorNode instance = null;
		ClassLoader cl = EditorNode.class.getClassLoader();
		try {
			Class<EditorNode> c = (Class<EditorNode>) cl.loadClass(className);
			instance = c.getConstructor(Integer.TYPE).newInstance(id);
			for (String cid : contentIdStrings) {
				instance.getContents().add(em.getNode(Integer.parseInt(cid)));
			}
			instance.restoreInner(element, em);
		} catch (Exception e) {
			logger.error("Could not restore editorNode for class {}",
					className, e);
		}
		return instance;
	}

	public void restoreInner(Element element, EditorModel em) {
		// by default, nothing to restore
	}

	/**
	 * Generates a one-line description with as much information as possible.
	 * @return a human-readable description of this node
	 */
	@Override
	public String getTextualDescription(EditorModel m) {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" (").append(getId())
				.append(")");
		for (DependencyNode n : getContents()) {
			sb.append("\n").append(n.getTextualDescription(m));
		}
		return sb.toString();
	}
}