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

package es.eucm.ead.engine.assets.multimedia;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.inject.Inject;
import es.eucm.ead.model.assets.multimedia.Music;
import es.eucm.ead.engine.assets.AbstractRuntimeAsset;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.assets.AssetHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuntimeMusic extends AbstractRuntimeAsset<Music> {

	static private Logger logger = LoggerFactory.getLogger(RuntimeMusic.class);

	private FileHandle fh;

	private com.badlogic.gdx.audio.Music music;

	@Inject
	public RuntimeMusic(AssetHandler assetHandler) {
		super(assetHandler);
	}

	@Override
	public boolean loadAsset() {
		super.loadAsset();
		fh = ((AssetHandlerImpl) assetHandler).getFileHandle(descriptor
				.getUri());
		try {
			music = Gdx.audio.newMusic(fh);
		} catch (Exception e) {
			logger.error("Error loading sound {}", descriptor.getUri(), e);
		}
		return true;
	}

	public void setVolume(float volume) {
		if (music != null) {
			music.setVolume(volume);
		}
	}

	@Override
	public void freeMemory() {
		super.freeMemory();
		if (music == null) {
			return;
		} else {
			music.dispose();
			music = null;
		}
	}

	public void play(boolean loop, float volume) {
		if (music == null) {
			return;
		}
		music.setVolume(volume);
		music.setLooping(loop);
		music.play();
	}

	public void stop() {
		if (music == null) {
			return;
		}
		music.stop();
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

	public void setPause(boolean pause) {
		if (pause) {
			music.pause();
		} else {
			this.music.play();
		}
	}
}
