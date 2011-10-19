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

package es.eucm.eadventure.engine.core.gameobjects.impl;

import java.util.ArrayList;

import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.Renderable;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.AbstractEventGO;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

public abstract class DrawableGameObject<T extends EAdSceneElement> extends
		GameObjectImpl<T> implements Renderable {

	public static final EAdVarDef<EAdBundleId> VAR_BUNDLE_ID = new EAdVarDefImpl<EAdBundleId>(
			"bundle_id", EAdBundleId.class, null);

	private ArrayList<AbstractEventGO<?>> eventGOList;

	public DrawableGameObject(AssetHandler assetHandler,
			StringHandler stringsReader, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState);
	}

	@Override
	public void setElement(T element) {
		super.setElement(element);
		gameState.getValueMap().setValue(element, VAR_BUNDLE_ID, element.getInitialBundle());
		eventGOList = new ArrayList<AbstractEventGO<?>>();
		if (element.getEvents() != null) {
			for (EAdEvent event : element.getEvents()) {
				AbstractEventGO<?> eventGO = (AbstractEventGO<?>) gameObjectFactory
						.get(event);
				eventGO.setParent( element );
				eventGO.initialize();
				eventGOList.add(eventGO);
			}
		}
	}

	@Override
	public void update() {
		super.update();
		if (eventGOList != null)
			for (AbstractEventGO<?> eventGO : eventGOList)
				eventGO.update();
	}

	public EAdBundleId getCurrentBundle() {
		EAdBundleId current = gameState.getValueMap().getValue(element, VAR_BUNDLE_ID);
		if (current == null) {
			current = element.getInitialBundle();
			gameState.getValueMap().setValue(element, VAR_BUNDLE_ID, current);
		}
		return current;
	}

	public void setCurrentBundle(EAdBundleId bundle) {
		gameState.getValueMap().setValue(element, VAR_BUNDLE_ID, bundle);
	}

	public String toString() {
		return element + " GO";
	}
	
	public boolean isEnable(){
		return true;
	}

}
