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

package es.eucm.eadventure.gui.structurepanel.extra;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;

import es.eucm.eadventure.gui.EAdGUILookAndFeel;
import es.eucm.eadventure.gui.structurepanel.StructurePanel;

/**
 * The selected element button (black background, white letters)
 */
public class SelectedElementButton extends JButton {

	private static final long serialVersionUID = -2199667397557966339L;

	public SelectedElementButton(String label, Icon icon, StructurePanel panel,
			int index, SelectedElementPanel selectedElementPanel) {
		super(label, icon);

		setForeground(EAdGUILookAndFeel.getBackgroundColor());
		setBackground(EAdGUILookAndFeel.getForegroundColor());

		setHorizontalAlignment(SwingConstants.LEFT);

		setBorder(new SelectedBorder());

		Font font = this.getFont().deriveFont(Font.BOLD).deriveFont(18.0f);
		setFont(font);
		setContentAreaFilled(true);

		setFocusable(false);

		ElementButtonListener elementListener = new ElementButtonListener(
				panel, selectedElementPanel, index);
		addActionListener(elementListener);
		addMouseListener(elementListener);
		addMouseMotionListener(elementListener);
	}

	/**
	 * Border for the selected element
	 */
	private class SelectedBorder extends AbstractBorder {

		private static final long serialVersionUID = 4669318117131018414L;

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int w,
				int h) {
			Shape clip = g.getClip();
			Area a = new Area(new RoundRectangle2D.Float(x, y, w - 1, h - 1, 6,
					6));
			Area b = new Area(clip);
			b.subtract(a);
			g.setClip(b);

			g.setColor(c.getForeground());

			g.fillRect(0, 0, 6, 6);
			g.fillRect(w - 6, 0, 6, 6);
			g.fillRect(0, h - 6, 6, 6);
			g.fillRect(w - 6, h - 6, 6, 6);

			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_DITHERING,
					RenderingHints.VALUE_DITHER_ENABLE);
			// TODO check if -1 can be removed for the height an the line of the
			// last element added excplicitly

			g.setColor(c.getBackground());

			g.drawRoundRect(x, y, w - 1, h - 1, 6, 6);

			g.setClip(clip);
		}

	}
}
