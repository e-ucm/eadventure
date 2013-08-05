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

package es.eucm.ead.engine.assets.drawables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.google.inject.Inject;

import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.assets.drawable.compounds.EAdStateDrawable;
import es.eucm.ead.engine.assets.AbstractRuntimeAsset;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.canvas.GdxCanvas;

public class RuntimeStateDrawable extends
		AbstractRuntimeAsset<EAdStateDrawable> implements
		RuntimeDrawable<EAdStateDrawable> {

	private Map<String, RuntimeDrawable<?>> drawables;

	private int time;

	private List<String> states;

	private int level;

	@Inject
	public RuntimeStateDrawable(AssetHandler assetHandler) {
		super(assetHandler);
	}

	@Override
	public boolean loadAsset() {
		super.loadAsset();
		drawables = new HashMap<String, RuntimeDrawable<?>>();
		for (String s : descriptor.getStates()) {
			EAdDrawable d = descriptor.getDrawable(s);
			if (d != null) {
				RuntimeDrawable<?> r = (RuntimeDrawable<?>) assetHandler
						.getRuntimeAsset(d, true);
				drawables.put(s, r);
			}
		}
		return true;
	}

	@Override
	public void freeMemory() {
		super.freeMemory();
		for (RuntimeDrawable<?> r : drawables.values()) {
			r.freeMemory();
		}
		drawables.clear();
		drawables = null;
	}

	@Override
	public RuntimeDrawable<?> getDrawable(int time, List<String> states,
			int level) {
		this.time = time;
		this.states = states;
		this.level = level;
		if (states == null) {
			return drawables.values().iterator().next().getDrawable(time,
					states, level);
		}
		String state = states.get(level);
		RuntimeDrawable<?> d = drawables.get(state);
		if (d == null) {
			return null;
		} else {
			return d.getDrawable(time, states, ++level);
		}
	}

	@Override
	public void refresh() {
		for (RuntimeDrawable<?> d : drawables.values()) {
			d.refresh();
		}
	}

	@Override
	public int getWidth() {
		// Never used
		return 0;
	}

	@Override
	public int getHeight() {
		// Never used
		return 0;
	}

	@Override
	public void render(GdxCanvas c) {
		// Never used

	}

	@Override
	public boolean contains(int x, int y) {
		// Never used
		return false;
	}

	public Texture getTextureHandle() {
		return getDrawable(time, states, level).getTextureHandle();
	}
}
