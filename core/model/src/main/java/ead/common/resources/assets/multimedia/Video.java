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

package ead.common.resources.assets.multimedia;

import ead.common.interfaces.Param;
import ead.common.resources.assets.AbstractAssetDescriptor;
import ead.common.util.EAdURI;

public class Video extends AbstractAssetDescriptor implements EAdVideo {

	@Param("stream")
	private boolean stream;

	@Param("uri")
	private EAdURI uri;

	public Video() {
		this(null);
	}

	public Video(String uri) {
		this.uri = new EAdURI(uri);
	}

	@Override
	public EAdURI getUri() {
		return uri;
	}

	@Override
	public void setUri(EAdURI uri) {
		this.uri = uri;
	}

	@Override
	public boolean isStream() {
		return stream;
	}

	public void setStream(boolean stream) {
		this.stream = stream;
	}

	@Override
	public int hashCode() {
		int hash = 7 * super.hashCode();
		hash = 53 * hash + (this.stream ? 1 : 0);
		hash = 53 * hash + (this.uri != null ? this.uri.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if ( ! super.equals(obj)) {
			return false;
		}
		final Video other = (Video) obj;
		if (this.stream != other.stream) {
			return false;
		}
		if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri))) {
			return false;
		}
		return true;
	}
}
