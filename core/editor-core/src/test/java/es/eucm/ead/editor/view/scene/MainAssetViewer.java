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

package es.eucm.ead.editor.view.scene;

import com.google.inject.Guice;
import com.google.inject.Injector;
import es.eucm.ead.editor.EditorGuiceModule;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.util.Log4jConfig;
import es.eucm.ead.editor.util.SwingUtilities;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.desktop.platform.DesktopModule;
import es.eucm.ead.engine.desktop.utils.assetviewer.AssetViewer;
import es.eucm.ead.model.assets.drawable.basics.animation.Frame;
import es.eucm.ead.model.assets.drawable.basics.animation.FramesAnimation;
import es.eucm.ead.tools.java.JavaToolsModule;
import es.eucm.ead.tools.reflection.ReflectionClassLoader;
import org.junit.Before;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainAssetViewer {

	private Controller controller;

	public static void main(String args[]) {
		final MainAssetViewer mav = new MainAssetViewer();
		mav.setUp();

		SwingUtilities.doInEDTNow(new Runnable() {
			@Override
			public void run() {
				JFrame jf = new JFrame();
				jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jf.setLayout(new GridLayout(1, 2));
				jf.add(mav.showSampleAsset());
				jf.add(mav.showSampleAsset());
				jf.setLocationRelativeTo(null);
				jf.setSize(400, 200);
				jf.setVisible(true);
			}
		});
	}

	@Before
	public void setUp() {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Info, new Object[] {
				"ModelVisitorDriver", Log4jConfig.Slf4jLevel.Info,
				"EditorModel", Log4jConfig.Slf4jLevel.Debug, "EditorAnnotator",
				Log4jConfig.Slf4jLevel.Debug, "EAdventureImporter",
				Log4jConfig.Slf4jLevel.Debug, "ActorFactory",
				Log4jConfig.Slf4jLevel.Debug, });

		Injector injector = Guice.createInjector(new DesktopModule(),
				new EditorGuiceModule(), new JavaToolsModule());

		// init reflection
		ReflectionClassLoader.init(injector
				.getInstance(ReflectionClassLoader.class));

		// init sample resource root
		File root = new File("../../demos/techdemo/src/main/resources/");
		if (!root.exists()) {
			System.err.println("resources not found");
			return;
		}

		controller = injector.getInstance(Controller.class);
		AssetHandler ah = injector.getInstance(AssetHandler.class);
		ah.setCacheEnabled(false);
		ah.setResourcesLocation(root.getAbsolutePath());
	}

	public JPanel showSampleAsset() {

		final AssetViewer viewer = controller.createAssetViewer();

		final FramesAnimation frames2 = new FramesAnimation();
		frames2.addFrame(new Frame("@drawable/man_walk_w_1.png", 200));
		frames2.addFrame(new Frame("@drawable/man_walk_w_2.png", 200));
		final FramesAnimation frames3 = new FramesAnimation();
		frames3.addFrame(new Frame("@drawable/man_walk_n_1.png", 500));
		frames3.addFrame(new Frame("@drawable/man_walk_n_2.png", 500));

		viewer.setDrawable(frames2);

		JButton button = new JButton("Change animations");
		button.addActionListener(new ActionListener() {
			boolean b;

			@Override
			public void actionPerformed(ActionEvent e) {
				viewer.setDrawable(b ? frames2 : frames3);
				b = !b;
			}
		});

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(viewer.getCanvas(), BorderLayout.CENTER);
		panel.add(button, BorderLayout.SOUTH);
		return panel;
	}
}
