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
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Injector;

import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.AbstractEventGO;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public abstract class AbstractGameObject<T extends EAdElement> implements GameObject<T> {

	/**
	 * The game's asset handler
	 */
	@Inject
	protected AssetHandler assetHandler;

	/**
	 * The string handler
	 */
	@Inject
	protected StringHandler stringHandler;

	@Inject
	protected GameObjectFactory gameObjectFactory;

	@Inject
	protected GUI gui;

	@Inject
	protected Injector injector;

	@Inject
	protected GameState gameState;
	
	@Inject
	protected ValueMap valueMap;
	
	protected T element;

	private ArrayList<AbstractEventGO<?>> eventGOList;
	
	@Inject
	public AbstractGameObject() {
	}

	@Override
	public boolean processAction(GUIAction action) {
		return false;
	}

	@Override
	public void setElement(T element) {
		this.element = element;
		eventGOList = new ArrayList<AbstractEventGO<?>>();
		if (element.getEvents() != null) {
			for (EAdEvent event : element.getEvents()) {
				AbstractEventGO<?> eventGO = (AbstractEventGO<?>) gameObjectFactory.get(event);
				eventGO.initialize();
				eventGOList.add(eventGO);
			}
		}
	}
	
	@Override
	public T getElement( ){
		return element;
	}

	@Override
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		// Implemented by inherited classes
		return null;
	}

	@Override
	public void doLayout(int offsetX, int offsetY) {
		// Implemented by inherited classes

	}

	@Override
	public void update(GameState state) {
		if (eventGOList != null)
			for (AbstractEventGO<?> eventGO : eventGOList)
				eventGO.update(state);
	}

	@Override
	public EAdPosition getPosition() {
		// Implemented by inherited classes
		return null;
	}
	
	public EAdBundleId getCurrentBundle() {
		EAdBundleId current = valueMap.getValue((EAdElement) element, EAdBundleId.class);
		if (current == null) {
			current = element.getInitialBundle();
			valueMap.setValue((EAdElement) element, current);
		}
		return current;
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		return assetList;
	}
	
	public String toString( ){
		return  getClass().getSimpleName() + ": " + element.getId();
	}

}
