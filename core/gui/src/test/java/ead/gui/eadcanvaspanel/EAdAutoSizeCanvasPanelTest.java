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

package ead.gui.eadcanvaspanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ead.gui.eadcanvaspanel.EAdCanvasPanel;
import ead.gui.eadcanvaspanel.listeners.DragListener;
import ead.gui.eadcanvaspanel.listeners.ResizeListener;
import ead.gui.eadcanvaspanel.scrollcontainers.EAdAutosizeScrollCanvasPanel;

public class EAdAutoSizeCanvasPanelTest {

	public static void main(String args[]) {
		JFrame f = new JFrame("Autosize");
		EAdAutosizeScrollCanvasPanel scroll = new EAdAutosizeScrollCanvasPanel();

		EAdCanvasPanel dragPanel = scroll.getCanvas();

		DragListener listener = new ResizeListener(dragPanel);

		JComponent j = new JComponent() {

			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			}

		};
		j.setBounds(0, 0, 50, 50);
		j.addKeyListener(listener);
		j.addMouseListener(listener);
		j.addMouseMotionListener(listener);

		dragPanel.add(j);
		JButton b = new JButton("Botón");
		b.setBounds(new Rectangle(0, 0, 100, 30));
		dragPanel.add(b);
		b.addKeyListener(listener);
		b.addMouseListener(listener);
		b.addMouseMotionListener(listener);

		JLabel label = new JLabel("Label");
		label.addKeyListener(listener);
		label.addMouseListener(listener);
		label.addMouseMotionListener(listener);
		label.setBounds(100, 100, 100, 20);
		dragPanel.add(label);

		f.getContentPane().add(scroll, BorderLayout.CENTER);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		f.setVisible(true);
	}
}
