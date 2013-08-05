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

package es.eucm.ead.engine.desktop.platform.assets.video.vlc;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

public class VLCMediaPlayerEventListener implements MediaPlayerEventListener {

	private VLCVideoRenderer vlcDesktopVideoRenderer;

	private int count;

	public VLCMediaPlayerEventListener() {
	}

	public void setRenderer(VLCVideoRenderer renderer) {
		this.vlcDesktopVideoRenderer = renderer;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public void backward(MediaPlayer arg0) {
	}

	@Override
	public void endOfSubItems(MediaPlayer arg0) {
		vlcDesktopVideoRenderer.setFinished(true);
		vlcDesktopVideoRenderer.setStarted(false);
	}

	@Override
	public void error(MediaPlayer player) {

		// vlcDesktopVideoRenderer.setFinished(true);
		// vlcDesktopVideoRenderer.setStarted(false);
	}

	@Override
	public void finished(MediaPlayer arg0) {
		count--;
		if (count == 0) {
			vlcDesktopVideoRenderer.setFinished(true);
			vlcDesktopVideoRenderer.setStarted(false);
		}
	}

	@Override
	public void forward(MediaPlayer arg0) {
	}

	@Override
	public void lengthChanged(MediaPlayer arg0, long arg1) {
	}

	@Override
	public void mediaDurationChanged(MediaPlayer arg0, long arg1) {
	}

	@Override
	public void mediaFreed(MediaPlayer arg0) {
	}

	@Override
	public void mediaMetaChanged(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void mediaParsedChanged(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void mediaStateChanged(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void mediaSubItemAdded(MediaPlayer arg0, libvlc_media_t arg1) {
	}

	@Override
	public void newMedia(MediaPlayer arg0) {
	}

	@Override
	public void opening(MediaPlayer arg0) {
	}

	@Override
	public void pausableChanged(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void paused(MediaPlayer arg0) {
	}

	@Override
	public void playing(MediaPlayer mediaPlayer) {
	}

	@Override
	public void positionChanged(MediaPlayer arg0, float arg1) {
	}

	@Override
	public void seekableChanged(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void snapshotTaken(MediaPlayer arg0, String arg1) {
	}

	@Override
	public void stopped(MediaPlayer arg0) {
		vlcDesktopVideoRenderer.setFinished(true);
		vlcDesktopVideoRenderer.setStarted(false);
		vlcDesktopVideoRenderer.stop();
	}

	@Override
	public void subItemFinished(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void subItemPlayed(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void timeChanged(MediaPlayer arg0, long arg1) {
	}

	@Override
	public void titleChanged(MediaPlayer arg0, int arg1) {
	}

	@Override
	public void videoOutput(MediaPlayer arg0, int arg1) {

	}

	@Override
	public void buffering(MediaPlayer arg0, float arg1) {

	}

	@Override
	public void mediaChanged(MediaPlayer arg0, libvlc_media_t arg1, String arg2) {

	}

}
