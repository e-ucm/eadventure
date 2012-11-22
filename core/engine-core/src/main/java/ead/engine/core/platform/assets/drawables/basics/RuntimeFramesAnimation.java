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

package ead.engine.core.platform.assets.drawables.basics;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import ead.common.resources.assets.AssetDescriptor;

import ead.common.resources.assets.drawable.basics.animation.Frame;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.engine.core.platform.assets.AbstractRuntimeAsset;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeCompoundDrawable;
import ead.engine.core.platform.assets.RuntimeDrawable;

public class RuntimeFramesAnimation extends
		AbstractRuntimeAsset<FramesAnimation> implements
		RuntimeCompoundDrawable<FramesAnimation> {

	private List<RuntimeDrawable<?, ?>> frames;

	private AssetHandler assetHandler;

	private boolean loaded;

	private boolean loading;

	@Inject
	public RuntimeFramesAnimation(AssetHandler assetHandler) {
		this.assetHandler = assetHandler;
		frames = new ArrayList<RuntimeDrawable<?, ?>>();
		loaded = false;
		loading = false;
	}

	@Override
	public boolean loadAsset() {
		for (Frame f : descriptor.getFrames()) {
			RuntimeDrawable<?, ?> d = (RuntimeDrawable<?, ?>) assetHandler
					.getRuntimeAsset(f.getDrawable(), true);
			frames.add(d);
		}
		loading = true;
		return true;
	}

	@Override
	public void freeMemory() {
		for (RuntimeDrawable<?, ?> d : frames) {
			d.freeMemory();
		}
		frames.clear();
	}

	@Override
	public boolean isLoaded() {
		if (loading) {
			loaded = true;
			for (RuntimeDrawable<?, ?> d : frames) {
				if (!d.isLoaded()) {
					loaded = false;
					break;
				}
			}
			loading = false;
		}
		return loaded;
	}

	@Override
	public RuntimeDrawable<?, ?> getDrawable(int time, List<String> states,
			int level) {
		int index = descriptor.getFrameIndexFromTime(time);
		return frames.get(index);
	}

	@Override
	public int getLength() {
		int length = 0;
		for (RuntimeDrawable d : frames) {
			length += d.getLength();
		}
		return length;
	}

}
