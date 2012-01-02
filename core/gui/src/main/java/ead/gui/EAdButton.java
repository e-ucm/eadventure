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

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Button for the eAdventure GUI.
 */
public class EAdButton extends JButton {

	private static final long serialVersionUID = 3364781023045177824L;

	private static final Logger logger = LoggerFactory.getLogger(EAdButton.class);
	
	public EAdButton( String string ) {
        super(string);
    }
    
    public EAdButton( String string, Icon icon) {
        super(string, icon);
    }
    
    public EAdButton( ImageIcon icon ) {
        super(icon);
    }
    
    public EAdButton(int orientation) {
        super();
        
        InputStream is = getIconInputStream(orientation);

        try {
            super.setIcon( new ImageIcon(ImageIO.read(is)));
        } catch( IOException e ) {
        	logger.error("Cannot load icons", e);
        }
    }
    
    /**
     * Get one of the default icons
     * 
     * @param orientation Orientation of the button
     * @return The inputStream for the icon
     */
    private InputStream getIconInputStream(int orientation) {
    	switch(orientation) {
    	case JButton.NORTH:
    		return ClassLoader.getSystemResourceAsStream( R.Drawable.arrow_north_png );
    	case JButton.SOUTH:
    		return ClassLoader.getSystemResourceAsStream( R.Drawable.arrow_south_png );
    	case JButton.EAST:
    		return ClassLoader.getSystemResourceAsStream( R.Drawable.arrow_east_png );
    	case JButton.WEST:
    		return ClassLoader.getSystemResourceAsStream( R.Drawable.arrow_west_png );
    	case JButton.PREVIOUS:
    		return ClassLoader.getSystemResourceAsStream( R.Drawable.previous_png );
    	case JButton.NEXT:
    		return ClassLoader.getSystemResourceAsStream( R.Drawable.next_png );
    	}
    	return null;
    }

}
