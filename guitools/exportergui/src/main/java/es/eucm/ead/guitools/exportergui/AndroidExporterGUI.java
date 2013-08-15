package es.eucm.ead.guitools.exportergui;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Properties;

public class AndroidExporterGUI extends JFrame {

	public AndroidExporterGUI(AndroidExporterGUI.ExportListener listener) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "";
		cfg.useGL20 = true;
		cfg.width = 400;
		cfg.height = 320;
		cfg.fullscreen = false;
		cfg.forceExit = true;
		Canvas c = new Canvas();
		c.setSize(cfg.width, cfg.height);
		this.add(c);
		this.pack();
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		final ExporterApplicationListener app = new ExporterApplicationListener(
				this, listener);
		new LwjglApplication(app, cfg, c);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				app.saveProperties();
			}
		});
	}

	public static interface ExportListener {
		void export(Properties properties);
	}
}
