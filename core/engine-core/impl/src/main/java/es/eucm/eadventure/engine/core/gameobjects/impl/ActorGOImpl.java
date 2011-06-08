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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.engine.core.EvaluatorFactory;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.ActorGO;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public class ActorGOImpl extends AbstractGameObject<EAdActor> implements
		ActorGO {

	/**
	 * The logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ActorGOImpl.class);

	private EvaluatorFactory evaluator;

	@Inject
	public ActorGOImpl(EvaluatorFactory evaluator, ValueMap valueMap ) {
		logger.info("New instance");
		this.evaluator = evaluator;
		this.valueMap = valueMap;
	}

	@Override
	public boolean processAction(GUIAction action) {
		EAdElementList<EAdEffect> list = element.getEffects(action.getGUIEvent());
		if (list != null && list.size() > 0) {
			action.consume();
			for (EAdEffect e : element.getEffects(action.getGUIEvent())) {
				gameState.addEffect(e);
			}
			return true;
		}
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
	public void update(GameState state) {
		super.update(state);
		RuntimeAsset<?> asset = assetHandler.getRuntimeAsset(
				getElement(), getCurrentBundle(),
				EAdBasicActor.appearance);
		if (asset != null) {
			asset.update(state);
		}
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		return assetList;
	}
	


}
