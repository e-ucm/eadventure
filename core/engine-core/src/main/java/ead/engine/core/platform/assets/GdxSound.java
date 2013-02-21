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

package ead.engine.core.platform.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.google.inject.Inject;

import ead.engine.core.platform.assets.multimedia.RuntimeSound;

public class GdxSound extends RuntimeSound {

	private Sound sound;

	private long id;

	private FileHandle fh;

	@Inject
	public GdxSound(AssetHandler assetHandler) {
		super(assetHandler);
		id = -1;
	}

	@Override
	public boolean loadAsset() {
		super.loadAsset();
		fh = ((GdxAssetHandler) assetHandler)
				.getFileHandle(descriptor.getUri());
		sound = Gdx.audio.newSound(fh);
		id = -1;
		return true;
	}

	@Override
	public void freeMemory() {
		super.freeMemory();
		sound.dispose();
		sound = null;
	}

	public void play(boolean override) {
		play(override, 1.0f);
	}

	@Override
	public void play(boolean override, float volume) {
		if (override || id == -1) {
			id = sound.play(volume);
		} else if (id != -1) {
			sound.stop(id);
			id = sound.play();
		}
	}

	@Override
	public void stop() {
		sound.stop(id);
	}

	@Override
	public void loop(float volume) {
		id = sound.loop(volume);
	}

	@Override
	public void refresh() {
		FileHandle fh = ((GdxAssetHandler) assetHandler)
				.getFileHandle(descriptor.getUri());
		if (!this.fh.path().equals(fh.path())) {
			this.freeMemory();
			this.loadAsset();
		}
	}
}
