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

package es.eucm.ead.model.assets;

import com.gwtent.reflection.client.Reflectable;

import es.eucm.ead.model.elements.BasicElement;
import es.eucm.ead.model.interfaces.features.Identified;

/**
 * Classes that implement this interface represent an asset. An asset is any
 * element that can be represented within an eAdventure game.
 */
@Reflectable
public abstract class AbstractAssetDescriptor implements AssetDescriptor {

	private String id;

	public AbstractAssetDescriptor() {

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
		return BasicElement.classToString(this.getClass())
				+ (id != null ? id.toString() : "");
	}

	public boolean equals(Object o) {
		if (o instanceof Identified) {
			String id2 = ((Identified) o).getId();
			return id == null ? super.equals(o) : id == id2 || id.equals(id2);
		}
		return false;
	}

	public int hashCode() {
		return id == null ? super.hashCode() : getId().hashCode();
	}

}
