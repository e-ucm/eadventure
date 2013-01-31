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

package ead.engine.utils.test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.assets.drawable.basics.animation.Frame;
import ead.common.model.assets.drawable.basics.animation.FramesAnimation;
import ead.common.model.assets.drawable.basics.shapes.BalloonShape;
import ead.common.model.assets.drawable.basics.shapes.extra.BalloonType;
import ead.common.model.params.fills.ColorFill;
import ead.engine.core.gdx.desktop.utils.assetviewer.AssetViewer;
import ead.engine.core.gdx.desktop.utils.assetviewer.AssetViewer.ImageGrabber;
import ead.engine.core.gdx.desktop.utils.assetviewer.AssetViewerModule;
import ead.engine.core.platform.assets.AssetHandler;
import ead.utils.swing.SwingUtilities;

public class MainAssetViewer implements Runnable {

	public static void main(String args[]) throws Exception {
		SwingUtilities.doInEDT(new MainAssetViewer());
	}

	@Override
	public void run() {

		final Image standNorth = new Image("@drawable/man_stand_n.png");
		final BalloonShape shape = new BalloonShape(10, 10, 100, 100,
				BalloonType.CLOUD);
		shape.setPaint(ColorFill.WHITE);

		FramesAnimation frames2 = new FramesAnimation();
		frames2.addFrame(new Frame("@drawable/man_walk_w_1.png", 500));
		frames2.addFrame(new Frame("@drawable/man_walk_w_2.png", 500));

		Injector i = Guice.createInjector(new AssetViewerModule());
		AssetHandler ah = i.getInstance(AssetHandler.class);
		ah.setCacheEnabled(false);
		ah.setResourcesLocation(new File(
				"../../demos/techdemo/src/main/resources/").getPath());

		AssetViewer viewer1 = i.getInstance(AssetViewer.class);
		AssetViewer viewer8 = i.getInstance(AssetViewer.class);
		AssetViewer viewer3 = i.getInstance(AssetViewer.class);
		AssetViewer viewer4 = i.getInstance(AssetViewer.class);
		AssetViewer viewer5 = i.getInstance(AssetViewer.class);
		AssetViewer viewer6 = i.getInstance(AssetViewer.class);
		AssetViewer viewer7 = i.getInstance(AssetViewer.class);
		final AssetViewer viewer2 = i.getInstance(AssetViewer.class);

		viewer1.setDrawable(standNorth);
		viewer8.setDrawable(frames2);
		viewer3.setDrawable(standNorth);
		viewer4.setDrawable(standNorth);
		viewer5.setDrawable(shape);
		viewer6.setDrawable(standNorth);
		viewer7.setDrawable(frames2);

		FramesAnimation frames = new FramesAnimation();
		Frame standSouth = new Frame("@drawable/man_stand_s_1.png", 1000);
		frames.addFrame(standSouth);
		frames.addFrame(new Frame("@drawable/man_stand_s_2.png", 200));

		viewer2.setDrawable(frames);

		JPanel viewerPanel = new JPanel(new GridLayout(4, 2));
		viewerPanel.add(viewer1.getCanvas());
		viewerPanel.add(viewer2.getCanvas());
		viewerPanel.add(viewer3.getCanvas());
		viewerPanel.add(viewer4.getCanvas());
		viewerPanel.add(viewer5.getCanvas());
		viewerPanel.add(viewer6.getCanvas());
		viewerPanel.add(viewer7.getCanvas());
		viewerPanel.add(viewer8.getCanvas());

		JButton changeButton = new JButton("Change#2");
		changeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewer2.setDrawable(shape);
			}
		});

		JButton grabButton = new JButton("Grab#2");
		grabButton.addActionListener(new ActionListener() {
			int i = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				final ImageGrabber g = new ImageGrabber();
				g.setCallback(new Runnable() {
					@Override
					public void run() {
						g.writeToFile(new File("/tmp/f" + (++i) + ".png"));
					}
				});
				viewer2.grabImage(g);
			}
		});

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		buttonsPanel.add(changeButton);
		buttonsPanel.add(grabButton);

		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(viewerPanel, BorderLayout.CENTER);
		frame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
