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

package ead.common.resources.assets.drawable.compounds;

import java.util.Collection;
import java.util.Set;

import ead.common.interfaces.Param;
import ead.common.model.elements.extra.EAdMap;
import ead.common.model.elements.extra.EAdMapImpl;
import ead.common.resources.assets.drawable.EAdDrawable;

/**
 * Basic implementation for a {@link EAdStateDrawable}
 * 
 * @author anserran
 * 
 */
public class StateDrawable implements EAdStateDrawable {

	@Param("drawables")
	private EAdMap<String, EAdDrawable> drawables;

	/**
	 * Constructs an empty bundle of drawables
	 */
	public StateDrawable() {
		drawables = new EAdMapImpl<String, EAdDrawable>(String.class,
				EAdDrawable.class);
	}

	public EAdMap<String, EAdDrawable> getDrawables() {
		return drawables;
	}

	public void setDrawables(EAdMap<String, EAdDrawable> drawables) {
		this.drawables = drawables;
	}

	@Override
	public boolean addDrawable(String stateName, EAdDrawable drawable) {
		if (drawables.containsKey(stateName))
			return false;
		else {
			drawables.put(stateName, drawable);
			return true;
		}
	}

	@Override
	public Set<String> getStates() {
		return drawables.keySet();
	}

	@Override
	public EAdDrawable getDrawable(String stateName) {
		return drawables.get(stateName);
	}

	@Override
	public Collection<EAdDrawable> getDrawablesCollection() {
		return drawables.values();
	}

	public void setDrawable(Object state, EAdDrawable image) {
		addDrawable(state.toString(), image);
	}

}
