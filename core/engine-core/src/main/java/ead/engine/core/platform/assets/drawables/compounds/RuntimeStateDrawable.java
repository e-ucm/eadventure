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

import ead.common.resources.assets.drawable.EAdDrawable;
import ead.common.resources.assets.drawable.compounds.EAdStateDrawable;
import ead.engine.core.platform.assets.AbstractRuntimeAsset;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeCompoundDrawable;
import ead.engine.core.platform.assets.RuntimeDrawable;

public class RuntimeStateDrawable extends
		AbstractRuntimeAsset<EAdStateDrawable> implements
		RuntimeCompoundDrawable<EAdStateDrawable> {

	private AssetHandler assetHandler;

	private Map<String, RuntimeCompoundDrawable<?>> drawables;

	private boolean loaded;

	private boolean loading;

	@Inject
	public RuntimeStateDrawable(AssetHandler assetHandler) {
		this.assetHandler = assetHandler;
		drawables = new HashMap<String, RuntimeCompoundDrawable<?>>();
		loaded = false;
		loading = false;
	}

	@Override
	public boolean loadAsset() {
		loading = true;
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
		for (RuntimeCompoundDrawable<?> r : drawables.values()) {
			r.freeMemory();
		}
		drawables.clear();
	}

	@Override
	public boolean isLoaded() {
		if (loading) {
			loaded = true;
			for (RuntimeCompoundDrawable<?> r : drawables.values()) {
				if (!r.isLoaded()) {
					loaded = false;
					break;
				}
				loading = false;
			}
		}
		return loaded;
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
	public int getLength() {
		int length = 0;
		for (RuntimeCompoundDrawable d : drawables.values()) {
			length += d.getLength();
		}
		return length;
	}
}
