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

package ead.common.model.assets;

import com.gwtent.reflection.client.Reflectable;

import ead.common.interfaces.features.Identified;
import ead.common.model.elements.BasicElement;

/**
 * Classes that implement this interface represent an asset. An asset is any
 * element that can be represented within an eAdventure game.
 */
@Reflectable
public abstract class AbstractAssetDescriptor implements AssetDescriptor {

	private String id;

	public static long lastId = 0;

	public static String idPrefix = "mod";

	public static void initLastId() {
		lastId = 0;
	}

	public AbstractAssetDescriptor() {
		this.id = BasicElement.classToString(this.getClass()) + idPrefix
				+ lastId++;
	}

	/**
	 * Get the element ID. This is guaranteed to be unique.
	 * 
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * Set the element ID. The ID must be unique.
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String toString() {
		return id;
	}

	/**
	 * Should be called from all children. Performs ID comparison; null IDs are
	 * admitted (and considered equal).
	 */
	@Override
	public int hashCode() {
		return this.getId().hashCode() ^ this.getClass().hashCode();
	}

	/**
	 * Should be called from all children. Compares by ID; null IDs are admitted
	 * (and considered equal).
	 * 
	 * @param other
	 * @return
	 */
	@Override
	public boolean equals(Object other) {
		if ((other == null) || !other.getClass().equals(getClass())) {
			return false;
		}
		String oid = ((Identified) other).getId();
		return (oid == null && id == null) || oid.equals(id);
	}
}
