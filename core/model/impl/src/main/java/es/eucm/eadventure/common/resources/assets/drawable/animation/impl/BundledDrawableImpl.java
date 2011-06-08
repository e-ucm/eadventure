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

package es.eucm.eadventure.common.resources.assets.drawable.animation.impl;

import java.util.Set;

import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.model.EAdMap;
import es.eucm.eadventure.common.model.impl.EAdMapImpl;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.drawable.BundledDrawable;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;

public class BundledDrawableImpl implements
		BundledDrawable {

	private EAdMap<String, Drawable> animations;

	public BundledDrawableImpl( ) {
		animations = new EAdMapImpl<String, Drawable>( "frames",String.class, Drawable.class);
	}

	@Override
	public boolean addDrawable(String stateName, Drawable animation) {
		if (animations.containsKey(stateName))
			return false;
		else {
			animations.put(stateName, animation);
			return true;
		}
	}

	@Override
	public Set<String> getStates() {
		return animations.keySet();
	}

	@Override
	public Drawable getDrawable(String stateName) {
		return animations.get(stateName);
	}

	@Override
	public AssetDescriptor getAssetDescritpor(Orientation orientation) {
		return animations.get(orientation.toString());
	}

	@Override
	public boolean addDrawable(Orientation orientation, Drawable animation) {
		return addDrawable(orientation.toString(), animation);
	}

}
