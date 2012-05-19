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

package ead.engine.desktop.core.platform.assets.multimedia;

import java.io.IOException;
import java.io.InputStream;

import com.google.inject.Inject;

import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.multimedia.RuntimeSound;
import ead.engine.desktop.core.platform.DesktopAssetHandler;

public class DesktopSound extends RuntimeSound {

	private InputStream inputStream;

	private Sound sound;

	@Inject
	public DesktopSound(AssetHandler assetHandler) {
		super(assetHandler);
	}

	@Override
	public boolean loadAsset() {
		String path = getAssetDescriptor().getUri().getPath();
		inputStream = ((DesktopAssetHandler) assetHandler)
				.getResourceAsStream(path);
		if (path.endsWith(".mid") || path.endsWith(".midi")) {
			sound = new SoundMidi(inputStream, false);
		} else if (path.endsWith(".mp3")) {
			sound = new SoundMp3(inputStream, false);
		}

		return inputStream != null;
	}

	@Override
	public void freeMemory() {
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isLoaded() {
		return inputStream != null;
	}

	@Override
	public void play() {
		if (sound != null)
			sound.playOnce();
	}

	@Override
	public void stop() {
		if (sound != null)
			sound.startPlaying();
	}

}
