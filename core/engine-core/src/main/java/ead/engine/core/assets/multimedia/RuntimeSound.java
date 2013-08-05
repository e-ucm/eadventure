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

package ead.engine.core.assets.multimedia;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.google.inject.Inject;
import es.eucm.ead.model.assets.multimedia.EAdSound;
import ead.engine.core.assets.AbstractRuntimeAsset;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.assets.AssetHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuntimeSound extends AbstractRuntimeAsset<EAdSound> {

	private static final Logger logger = LoggerFactory
			.getLogger("RuntimeSound");

	private Sound sound;

	private long id;

	private FileHandle fh;

	@Inject
	public RuntimeSound(AssetHandler assetHandler) {
		super(assetHandler);
		id = -1;
	}

	@Override
	public boolean loadAsset() {
		super.loadAsset();
		fh = ((AssetHandlerImpl) assetHandler).getFileHandle(descriptor
				.getUri());
		try {
			sound = Gdx.audio.newSound(fh);
		} catch (Exception e) {
			logger.error("Error loading sound {}", descriptor.getUri(), e);
		}
		id = -1;
		return true;
	}

	public void setVolume(float volume) {
		if (sound == null) {
			return;
		}
		if (sound != null && id != -1) {
			sound.setVolume(id, volume);
		}
	}

	@Override
	public void freeMemory() {
		if (sound == null) {
			return;
		}
		if (isLoaded()) {
			super.freeMemory();
			sound.dispose();
			sound = null;
		}
	}

	/**
	 * Plays the sound
	 * 
	 * @param overlay
	 *            If true, the sound is played always. If false, the sound is
	 *            played only if it's not playing
	 */
	public void play(boolean overlay) {
		play(overlay, 1.0f);
	}

	public void play(boolean override, float volume) {
		if (sound == null) {
			return;
		}
		if (override || id == -1) {
			id = sound.play(volume);
		} else if (id != -1) {
			sound.stop(id);
			id = sound.play(volume);
		}
	}

	public void stop() {
		if (sound == null) {
			return;
		}
		sound.stop(id);
	}

	public void loop(float volume) {
		if (sound == null) {
			return;
		}
		id = sound.loop(volume);
	}

	@Override
	public void refresh() {
		FileHandle fh = ((AssetHandlerImpl) assetHandler)
				.getFileHandle(descriptor.getUri());
		if (!this.fh.path().equals(fh.path())) {
			this.freeMemory();
			this.loadAsset();
		}
	}

}
