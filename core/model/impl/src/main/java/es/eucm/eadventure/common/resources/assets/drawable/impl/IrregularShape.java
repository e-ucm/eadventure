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

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.impl.EAdListImpl;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdPosition;

/**
 * <p>Rectangular shape asset</p>
 * 
 */
public class IrregularShape extends BezierShape {

	@Param("color")
	private EAdBorderedColor color;
	
	private EAdList<EAdPosition> positions;
	
	@Param("borderWidth")
	private int borderWidth;
	
	public IrregularShape() {
		color = EAdBorderedColor.TRANSPARENT;
		borderWidth = 1;
		positions = new EAdListImpl<EAdPosition>(EAdPosition.class);
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

	@Override
	public int getBorderWidth() {
		return borderWidth;
	}
	
	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}
	
	public EAdList<EAdPosition> getPositions() {
		return positions;
	}
	
}
