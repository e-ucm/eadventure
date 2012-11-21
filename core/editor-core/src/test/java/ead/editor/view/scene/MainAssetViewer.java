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

package ead.editor.view.scene;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ead.common.resources.assets.drawable.basics.animation.Frame;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.common.util.EAdURI;
import ead.engine.core.gdx.desktop.utils.assetviewer.AssetViewer;
import ead.engine.core.gdx.desktop.utils.assetviewer.AssetViewerModule;
import ead.engine.core.platform.assets.AssetHandler;
import ead.utils.Log4jConfig;
import ead.utils.swing.SwingUtilities;

public class MainAssetViewer {

	public static void main(String args[]) {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Debug,
				new Object[] {});
		SwingUtilities.doInEDTNow(new Runnable() {
			@Override
			public void run() {
				showSampleAsset();
			}
		});
	}

	public static void showSampleAsset() {

		File root = new File("../../demos/techdemo/src/main/resources/");
		if (!root.exists()) {
			System.err.println("resources not found");
			return;
		}

		System.err.println(new File(".").getAbsolutePath());

		Injector i = Guice.createInjector(new AssetViewerModule());
		AssetHandler ah = i.getInstance(AssetHandler.class);
		ah.setCacheEnabled(false);
		ah.setResourcesLocation(new EAdURI(new File(
				"../../demos/techdemo/src/main/resources/").getPath()));
		final AssetViewer viewer = i.getInstance(AssetViewer.class);

		final FramesAnimation frames2 = new FramesAnimation();
		frames2.addFrame(new Frame("@drawable/man_walk_w_1.png", 100));
		frames2.addFrame(new Frame("@drawable/man_walk_w_2.png", 500));
		final FramesAnimation frames3 = new FramesAnimation();
		frames3.addFrame(new Frame("@drawable/man_walk_n_1.png", 500));
		frames3.addFrame(new Frame("@drawable/man_walk_n_2.png", 100));

		viewer.setDrawable(frames2);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(viewer.getCanvas(), BorderLayout.CENTER);

		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(panel, BorderLayout.CENTER);

		JButton button = new JButton("Change animations");
		button.addActionListener(new ActionListener() {
			boolean b;

			@Override
			public void actionPerformed(ActionEvent e) {
				viewer.setDrawable(b ? frames3 : frames2);
				b = !b;
			}
		});

		frame.getContentPane().add(button, BorderLayout.SOUTH);

		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
