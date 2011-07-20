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
import es.eucm.eadventure.engine.core.gameobjects.GameObjectManager;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.gameobjects.huds.impl.ActionsHUDImpl;
import es.eucm.eadventure.engine.core.platform.GUI;

@Singleton
public class DesktopActionsHUDImpl extends ActionsHUDImpl {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger("DesktopActionsHUDImpl");

	/**
	 * List of action game objects
	 */
	private List<GameObject<?>> actionGOs;

	/**
	 * Game object factory
	 */
	private GameObjectFactory gameObjectFactory;

	@Inject
	public DesktopActionsHUDImpl(GUI gui, GameObjectFactory gameObjectFactory,
			GameObjectManager gameObjectManager) {
		super(gui, gameObjectManager);
		actionGOs = new ArrayList<GameObject<?>>();
		this.gameObjectFactory = gameObjectFactory;
		logger.info("New instance");
	}

	@Override
	public void setElement(SceneElementGO<?> reference) {
		super.setElement(reference);
		actionGOs.clear();
		int i = 0;
		double[] angles = getStartEndAngles();
		float scale = (float) (0.5f / Math.sqrt(getActions().size()) + 0.5f) ;
		for (EAdAction eAdAction : this.getActions()) {
			EAdBasicSceneElement action = new EAdBasicSceneElement("action");
			i++;

			double angle = angles[0]
					+ ((angles[1] - angles[0]) / (getActions().size() + 1)) * i;

			int x = (int) (radius * Math.sin(angle));
			int y = - (int) (radius * Math.cos(angle));
			action.setPosition(new EAdPosition(EAdPosition.Corner.CENTER, this
					.getX() + x, this.getY() + y));
			action.setScale(scale);

			// TODO null?
			EAdActorActionsEffect e = new EAdActorActionsEffect("", null,
					Change.HIDE_ACTIONS);
			action.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, e);
			action.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, e);
			action.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK,
					eAdAction.getEffects());
			action.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK,
					eAdAction.getEffects());

			AssetDescriptor asset = eAdAction.getAsset(
					eAdAction.getInitialBundle(), EAdBasicAction.appearance);
			action.getResources().addAsset(action.getInitialBundle(),
					EAdBasicSceneElement.appearance, asset);

			if (eAdAction.getResources().getBundles()
					.contains(eAdAction.getHighlightBundle())) {
				action.getResources().addBundle(eAdAction.getHighlightBundle());
				action.getResources().addAsset(
						eAdAction.getHighlightBundle(),
						EAdBasicSceneElement.appearance,
						eAdAction.getAsset(eAdAction.getHighlightBundle(),
								EAdBasicAction.appearance));
				action.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED,
						new EAdChangeAppearance("action_mouseEnter", action,
								eAdAction.getHighlightBundle()));
			} else
				action.getResources().addAsset(eAdAction.getHighlightBundle(),
						EAdBasicSceneElement.appearance, asset);

			action.addBehavior(
					EAdMouseEventImpl.MOUSE_EXITED,
					new EAdChangeAppearance("action_mouseExit", action, action
							.getInitialBundle()));
			actionGOs.add(gameObjectFactory.get(action));
		}
	}

	public double[] getStartEndAngles() {
		double[] angles = new double[] { - Math.PI, Math.PI };

		if (this.getY() + radius > 600) {
			double h = 600 - this.getY();
			double a = Math.asin(h / radius);
			angles = new double[] { -Math.PI / 2 - a, Math.PI / 2 + a };
		}

		if (this.getY() - radius < 0) {
			double h = this.getY();
			double a = Math.asin(h / radius);
			angles = new double[] { a, Math.PI / 2 + a };
		}

		//TODO check offset
		if (this.getX() - radius < 0) {
			double w = this.getX();
			double a = Math.acos(w / radius);
			angles = new double[] { Math.max(-a, angles[0]), Math.min(Math.PI + a, angles[1]) };
		}
		
		//TODO check offset
		if (this.getX() + radius > 800) {
			double w = 800 - this.getX();
			double a = Math.acos(w / radius);
			angles = new double[] { Math.max(-a, angles[0]), Math.min(Math.PI + a, angles[1]) };
		}
		
		logger.info("Action angles: " + angles[0] + "; " + angles[1]);
		
		return angles;
	}

	@Override
	public void doLayout(int offsetX, int offsetY) {
		// TODO ...
		for (GameObject<?> action : actionGOs)
			gui.addElement(action, offsetX, offsetY);
	}

}
