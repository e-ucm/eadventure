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

package es.eucm.ead.playground.tests.application;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class TestAudio implements Audio {
	@Override
	public AudioDevice newAudioDevice(int samplingRate, boolean isMono) {
		return new TestAudioDevice();
	}

	@Override
	public AudioRecorder newAudioRecorder(int samplingRate, boolean isMono) {
		return new TestAudioRecorder();
	}

	@Override
	public Sound newSound(FileHandle fileHandle) {
		return new TestSound();
	}

	@Override
	public Music newMusic(FileHandle file) {
		return new TestMusic();
	}

	public static class TestMusic implements Music {

		@Override
		public void play() {
		}

		@Override
		public void pause() {
		}

		@Override
		public void stop() {
		}

		@Override
		public boolean isPlaying() {
			return false;
		}

		@Override
		public void setLooping(boolean isLooping) {
		}

		@Override
		public boolean isLooping() {
			return false;
		}

		@Override
		public void setVolume(float volume) {
		}

		@Override
		public float getVolume() {
			return 0;
		}

		@Override
		public void setPan(float pan, float volume) {
		}

		@Override
		public float getPosition() {
			return 0;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void setOnCompletionListener(OnCompletionListener listener) {
		}
	}

	public static class TestSound implements Sound {

		@Override
		public long play() {
			return 0;
		}

		@Override
		public long play(float volume) {
			return 0;
		}

		@Override
		public long play(float volume, float pitch, float pan) {
			return 0;
		}

		@Override
		public long loop() {
			return 0;
		}

		@Override
		public long loop(float volume) {
			return 0;
		}

		@Override
		public long loop(float volume, float pitch, float pan) {
			return 0;
		}

		@Override
		public void stop() {
		}

		@Override
		public void pause() {
		}

		@Override
		public void resume() {
		}

		@Override
		public void dispose() {
		}

		@Override
		public void stop(long soundId) {
		}

		@Override
		public void pause(long soundId) {
		}

		@Override
		public void resume(long soundId) {
		}

		@Override
		public void setLooping(long soundId, boolean looping) {
		}

		@Override
		public void setPitch(long soundId, float pitch) {
		}

		@Override
		public void setVolume(long soundId, float volume) {
		}

		@Override
		public void setPan(long soundId, float pan, float volume) {
		}

		@Override
		public void setPriority(long soundId, int priority) {
		}
	}

	public static class TestAudioRecorder implements AudioRecorder {

		@Override
		public void read(short[] samples, int offset, int numSamples) {
		}

		@Override
		public void dispose() {
		}
	}

	public static class TestAudioDevice implements AudioDevice {

		@Override
		public boolean isMono() {
			return false;
		}

		@Override
		public void writeSamples(short[] samples, int offset, int numSamples) {
		}

		@Override
		public void writeSamples(float[] samples, int offset, int numSamples) {
		}

		@Override
		public int getLatency() {
			return 0;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void setVolume(float volume) {
		}
	}
}
