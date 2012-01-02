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

package ead.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * A panel that has a border with an optional title.
 */
public class EAdBorderedPanel extends JPanel {

	private static final long serialVersionUID = -5869276827319708715L;
	
	public EAdBorderedPanel() {
		this(null);
	}
	
	/**
	 * @param title The title of the panel
	 */
	public EAdBorderedPanel(String title) {
	    Color c = EAdGUILookAndFeel.getForegroundColor();
	    if (title != null) {
			setBorder( BorderFactory.createTitledBorder( 
					BorderFactory.createLineBorder(new Color(c.getRed(), c.getGreen(), c.getBlue(), 60)), title ));
	    } else {
			setBorder( BorderFactory.createCompoundBorder( 
					BorderFactory.createLineBorder(new Color(c.getRed(), c.getGreen(), c.getBlue(), 60)),
					BorderFactory.createEmptyBorder(2, 2, 2, 2)));
	    }
        setBackground(EAdGUILookAndFeel.getBackgroundColor());
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 * 
	 * Overriden to allow for antialiasing of title text
	 */
	@Override
	public void paint(Graphics g) {
	    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    super.paint(g);
	}
}
