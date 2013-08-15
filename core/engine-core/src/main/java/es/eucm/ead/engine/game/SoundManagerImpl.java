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

package es.eucm.ead.engine.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.model.assets.multimedia.EAdSound;
import es.eucm.ead.model.assets.multimedia.Music;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.assets.multimedia.RuntimeMusic;
import es.eucm.ead.engine.assets.multimedia.RuntimeSound;
import es.eucm.ead.engine.game.interfaces.GameState;
import es.eucm.ead.engine.game.interfaces.SoundManager;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class SoundManagerImpl implements SoundManager {

	private RuntimeMusic backgroundMusic;

	private AssetHandler assetHandler;

	private boolean silence;

	private List<RuntimeSound> currentSounds;

	private GameState gameState;

	@Inject
	public SoundManagerImpl(AssetHandler assetHandler, GameState gameState) {
		this.assetHandler = assetHandler;
		this.gameState = gameState;
		this.silence = false;
		currentSounds = new ArrayList<RuntimeSound>();
		gameState.setValue(SystemFields.SOUND_ON, !isSilence());
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
	public void playBackgroundMusic(Music sound, boolean loop, float volume) {
		if (backgroundMusic != null) {
			backgroundMusic.stop();
			backgroundMusic = null;
		}

		if (sound != null) {
			backgroundMusic = (RuntimeMusic) assetHandler.getRuntimeAsset(
					sound, true);
			backgroundMusic.play(loop, silence ? 0.0f : volume);
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
		gameState.setValue(SystemFields.SOUND_ON, !isSilence());
	}

	public boolean isSilence() {
		return silence;
	}

	@Override
	public void setPause(boolean paused) {
		// FIXME currently, the API sound doesn't allow to pause
		// sounds..
		if (backgroundMusic != null) {
			backgroundMusic.setPause(paused);
		}

	}

	@Override
	public void stopAll() {
		if (backgroundMusic != null) {
			backgroundMusic.stop();
		}
		for (RuntimeSound s : currentSounds) {
			s.stop();
		}
		gameState.setValue(SystemFields.SOUND_ON, !isSilence());
	}

}
