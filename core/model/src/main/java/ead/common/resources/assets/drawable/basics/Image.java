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

package ead.common.resources.assets.drawable.basics;

import ead.common.interfaces.Param;
import ead.common.util.EAdURI;

/**
 * An image asset
 * 
 */
public class Image implements EAdImage {

	@Param("uri")
	private EAdURI uri;

	/**
	 * Constructs an empty
	 */
	public Image() {

	}

	/**
	 * Constructs an image with the given URI
	 * 
	 * @param uri
	 *            the image's URI
	 */
	public Image(String uri) {
		this.uri = new EAdURI(uri);
	}

	/**
	 * Constructs an image with the given URI
	 * @param uri the URI
	 */
	public Image(EAdURI uri) {
		this.uri = uri;
	}

	@Override
	public EAdURI getUri() {
		return uri;
	}

	@Override
	public void setUri(EAdURI uri) {
		this.uri = uri;
	}

	public boolean equals(Object o) {
		if (o != null && o instanceof EAdImage) {
			if (uri == null && ((Image) o).getUri() == null)
				return false;
			if (((Image) o).getUri() == null && uri != null)
				return false;
			return ((Image) o).getUri().equals(uri);
		}
		return false;
	}

	public int hashCode() {
		return (uri != null ? uri.hashCode() * 10 : 0);
	}

	public String toString() {
		return "Img:" + uri;
	}

}
