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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ead.common.params.fills.ColorFill;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.animation.Frame;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.common.resources.assets.drawable.basics.shapes.BalloonShape;
import ead.common.resources.assets.drawable.basics.shapes.extra.BalloonType;
import ead.engine.core.gdx.desktop.utils.assetviewer.AssetViewer;

public class MainAssetViewer {

	public static void main(String args[]) {

		final Image standNorth = new Image("@drawable/man_stand_n.png");
		final BalloonShape shape = new BalloonShape(10, 10, 100, 100,
				BalloonType.CLOUD);
		shape.setPaint(ColorFill.WHITE);

		FramesAnimation frames2 = new FramesAnimation();
		frames2.addFrame(new Frame("@drawable/man_walk_w_1.png", 500));
		frames2.addFrame(new Frame("@drawable/man_walk_w_2.png", 500));
		JFrame frame = new JFrame();

		AssetViewer viewer1 = new AssetViewer();
		AssetViewer viewer8 = new AssetViewer();
		AssetViewer viewer3 = new AssetViewer();
		AssetViewer viewer4 = new AssetViewer();
		AssetViewer viewer5 = new AssetViewer();
		AssetViewer viewer6 = new AssetViewer();
		AssetViewer viewer7 = new AssetViewer();
		final AssetViewer viewer2 = new AssetViewer(viewer1.getLwjglAWTCanvas());

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

		JPanel panel = new JPanel(new GridLayout(4, 2));
		panel.add(viewer1.getCanvas());
		panel.add(viewer2.getCanvas());
		panel.add(viewer3.getCanvas());
		panel.add(viewer4.getCanvas());
		panel.add(viewer5.getCanvas());
		panel.add(viewer6.getCanvas());
		panel.add(viewer7.getCanvas());
		panel.add(viewer8.getCanvas());

		frame.getContentPane().setLayout(new BorderLayout());

		frame.getContentPane().add(panel, BorderLayout.CENTER);

		JButton button = new JButton("Change");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				viewer2.setDrawable(shape);
			}

		});

		frame.getContentPane().add(button, BorderLayout.SOUTH);

		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
