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

package ead.engine.core.platform.test.specialassetrenderers;

import static java.util.logging.Level.SEVERE;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;

import ead.common.resources.assets.multimedia.EAdVideo;
import ead.common.resources.assets.multimedia.Video;
import ead.engine.core.platform.assets.specialassetrenderers.VLCDesktopVideoRenderer;

/**
 * Non JUnit test case
 */
public class VLCDesktopVideoRendererTest {

	private static final Logger log = Logger.getLogger("DesktopVideoRendererTest");

	private JFrame frame;

	private Object o;

	public static final void main(String[] args) {
		try {
			new VLCDesktopVideoRendererTest().testLoadVideo(false);
			new VLCDesktopVideoRendererTest().testLoadVideo(true);
		} catch (URISyntaxException e) {
			log.log(SEVERE, "Problem with URI", e);
		} catch (IOException e) {
			log.log(SEVERE, "Exception with IO", e);
		}
	}

	public void testLoadVideo(boolean fullScreen) throws URISyntaxException, IOException {
		testVideo("ead/resources/binary/flame.mpg", fullScreen);
		// testVideo("ead/resources/binary/bbb_trailer_360p.webm", fullScreen);
		// testVideo("ead/resources/binary/bbb_trailer_400p.ogv", fullScreen);
	}

	private void testVideo(String fileName, final boolean fullscreen) throws URISyntaxException,
			IOException {
		Enumeration<URL> temp = ClassLoader.getSystemResources(fileName);
		File file = new File(temp.nextElement().toURI());
		assertTrue(file.exists(), "File dose not exist");
		EAdVideo video = new Video(file.getAbsolutePath());
		assertTrue(new File(video.getUri().getPath()).exists(), "File dose not exist");
		final VLCDesktopVideoRenderer desktopVideoRenderer = new VLCDesktopVideoRenderer(null);
		o = desktopVideoRenderer.getComponent(video);
		assertTrue(o != null, "Null video component");
		assertTrue(o instanceof Component, "Component of the wrong class");

		new Thread(new Runnable() {

			@Override
			public void run() {
				frame = new JFrame();
				frame.setUndecorated(true);
				frame.setSize(800, 600);
				if (fullscreen) {
					GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
							.getDefaultScreenDevice();
					gd.setFullScreenWindow(frame);

					// Fullscreen exclusive mode does not support
					// painting of video on the canvas.
					int width = frame.getWidth();
					int height = frame.getHeight();
					gd.setFullScreenWindow(null);
					frame.setSize(width, height);
					frame.setLocation(0, 0);
				}
				frame.setLayout(new BorderLayout());
				frame.add(new JLabel("test video"), BorderLayout.NORTH);
				frame.add((Component) o, BorderLayout.CENTER);
				frame.setVisible(true);
				desktopVideoRenderer.start();
			}
		}).start();

		while (!desktopVideoRenderer.isFinished()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		assertTrue(desktopVideoRenderer.isFinished(), "Video is not finished");
		frame.setVisible(false);
		frame.dispose();
		frame = null;
	}

	public static void assertTrue(boolean condition, String text) {
		if (!condition) {
			throw new RuntimeException(text);
		}
	}
}
