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
package ead.utils.i18n;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Resource-loading static utilities. Provides simple access to resources, acting
 * in tandem with the ResourceCreator and R classes
 * @author mfreire
 */
public class Resource {

	private static final Logger logger = Logger.getLogger("Resource");

    private static BufferedImage placeholderImage;
    private static Font placeholderFont;

    /**
     * Returns a placeholder image, used when no image is found
     */
    public static Image placeholderImage() {
        if (placeholderImage == null) {
            // currently a 32x32 pixmap with a red '?' on a white field
            placeholderImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
            Graphics g = placeholderImage.getGraphics();
            g.setColor(Color.red);
            g.drawString("?", 5, 30);
            g.dispose();
        }
        return placeholderImage;
    }

    /**
     * Returns a placeholder font, used when no font is found
     */
    public static Font placeholderFont() {
        if (placeholderFont == null) {
            placeholderFont = (new JPanel()).getFont();
        }
        return placeholderFont;
    }

    /**
     * Loads an image resource; logs an exception if not possible.
     * @param resourceName name of resource (typically R.Drawable.something)
     */
    public static Image loadImage(String resourceName) {
        try {
            return ImageIO.read(ClassLoader.getSystemResourceAsStream(resourceName));
        } catch (IOException e) {
            logger.log(Level.WARNING, "Image not found: ''{0}''", resourceName);
            return placeholderImage();
        }
    }

    /**
     * Loads a font resource; logs an exception if not possible
     * @param resourceName name of resource (must be available in class-path)
     */
    public static Font loadFont(String resourceName) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT,
                ClassLoader.getSystemResourceAsStream(resourceName));
        } catch (IOException e) {
            logger.log(Level.WARNING, "Font not found: ''{0}''", resourceName);
        } catch (FontFormatException ffe) {
            logger.log(Level.WARNING, "Bad font format: ''{0}''", resourceName);
        }
        return placeholderFont();
    }
}
