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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;

import es.eucm.eadventure.gui.EAdGUILookAndFeel;

public class EAdToggleButtonUI extends BasicToggleButtonUI {

	private EAdBorder border;
	
	public EAdToggleButtonUI() {
		super();
	}
	
	public static ComponentUI createUI(JComponent c) {
		return new EAdToggleButtonUI();
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		JToggleButton button = (JToggleButton) c;

		border = new EAdBorder(c);

		button.setBackground(EAdGUILookAndFeel.getBackgroundColor());
		button.setForeground(EAdGUILookAndFeel.getForegroundColor());

		button.setContentAreaFilled( false );
		button.setFocusPainted( false );
		button.setBackground( EAdGUILookAndFeel.getBackgroundColor() );
		button.setBorder( border );
		EAdButtonListener eAdButtonListener = new EAdButtonListener( border, button );
		button.addFocusListener(eAdButtonListener);
		button.addMouseListener(eAdButtonListener);
		button.addActionListener(eAdButtonListener);
		button.addPropertyChangeListener("enabled", eAdButtonListener);
	}
	
	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
		
		if (((JToggleButton) c).isSelected()) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setColor(Color.BLUE);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			g2.fillRoundRect(EAdBorder.BORDER - border.getDepth(), 
					border.getDepth(), 
					c.getWidth() - EAdBorder.BORDER, 
					c.getHeight() - EAdBorder.BORDER, 6, 6);
			g2.dispose();
		}

	}
	
}
