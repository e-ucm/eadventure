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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import ead.utils.i18n.Resource;

/**
 * Button for the eAdventure GUI.
 */
public class EAdButton extends JButton {

    private static final long serialVersionUID = 3364781023045177824L;

    public EAdButton(String string) {
        super(string);
    }

    public EAdButton(String string, Icon icon) {
        super(string, icon);
    }

    public EAdButton(ImageIcon icon) {
        super(icon);
    }

    public EAdButton(int orientation) {
        super();

        String iconName = null;
        switch (orientation) {
            case JButton.NORTH:
                iconName = R.Drawable.arrow_north_png; break;
            case JButton.SOUTH:
                iconName = R.Drawable.arrow_south_png; break;
            case JButton.EAST:
                iconName = R.Drawable.arrow_east_png; break;
            case JButton.WEST:
                iconName = R.Drawable.arrow_west_png; break;
            case JButton.PREVIOUS:
                iconName = R.Drawable.previous_png; break;
            case JButton.NEXT:
                iconName = R.Drawable.next_png; break;
            default:
                throw new IllegalArgumentException("Bad orientation: " + orientation);
        }

        setIcon(new ImageIcon(Resource.loadImage(iconName)));
    }
}
