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

package ead.engine.core.assets.drawables;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ead.common.model.assets.drawable.basics.animation.Frame;
import ead.common.model.assets.drawable.basics.animation.FramesAnimation;
import ead.engine.core.assets.AbstractRuntimeAsset;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.canvas.GdxCanvas;

public class RuntimeFramesAnimation extends
		AbstractRuntimeAsset<FramesAnimation> implements
		RuntimeDrawable<FramesAnimation> {

	private List<RuntimeDrawable<?>> frames;

	private List<Integer> times;

	private int totalTime;

	@Inject
	public RuntimeFramesAnimation(AssetHandler assetHandler) {
		super(assetHandler);
	}

	@Override
	public boolean loadAsset() {
		super.loadAsset();
		frames = new ArrayList<RuntimeDrawable<?>>();
		times = new ArrayList<Integer>();
		for (Frame f : descriptor.getFrames()) {
			RuntimeDrawable<?> d = (RuntimeDrawable<?>) assetHandler
					.getRuntimeAsset(f.getDrawable(), true);
			frames.add(d);
			totalTime += f.getTime();
			times.add(totalTime);
		}
		return true;
	}

	@Override
	public void freeMemory() {
		super.freeMemory();
		for (RuntimeDrawable<?> d : frames) {
			d.freeMemory();
		}
		frames.clear();
		frames = null;
	}

	@Override
	public RuntimeDrawable<?> getDrawable(int time, List<String> states,
			int level) {
		int realTime = time % totalTime;
		int index = 0;
		while (realTime > times.get(index)) {
			index++;
		}
		return frames.get(index);
	}

	@Override
	public void refresh() {
		for (RuntimeDrawable<?> d : this.frames) {
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

}
