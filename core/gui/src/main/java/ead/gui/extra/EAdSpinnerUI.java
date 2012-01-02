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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

import ead.gui.EAdButton;
import ead.gui.EAdGUILookAndFeel;

public class EAdSpinnerUI extends BasicSpinnerUI {

    public static ComponentUI createUI(JComponent c) {
    	
        c.setBackground(EAdGUILookAndFeel.getBackgroundColor());
        c.setBorder(null);
        
        return new EAdSpinnerUI();
    }
    
    public EAdSpinnerUI() {
        super();
    }
    
    @Override
    public void installUI(JComponent c) {
    	super.installUI(c);
    	EAdBorder border = new EAdBorder();
    	spinner.getEditor().setBorder(border);
		EAdBorderListener eAdButtonListener = new EAdBorderListener( border, spinner );
		spinner.addFocusListener(eAdButtonListener);
		spinner.addMouseListener(eAdButtonListener);
		spinner.addPropertyChangeListener("enabled", eAdButtonListener);
    }
    
    protected void installDefaults() {
        spinner.setLayout(createLayout());
        LookAndFeel.installColorsAndFont(spinner, "Spinner.background",
                "Spinner.foreground", "Spinner.font");
        LookAndFeel.installProperty(spinner, "opaque", Boolean.TRUE);
    }
    

    @Override
    protected JButton createNextButton() {
    	EAdButton button = new EAdButton(EAdButton.NORTH);
    	installNextButtonListeners(button);
    	return button;
    }

    @Override
    protected JButton createPreviousButton() {
    	EAdButton button = new EAdButton(EAdButton.SOUTH);
    	installPreviousButtonListeners(button);
    	return button;
    } 
}
