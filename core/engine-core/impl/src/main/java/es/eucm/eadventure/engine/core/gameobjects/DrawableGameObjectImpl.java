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

package es.eucm.eadventure.engine.core.gameobjects;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.game.GameState;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.go.DrawableGO;
import es.eucm.eadventure.engine.core.gameobjects.go.SceneElementGO;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.util.EAdTransformation;
import es.eucm.eadventure.engine.core.util.EAdTransformationImpl;

public abstract class DrawableGameObjectImpl<T extends EAdElement> extends
		GameObjectImpl<T> implements DrawableGO<T> {
	
	protected SceneElementGOFactory sceneElementFactory;
	
	/**
	 * The game's asset handler
	 */
	protected AssetHandler assetHandler;

	/**
	 * The string handler
	 */
	protected StringHandler stringsHandler;

	protected GUI gui;
	
	protected EAdTransformationImpl transformation;

	protected boolean enable;

	public DrawableGameObjectImpl(AssetHandler assetHandler,
			StringHandler stringsHandler, SceneElementGOFactory sceneElementFactory,
			GUI gui, GameState gameState) {
		super(gameState);
		this.assetHandler = assetHandler;
		this.stringsHandler = stringsHandler;
		this.sceneElementFactory = sceneElementFactory;
		this.gui = gui;
	}

	@Override
	public void setElement(T element) {
		super.setElement(element);
		transformation = new EAdTransformationImpl();
	}

	public boolean isEnable() {
		return enable;
	}
	
	public EAdTransformation getTransformation() {
		return transformation;
	}
	
	@Override
	public SceneElementGO<?> getDraggableElement() {
		return null;
	}

}
