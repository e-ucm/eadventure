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

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JLabel;

import junit.framework.TestCase;

import org.junit.Test;

import ead.common.resources.assets.multimedia.EAdVideo;
import ead.common.resources.assets.multimedia.Video;
import ead.engine.core.platform.specialassetrenderers.VLCDesktopVideoRenderer;

public class VLCDesktopVideoRendererTest extends TestCase {

	private JFrame frame;
	
	private Object o;
	
	@Test
	public void testLoadVideo() throws URISyntaxException, IOException  {
		testVideo("ead/resources/flame.mpg");
		testVideo("ead/resources/binary/bbb_trailer_360p.webm");
		testVideo("ead/resources/binary/bbb_trailer_400p.ogv");

	}
	
	private void testVideo(String fileName) throws URISyntaxException, IOException {
		Enumeration<URL> temp = ClassLoader.getSystemResources(fileName);
		File file = new File(temp.nextElement().toURI());
		assertTrue(file.exists());
		EAdVideo video = new Video(file.getAbsolutePath());
		assertTrue(new File(video.getUri().getPath()).exists());
		final VLCDesktopVideoRenderer desktopVideoRenderer = new VLCDesktopVideoRenderer(null);
		o = desktopVideoRenderer.getComponent(video);
		assertTrue(o != null);
		assertTrue(o instanceof Component);
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				frame = new JFrame();
				frame.setSize(800, 600);
				frame.setLayout(new BorderLayout());
				frame.add(new JLabel("test video"), BorderLayout.NORTH);
				frame.add((Component) o, BorderLayout.CENTER);
				frame.setVisible(true);
				desktopVideoRenderer.start();
			}
		}).start();
		
		while(!desktopVideoRenderer.isFinished()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		assertTrue(desktopVideoRenderer.isFinished());
		frame.setVisible(false);
		frame.dispose();
		frame = null;		
	}
	
}
