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

import es.eucm.eadventure.common.StringsReader;
import es.eucm.eadventure.common.model.elements.EAdGeneralElement;
import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.common.model.variables.EAdVar;
import es.eucm.eadventure.common.model.variables.impl.EAdVarImpl;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.impl.events.AbstractEventGO;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public abstract class AbstractGameObject<T extends EAdGeneralElement> extends
		GameObjectImpl<T> {
	
	private EAdVar<EAdBundleId> bundleVar;
	
	private ArrayList<AbstractEventGO<?>> eventGOList;
	
	public AbstractGameObject(AssetHandler assetHandler,
			StringsReader stringsReader, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
	}

	@Override
	public void setElement(T element) {
		super.setElement( element );
		this.bundleVar = new EAdVarImpl<EAdBundleId>(EAdBundleId.class,
				"bundleId", element.getInitialBundle(), element);
		eventGOList = new ArrayList<AbstractEventGO<?>>();
		if (element.getEvents() != null) {
			for (EAdEvent event : element.getEvents()) {
				AbstractEventGO<?> eventGO = (AbstractEventGO<?>) gameObjectFactory
						.get(event);
				eventGO.initialize();
				eventGOList.add(eventGO);
			}
		}
	}

	@Override
	public void update(GameState state) {
		super.update(state);
		if (eventGOList != null)
			for (AbstractEventGO<?> eventGO : eventGOList)
				eventGO.update(state);
	}

	public EAdBundleId getCurrentBundle() {
		EAdBundleId current = valueMap.getValue(bundleVar);
		if (current == null) {
			current = element.getInitialBundle();
			valueMap.setValue(bundleVar, current);
		}
		return current;
	}
	
	public void setCurrentBundle(EAdBundleId bundle){
		valueMap.setValue(bundleVar, bundle);
	}

	public String toString() {
		return getClass().getName() + ": " + element.getId();
	}

}
