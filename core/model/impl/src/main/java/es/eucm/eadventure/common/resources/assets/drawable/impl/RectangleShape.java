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

package es.eucm.eadventure.common.resources.assets.drawable.impl;

import es.eucm.eadventure.common.Param;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.resources.assets.drawable.Shape;

/**
 * <p>Rectangular shape asset</p>
 * 
 */
public class RectangleShape implements Shape {

	@Param("color")
	private EAdBorderedColor color;

	@Param("width")
	private int width;
	
	@Param("height")
	private int height;
	
	@Param("borderWidth")
	private int borderWidth;
	
	public RectangleShape() {
		this(100, 100);
	}

	public RectangleShape(int width, int height) {
		this(width, height, EAdBorderedColor.TRANSPARENT);
	}
	
	public RectangleShape(int width, int height, EAdBorderedColor color) {
		this.color = color;
		this.width = width;
		this.height = height;
		borderWidth = 1;
	}


	/* (non-Javadoc)
	 * @see es.eucm.eadventure.common.resources.assets.drawable.Shape#getColor()
	 */
	@Override
	public EAdBorderedColor getColor() {
		return color;
	}
	
	public void setColor(EAdBorderedColor color) {
		this.color = color;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getBorderWidth() {
		return borderWidth;
	}
	
	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}
	
}
