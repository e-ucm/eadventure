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

package ead.editor.model.nodes;

import java.util.ArrayList;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.shapes.BezierShape;
import ead.common.resources.assets.multimedia.Sound;
import ead.common.resources.assets.multimedia.Video;
import ead.common.util.EAdRectangle;
import ead.editor.R;
import es.eucm.eadventure.common.data.animation.Animation;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Synthetic node for i18n-susceptible game strings.
 *
 * @author mfreire
 */
public class AssetNode extends EditorNode {

	private ArrayList<String> sources = new ArrayList<String>();
	private String notes;

	public AssetNode(int id) {
		super(id);
	}

	public AssetDescriptor getDescriptor() {
		return (AssetDescriptor)(getFirst().getContent());
	}

	public void setDescriptor(AssetDescriptor descriptor) {
		throw new IllegalArgumentException("Not yet implemented");
	}

	public ArrayList<String> getSources() {
		return sources;
	}

	@Override
	public void writeInner(StringBuilder sb) {
		for (String s : sources) {
			sb.append("\t<source>").append(s).append("</source>\n");
		}
		sb.append("\t<notes>").append(notes.trim()).append("</notes>\n");
	}

	@Override
	public void restoreInner(Element element) {
		sources.clear();
		NodeList sourceList = element.getElementsByTagName("source");
		for (int i=0; i<sourceList.getLength(); i++) {
			sources.add(sourceList.item(i).getTextContent().trim());
		}
		notes = element.getElementsByTagName("notes").item(0).getTextContent().trim();
	}

	@Override
	public String getLinkText() {
		AssetDescriptor d = getDescriptor();
		if (d instanceof Video || d instanceof Image || d instanceof Sound) {
			String s = d.toString();
			return s.substring(s.indexOf("/")+1);
		} else {
			return ""+getFirst().getId();
		}
	}

	@Override
	public String getLinkIcon() {
		AssetDescriptor d = getDescriptor();
		if (d instanceof Video) {
			return R.Drawable.assets__video_png;
		} else if (d instanceof Image) {
			return R.Drawable.assets__image_png;
		} else if (d instanceof Animation) {
			return R.Drawable.assets__animation_png;
		} else if (d instanceof EAdRectangle) {
			return R.Drawable.assets__rectangle_png;
		} else if (d instanceof BezierShape) {
			return R.Drawable.assets__bezier_png;
		} else {
			return super.getLinkIcon();
		}
	}
}
