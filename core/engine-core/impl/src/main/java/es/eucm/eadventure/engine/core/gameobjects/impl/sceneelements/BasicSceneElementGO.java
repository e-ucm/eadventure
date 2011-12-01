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

package es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements;

import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.evaluators.EvaluatorFactory;
import es.eucm.eadventure.engine.core.gameobjects.factories.EventGOFactory;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

public class BasicSceneElementGO extends
		SceneElementGOImpl<EAdBasicSceneElement> {

	private static final Logger logger = Logger
			.getLogger("BasicSceneElementGOImpl");

	private EvaluatorFactory evaluatorFactory;

	@Inject
	public BasicSceneElementGO(AssetHandler assetHandler,
			StringHandler stringHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EvaluatorFactory evaluatorFactory,
			EventGOFactory eventFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				eventFactory);
		logger.info("New instance");
		this.evaluatorFactory = evaluatorFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.SceneElementGOImpl#
	 * getDraggableElement(es.eucm.eadventure.engine.core.MouseState)
	 */
	public EAdSceneElementDef getDraggableElement(MouseState mouseState) {
		if (evaluatorFactory.evaluate(element.getDragCond())){
			return element;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.SceneElementGOImpl#
	 * processAction(es.eucm.eadventure.engine.core.guiactions.GUIAction)
	 */
	@Override
	public boolean processAction(GUIAction action) {
		EAdList<EAdEffect> list = element.getEffects(action.getGUIEvent());
		boolean processed = addEffects(list, action);
		if (element.getDefinition() != element) {
			list = element.getDefinition().getEffects(action.getGUIEvent());
			processed |= addEffects(list, action);
		}
		return processed;

	}

	private boolean addEffects(EAdList<EAdEffect> list, GUIAction action) {
		if (list != null && list.size() > 0) {
			action.consume();
			for (EAdEffect e : list) {
				gameState.addEffect(e, action, getElement());
			}
			return true;
		}
		return false;
	}

}
