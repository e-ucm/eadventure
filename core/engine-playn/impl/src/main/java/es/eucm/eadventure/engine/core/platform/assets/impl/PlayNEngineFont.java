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

package es.eucm.eadventure.engine.core.platform.assets.impl;

import es.eucm.eadventure.common.params.EAdFont;
import es.eucm.eadventure.common.params.geom.impl.EAdRectangleImpl;
import es.eucm.eadventure.engine.core.platform.RuntimeFont;

public class PlayNEngineFont implements RuntimeFont {

	private EAdFont eadFont;

	public PlayNEngineFont(EAdFont font) {
		this.eadFont = font;
	}

	public int getFont() {
		return 0;
	}

	@Override
	public EAdFont getEAdFont() {
		return eadFont;
	}

	@Override
	public int stringWidth(String string) {
		//FIXME
		return string.length() * 10;
	}

	@Override
	public int lineHeight() {
		//FIXME
		return 15;
	}

	@Override
	public EAdRectangleImpl stringBounds(String string) {
		//FIXME
		return new EAdRectangleImpl(0, 0, stringWidth(string), lineHeight());
	}

}
