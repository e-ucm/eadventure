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

package ead.gui.extra;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxUI;

import ead.gui.EAdGUILookAndFeel;
import ead.gui.R;
import ead.utils.i18n.Resource;

public class EAdCheckBoxUI extends BasicCheckBoxUI {

	public EAdCheckBoxUI() {
		super();
	}

	public static ComponentUI createUI(JComponent c) {
		return new EAdCheckBoxUI();
	}

	public void installUI(JComponent c) {
		super.installUI(c);

		JCheckBox radioButton = (JCheckBox) c;

        ImageIcon i;
        i = new ImageIcon(Resource.loadImage(R.Drawable.box_empty_png));
        radioButton.setIcon( i );
        radioButton.setDisabledIcon( i );
        radioButton.setPressedIcon( i );

        i = new ImageIcon(Resource.loadImage(R.Drawable.box_checked_png));
        radioButton.setDisabledSelectedIcon( i );
        radioButton.setSelectedIcon( i );

        radioButton.setBackground(radioButton.isEnabled() ?
                EAdGUILookAndFeel.getBackgroundColor() :
                EAdGUILookAndFeel.getDisabledColor());
        radioButton.setForeground(EAdGUILookAndFeel.getForegroundColor());

        radioButton.setFocusPainted( false );
        radioButton.setBorderPainted( true );

        EAdBorder border = new EAdBorder(radioButton);
        radioButton.setBorder( border );

        EAdBorderListener eAdButtonListener
                = new EAdBorderListener( border, radioButton );
        radioButton.addMouseListener(eAdButtonListener);
        radioButton.addFocusListener(eAdButtonListener);
        radioButton.addActionListener(eAdButtonListener);
        radioButton.addPropertyChangeListener("enabled", eAdButtonListener);
	}

	@Override
	public Dimension getPreferredSize(JComponent c) {
		JCheckBox r = (JCheckBox) c;
	    r.setBorderPainted( true );
    	FontMetrics fm = c.getFontMetrics(c.getFont());
        return new Dimension(fm.stringWidth(r.getText())
                + r.getIcon().getIconWidth() + 16 + 2 * EAdBorder.BORDER,
				(int) (1.2f*fm.getHeight()) + 4 + EAdBorder.BORDER);
	}

	@Override
	public void paint(Graphics g, JComponent c)  {
        ((Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paint(g, c);
    }
}
