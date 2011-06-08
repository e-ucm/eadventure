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

package es.eucm.eadventure.gui.extra;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

import es.eucm.eadventure.gui.EAdGUILookAndFeel;

public class EAdButtonUI extends BasicButtonUI {

	public EAdButtonUI() {
		super();
	}
	
	public static ComponentUI createUI(JComponent c) {
		return new EAdButtonUI();
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		JButton button = (JButton) c;

		EAdBorder border = new EAdBorder(c);

		button.setBackground(EAdGUILookAndFeel.getBackgroundColor());
		button.setForeground(EAdGUILookAndFeel.getForegroundColor());

		button.setContentAreaFilled( false );
		button.setFocusPainted( false );
		
		button.setBorder( border );
		EAdButtonListener eAdButtonListener = new EAdButtonListener( border, button );
		button.addFocusListener(eAdButtonListener);
		button.addMouseListener(eAdButtonListener);
		button.addActionListener(eAdButtonListener);
		button.addPropertyChangeListener("enabled", eAdButtonListener);
	}

	public void paint(Graphics g, JComponent c)  {
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        ((Graphics2D) g).setRenderingHint( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE );
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		super.paint(g, c);
	}
}
