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

package es.eucm.eadventure.engine.core.platform.assets.impl;

import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.common.resources.assets.drawable.impl.IrregularShape;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;

public class RuntimeIrregularShape implements DrawableAsset<IrregularShape> {

	protected IrregularShape shape;
	
	private int width;
	
	private int height;
	
	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#loadAsset()
	 */
	@Override
	public boolean loadAsset() {
		if (shape.getPositions().size() > 0) {
			int minX = shape.getPositions().get(0).getX();
			int minY = shape.getPositions().get(0).getY();
			int maxX = shape.getPositions().get(0).getX();
			int maxY = shape.getPositions().get(0).getY();
			for (EAdPosition pos : shape.getPositions()) {
				minX = Math.min(minX, pos.getX());
				minY = Math.min(minY, pos.getY());
				maxX = Math.max(maxX, pos.getX());
				maxY = Math.max(maxY, pos.getY());
			}
			width = maxX - minX;
			height = maxY - minY;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#freeMemory()
	 */
	@Override
	public void freeMemory() {
		//DO NOTHING
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#isLoaded()
	 */
	@Override
	public boolean isLoaded() {
		return width != 0 && height != 0;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#setDescriptor(es.eucm.eadventure.common.resources.assets.AssetDescriptor)
	 */
	@Override
	public void setDescriptor(IrregularShape descriptor) {
		this.shape = descriptor;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.platform.RuntimeAsset#update(es.eucm.eadventure.engine.core.GameState)
	 */
	@Override
	public void update(GameState state) {
		//DO NOTHING
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.platform.DrawableAsset#getWidth()
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.platform.DrawableAsset#getHeight()
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.platform.DrawableAsset#getDrawable()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <S extends Drawable> DrawableAsset<S> getDrawable() {
		return (DrawableAsset<S>) this;
	}

	public EAdBorderedColor getColor() {
		return shape.getColor();
	}

	public int getBorderWidth() {
		return shape.getBorderWidth();
	}

}
