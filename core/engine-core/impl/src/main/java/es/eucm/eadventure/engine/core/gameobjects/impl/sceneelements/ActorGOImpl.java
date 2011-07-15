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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.EvaluatorFactory;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.ActorGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public class ActorGOImpl extends SceneElementGOImpl<EAdActor> implements
		ActorGO {

	/**
	 * The logger
	 */
	private static final Logger logger = Logger
			.getLogger("ActorGOImpl");

	private EvaluatorFactory evaluator;
	
	private EAdActorReference currentReference;
	
	private EAdScene lastScene;

	@Inject
	public ActorGOImpl(AssetHandler assetHandler, StringHandler stringHandler,
			GameObjectFactory gameObjectFactory, GUI gui, GameState gameState,
			ValueMap valueMap, PlatformConfiguration platformConfiguration, EvaluatorFactory evaluator) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
		this.evaluator = evaluator;
		this.lastScene = null;
		logger.info("New instance");
	}

	@Override
	public boolean processAction(GUIAction action) {
		return false;
	}

	public List<EAdAction> getValidActions() {
		List<EAdAction> actions = new ArrayList<EAdAction>();
		if (element instanceof EAdBasicActor) {
			for (EAdAction action : ((EAdBasicActor) element).getActions())
				if (evaluator.evaluate(action.getCondition()))
					actions.add(action);
		}
		return actions;
	}

	@Override
	public EAdString getName() {
		return element.getName();
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		return assetList;
	}
	
	@Override
	public void update(GameState state) {
		super.update(state);
		if (lastScene != state.getScene()
				&& state.getScene() != null
				&& state.getScene().getElement() != null
				&& state.getScene().getElement().getSceneElements() != null) {
			for (EAdSceneElement se : state.getScene().getElement().getSceneElements()) {
				if (se != null 
						&& se instanceof EAdActorReference
						&& ((EAdActorReference) se).getReferencedActor() == element
						&& currentReference != se
						&& se != null) {
						currentReference = (EAdActorReference) se;
						valueMap.setValue(element.positionXVar(), valueMap.getValue(currentReference.positionXVar()));
						valueMap.setValue(element.positionYVar(), valueMap.getValue(currentReference.positionYVar()));
						valueMap.setValue(element.visibleVar(), valueMap.getValue(currentReference.visibleVar()));
						valueMap.setValue(element.rotationVar(), valueMap.getValue(currentReference.rotationVar()));
						valueMap.setValue(element.scaleVar(), valueMap.getValue(currentReference.scaleVar()));
						valueMap.setValue(element.alphaVar(), valueMap.getValue(currentReference.alphaVar()));
						valueMap.setValue(element.orientationVar(), valueMap.getValue(currentReference.orientationVar()));
						valueMap.setValue(element.stateVar(), valueMap.getValue(currentReference.stateVar()));
				}
			}
			lastScene = state.getScene().getElement();
		}
		
		if (currentReference != null) {
			valueMap.setValue(currentReference.positionXVar(), valueMap.getValue(element.positionXVar()));
			valueMap.setValue(currentReference.positionYVar(), valueMap.getValue(element.positionYVar()));
			valueMap.setValue(currentReference.visibleVar(), valueMap.getValue(element.visibleVar()));
			valueMap.setValue(currentReference.rotationVar(), valueMap.getValue(element.rotationVar()));
			valueMap.setValue(currentReference.scaleVar(), valueMap.getValue(element.scaleVar()));
			valueMap.setValue(currentReference.alphaVar(), valueMap.getValue(element.alphaVar()));
			valueMap.setValue(currentReference.orientationVar(), valueMap.getValue(element.orientationVar()));
			valueMap.setValue(currentReference.stateVar(), valueMap.getValue(element.stateVar()));
		}
	}

}
