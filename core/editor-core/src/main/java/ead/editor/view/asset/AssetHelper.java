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
package ead.editor.view.asset;

import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.EAdDrawable;
import ead.common.resources.assets.drawable.basics.EAdBasicDrawable;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Utility class that performs asset-related functions.
 *
 * @author mfreire
 */
public class AssetHelper {

	private File resourcePath;

	public static final String ENGINE_RESOURCES_PATH = "ead/engine/resources/";

	public AssetHelper(File resourcePath) {
		this.resourcePath = resourcePath;
	}

	public File getFile(AssetDescriptor d) {
		String s = d.toString();
		if (s.contains("@")) {
			File f = new File(resourcePath.getAbsolutePath(), s.substring(s
					.indexOf('@') + 1));
			return f.exists() ? f : null;
		}
		return null;
	}

	public InputStream getEngineResource(AssetDescriptor d) throws IOException {
		ClassLoader cl = AssetHelper.class.getClassLoader();
		if (d instanceof EAdBasicDrawable) {
			EAdBasicDrawable drawable = (EAdBasicDrawable) d;
		}
		return null;
	}

	public boolean isEngineResource(AssetDescriptor d) {
		return false;
	}

	public Image getThumbnail(AssetDescriptor d, File resourcePath) {
		return null;
	}
}
