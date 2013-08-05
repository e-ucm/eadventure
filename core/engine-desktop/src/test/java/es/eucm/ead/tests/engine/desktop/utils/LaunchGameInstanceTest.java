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

package es.eucm.ead.tests.engine.desktop.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import es.eucm.ead.model.elements.BasicAdventureModel;
import es.eucm.ead.model.elements.BasicChapter;
import es.eucm.ead.model.elements.scenes.BasicScene;
import es.eucm.ead.engine.desktop.DesktopGame;

/**
 * Test to launch several instances of the engine
 *
 */
public class LaunchGameInstanceTest {

	private static DesktopGame game = null;

	public static void main(String args[]) {
		final BasicAdventureModel model = new BasicAdventureModel();
		BasicChapter chapter1 = new BasicChapter();
		model.getChapters().add(chapter1);
		BasicScene scene = new BasicScene();
		chapter1.setInitialScene(scene);

		JButton b = new JButton("Launch engine");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game != null) {
					game.exit();
				}
				game = new DesktopGame(false);
				game.start();
			}

		});

		JFrame frame = new JFrame("Frame not closing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200, 200);
		frame.getContentPane().add(b);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
