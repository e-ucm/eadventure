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

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.GdxNativesLoader;

import java.util.HashMap;
import java.util.Map;

public class TestApplication implements Application {

	private ApplicationListener listener;

	private Input input;

	private Files files;

	private boolean done;

	protected final Array<Runnable> runnables = new Array();
	private Graphics graphics;
	private Audio audio;

	public TestApplication(ApplicationListener listener, int width, int height) {
		this.listener = listener;

		files = new LwjglFiles();
		audio = new TestAudio();
		input = new TestInput();
		graphics = new TestGraphics(width, height);
		done = false;

		Gdx.app = this;
		Gdx.graphics = graphics;
		Gdx.files = files;
		Gdx.audio = audio;
		Gdx.input = input;
		Gdx.gl = graphics.getGLCommon();
		Gdx.gl10 = graphics.getGL10();
		Gdx.gl20 = graphics.getGL20();
		GdxNativesLoader.load();
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return listener;
	}

	@Override
	public Graphics getGraphics() {
		return null;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public Files getFiles() {
		return files;
	}

	@Override
	public Net getNet() {
		return null;
	}

	@Override
	public void log(String tag, String message) {
	}

	@Override
	public void log(String tag, String message, Throwable exception) {
	}

	@Override
	public void error(String tag, String message) {
	}

	@Override
	public void error(String tag, String message, Throwable exception) {
	}

	@Override
	public void debug(String tag, String message) {
	}

	@Override
	public void debug(String tag, String message, Throwable exception) {
	}

	@Override
	public void setLogLevel(int logLevel) {
	}

	@Override
	public int getLogLevel() {
		return 0;
	}

	@Override
	public ApplicationType getType() {
		return ApplicationType.Desktop;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public long getJavaHeap() {
		return 0;
	}

	@Override
	public long getNativeHeap() {
		return 0;
	}

	Map<String, Preferences> preferences = new HashMap<String, Preferences>();

	@Override
	public Preferences getPreferences(String name) {
		if (preferences.containsKey(name)) {
			return preferences.get(name);
		} else {
			Preferences prefs = new LwjglPreferences(name);
			preferences.put(name, prefs);
			return prefs;
		}
	}

	@Override
	public Clipboard getClipboard() {
		return null;
	}

	@Override
	public void postRunnable(Runnable runnable) {
		synchronized (runnables) {
			runnables.add(runnable);
			Gdx.graphics.requestRendering();
		}
	}

	@Override
	public void exit() {
		done = true;
	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
	}

	public void run() {
		listener.create();
		listener.resize(graphics.getWidth(), graphics.getHeight());
		// Remove the first runnable, which is the DesktopGUI setting the cursor to blank using natives methods
		runnables.removeIndex(0);
		while (!done) {
			for (int i = 0; i < runnables.size; i++) {
				runnables.get(i).run();
			}
			runnables.clear();
			listener.render();
		}
	}
}
