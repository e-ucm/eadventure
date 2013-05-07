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

package ead.engine.core.game;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.assets.multimedia.EAdSound;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.assets.multimedia.RuntimeSound;
import ead.engine.core.game.interfaces.SoundManager;

@Singleton
public class SoundManagerImpl implements SoundManager {

	private RuntimeSound backgroundMusic;

	private AssetHandler assetHandler;

	private boolean silence;

	private List<RuntimeSound> currentSounds;

	@Inject
	public SoundManagerImpl(AssetHandler assetHandler) {
		this.assetHandler = assetHandler;
		currentSounds = new ArrayList<RuntimeSound>();
	}

	@Override
	public void playSound(EAdSound sound, boolean overlay, float volume) {
		RuntimeSound s = (RuntimeSound) assetHandler.getRuntimeAsset(sound);
		s.play(overlay, silence ? 0.0f : volume);
		if (!currentSounds.contains(s)) {
			currentSounds.add(s);
		}
	}

	@Override
	public void playBackgroundMusic(EAdSound sound, float volume) {
		if (backgroundMusic != null) {
			backgroundMusic.stop();
			backgroundMusic = null;
		}

		if (sound != null) {
			backgroundMusic = (RuntimeSound) assetHandler
					.getRuntimeAsset(sound);
			backgroundMusic.loop(silence ? 0.0f : volume);
		}
	}

	public void toggleSound() {
		setSilence(!silence);
	}

	public void setSilence(boolean silence) {
		this.silence = silence;
		float volume = silence ? 0.0f : 1.0f;
		if (backgroundMusic != null) {
			backgroundMusic.setVolume(volume);
		}
		for (RuntimeSound s : currentSounds) {
			s.setVolume(volume);
		}
	}

	public boolean isSilence() {
		return silence;
	}

	@Override
	public void setPause(boolean paused) {
		// FIXME currently, the API sound doesn't allow to pause sounds...

	}

	@Override
	public void stopAll() {
		if (backgroundMusic != null) {
			backgroundMusic.stop();
		}
		for (RuntimeSound s : currentSounds) {
			s.stop();
		}
	}

}
