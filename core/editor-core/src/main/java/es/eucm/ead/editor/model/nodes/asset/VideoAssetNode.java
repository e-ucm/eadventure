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

package es.eucm.ead.editor.model.nodes.asset;

import java.io.File;

import es.eucm.ead.editor.R;
import es.eucm.ead.model.assets.multimedia.Video;

/**
 * Image asset node
 *
 * @author mfreire
 */
public class VideoAssetNode extends AssetNode {

	public VideoAssetNode(int id) {
		super(id);
	}

	@Override
	public String getLinkText() {
		String s = ((Video) getDescriptor()).getUri().toString();
		return s.substring(s.lastIndexOf("video") + "video".length() + 1);
	}

	public File resolveUri(String uri) {
		return new File(uri.replace("@", base.getAbsolutePath()
				+ File.separator));
	}

	public File getFile() {
		String uri = ((Video) getDescriptor()).getUri().toString();
		return resolveUri(uri);
	}

	@Override
	public int getAssetSize() {
		return (int) getFile().length();
	}

	@Override
	public String getLinkIcon() {
		return R.Drawable.assets__video_png;
	}
}
