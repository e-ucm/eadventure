package es.eucm.eadventure.engine.core.platform.test.specialassetrenderers;

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

import es.eucm.eadventure.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.common.resources.assets.multimedia.impl.VideoImpl;
import es.eucm.eadventure.engine.core.platform.impl.specialassetrenderers.DesktopVideoRenderer;

public class DesktopVideoRendererTest extends TestCase {

	private JFrame frame;
	
	private Object o;
	
	@Test
	public void testLoadVideo() throws URISyntaxException, IOException  {
		Enumeration<URL> temp = ClassLoader.getSystemResources("binary/flame.mpg");
		File file = new File(temp.nextElement().toURI());
		assertTrue(file.exists());
		Video video = new VideoImpl(file.getAbsolutePath());
		assertTrue(new File(video.getURI()).exists());
		DesktopVideoRenderer desktopVideoRenderer = new DesktopVideoRenderer();
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
			}
		}).start();
		
		while(!desktopVideoRenderer.isFinished()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		assertTrue(desktopVideoRenderer.isFinished());
		frame.setVisible(false);
		frame.dispose();
		frame = null;

	}
	
}
