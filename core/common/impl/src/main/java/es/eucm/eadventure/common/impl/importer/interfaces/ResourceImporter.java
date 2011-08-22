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

package es.eucm.eadventure.common.impl.importer.interfaces;

import java.util.List;
import java.util.Map;

import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.model.elements.EAdGeneralElement;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

public interface ResourceImporter {

	public static final String DRAWABLE = "drawable";

	public static final String BINARY = "binary";

	/**
	 * Returns the new URI for an old resource situated in oldURI. It also
	 * copies the old resource to its new location
	 * 
	 * @param oldURI
	 *            the old URI
	 * @return the new URI for the resource. {@code null} it there was any
	 *         problem with the import
	 */
	String getURI(String oldURI);

	/**
	 * Set the path for the resources importer
	 * 
	 * @param newAdventurePath
	 *            Absolute path where the new adventure must be placed
	 */
	public void setPath(String newAdventurePath);

	void importResources(EAdGeneralElement element, List<Resources> resources,
			Map<String, String> resourcesStrings,
			Map<String, Object> resourcesObjectsClasses);

	boolean copyFile(String oldURI, String newURI);

	/**
	 * Returns an asset descriptor for an old asset path and the class in the
	 * new model
	 * 
	 * @param assetPath
	 *            old asset path
	 * @param clazz
	 *            the class in the new model
	 * @return
	 */
	AssetDescriptor getAssetDescritptor(String assetPath, Class<?> clazz);

	/**
	 * Returns the folder where project is being imported
	 * 
	 * @return
	 */
	String getNewProjecFolder();
}
