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

package ead.engine.core.platform.assets.drawables.compounds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.compounds.EAdStateDrawable;
import ead.engine.core.platform.assets.AbstractRuntimeAsset;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeCompoundDrawable;
import ead.engine.core.platform.assets.RuntimeDrawable;

public class RuntimeStateDrawable extends
		AbstractRuntimeAsset<EAdStateDrawable> implements
		RuntimeCompoundDrawable<EAdStateDrawable> {

	private Map<String, RuntimeCompoundDrawable<?>> drawables;

	@Inject
	public RuntimeStateDrawable(AssetHandler assetHandler) {
		super(assetHandler);
	}

	@Override
	public boolean loadAsset() {
		super.loadAsset();
		drawables = new HashMap<String, RuntimeCompoundDrawable<?>>();
		for (String s : descriptor.getStates()) {
			EAdDrawable d = descriptor.getDrawable(s);
			if (d != null) {
				RuntimeCompoundDrawable<?> r = (RuntimeCompoundDrawable<?>) assetHandler
						.getRuntimeAsset(d, true);
				drawables.put(s, r);
			}
		}
		return true;
	}

	@Override
	public void freeMemory() {
		super.freeMemory();
		for (RuntimeCompoundDrawable<?> r : drawables.values()) {
			r.freeMemory();
		}
		drawables.clear();
		drawables = null;
	}

	@Override
	public RuntimeDrawable<?, ?> getDrawable(int time, List<String> states,
			int level) {
		if (states == null) {
			return drawables.values().iterator().next().getDrawable(time,
					states, level);
		}
		String state = states.get(level);
		RuntimeCompoundDrawable<?> d = drawables.get(state);
		if (d == null) {
			return null;
		} else {
			return d.getDrawable(time, states, ++level);
		}
	}

	@Override
	public void refresh() {
		for (RuntimeCompoundDrawable<?> d : drawables.values()) {
			d.refresh();
		}
	}
}
