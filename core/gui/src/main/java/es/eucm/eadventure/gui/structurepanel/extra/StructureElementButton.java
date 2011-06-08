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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;

import es.eucm.eadventure.gui.EAdGUILookAndFeel;

/**
 * The structure element default button
 */
public class StructureElementButton extends JButton {

	private static final long serialVersionUID = -4565262077680472274L;

	public StructureElementButton(String label, Icon icon) {
		super(label, icon);
		setHorizontalAlignment(SwingConstants.LEFT);
		setBorder(new RoundedBorder());
	}
	
	/**
	 * Rounded border for the structure element
	 */
	private class RoundedBorder extends AbstractBorder {
		
		private static final long serialVersionUID = 4669318117131018414L;

		@Override
	    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
    		g.setColor(EAdGUILookAndFeel.getForegroundColor());
            ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE );
            // TODO check if -1 can be removed for the height an the line of the last element added excplicitly
			g.drawRoundRect(x, y, w - 1, h - 1, 6, 6);
	    }
	}

}
