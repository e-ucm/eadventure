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

package es.eucm.eadventure.engine.core.platform.impl.extra;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect.Change;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.ActionsHUDImpl;
import es.eucm.eadventure.engine.core.platform.GUI;

@Singleton
public class DesktopActionsHUDImpl extends ActionsHUDImpl {

	private static final Logger logger = Logger.getLogger("DesktopActionsHUDImpl");
	
	private List<GameObject<?>> actionGOs;
	
	private GameObjectFactory gameObjectFactory;
	
	@Inject
	public DesktopActionsHUDImpl(GUI<Component> gui,
			GameObjectFactory gameObjectFactory) {
		super(gui);
		actionGOs = new ArrayList<GameObject<?>>();
		this.gameObjectFactory = gameObjectFactory;
		logger.info("New instance");
	}

	@Override
	public void setElement(SceneElementGO<?> reference) {
		super.setElement(reference);
		actionGOs.clear();
		// FIXME Habrá que distribuir las acciones de alguna manera más exacta (o de varias)
		float radius = 50;
		int i = 0;
		for (EAdAction eAdAction : this.getActions()) {
			EAdBasicSceneElement action = new EAdBasicSceneElement("action");
			float angle = (float) (( 2 * Math.PI / getActions().size() ) * i);
			i++;
			
			int x = (int) (radius * Math.cos( angle ));
			int y = (int) (radius * Math.sin( angle ));
			action.setPosition(new EAdPosition(EAdPosition.Corner.CENTER, this.getX() + x, this.getY() + y));
			
			//TODO null?
			EAdActorActionsEffect e = new EAdActorActionsEffect("", null, Change.HIDE_ACTIONS);
			action.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, e);
			action.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, e);
			action.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, eAdAction.getEffects());
			action.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, eAdAction.getEffects());
			
			AssetDescriptor asset = eAdAction.getAsset(eAdAction.getInitialBundle(), EAdBasicAction.appearance);
			action.getResources().addAsset(action.getInitialBundle(), EAdBasicSceneElement.appearance, asset);

			action.getResources().addBundle(eAdAction.getHighlightBundle());
			
			action.getResources().addAsset(eAdAction.getHighlightBundle(), EAdBasicSceneElement.appearance,
					eAdAction.getAsset(eAdAction.getHighlightBundle(), EAdBasicAction.appearance));

			action.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, new EAdChangeAppearance("action_mouseEnter", action, eAdAction.getHighlightBundle()));
			action.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, new EAdChangeAppearance("action_mouseExit", action, action.getInitialBundle()));
			actionGOs.add(gameObjectFactory.get(action));
		}

	}
	
	@Override
	public void doLayout(int offsetX, int offsetY) {
		//TODO ...
		for (GameObject<?> action : actionGOs)
			gui.addElement(action, offsetX, offsetY);
	}


}
