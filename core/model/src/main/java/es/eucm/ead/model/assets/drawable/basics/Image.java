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

package es.eucm.ead.model.assets.drawable.basics;

import es.eucm.ead.model.interfaces.HasURI;
import es.eucm.ead.model.interfaces.Param;
import es.eucm.ead.model.assets.AbstractAssetDescriptor;

/**
 * An image asset
 *
 */
public class Image extends AbstractAssetDescriptor implements EAdBasicDrawable,
		HasURI {

	@Param
	private String uri;

	/**
	 * Constructs an empty
	 */
	public Image() {

	}

	/**
	 * Constructs an image with the given String
	 *
	 * @param uri
	 *            the image's String
	 */
	public Image(String uri) {
		this.uri = uri;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public int hashCode() {
		int hash = 7 * super.hashCode();
		hash = 37 * hash + (this.uri != null ? this.uri.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		final Image other = (Image) obj;
		if (this.uri != other.uri
				&& (this.uri == null || !this.uri.equals(other.uri))) {
			return false;
		}
		return true;
	}

}
