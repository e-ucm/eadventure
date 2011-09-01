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

import es.eucm.eadventure.common.StringsReader;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.ActorGO;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public class ActorReferenceGOImpl extends SceneElementGOImpl<EAdActorReference>
		implements ActorReferenceGO {

	private static final Logger logger = Logger
			.getLogger("ActorReferneceGOImpl");

	protected ActorGO actor;

	private boolean inventoryReference;

	@Inject
	public ActorReferenceGOImpl(AssetHandler assetHandler,
			StringsReader stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				valueMap, platformConfiguration);
		logger.info("New instance");
		this.inventoryReference = false;
	}

	private boolean isRemoved() {
		if (gameState.getRemovedActors().contains(element.getReferencedActor())
				|| gameState.getRemovedActors().contains(
						element.getReferencedActor()))
			return true;
		return false;
	}

	public GameObject<?> getDraggableElement(MouseState mouseState) {
		if (isRemoved())
			return null;
		/*
		 * TODO return "this" if one of the valid actions is draggable? for
		 * (EAdAction action : actor.getValidActions() ) { }
		 */
		return null;
	}

	@Override
	public void setElement(EAdActorReference actorReference) {
		this.actor = (ActorGO) gameObjectFactory.get(actorReference
				.getReferencedActor());
		super.setElement(actorReference);
		// To update width and height
		getRenderAsset();
	}

	@Override
	public boolean processAction(GUIAction action) {
		EAdList<EAdEffect> list = element.getEffects(action.getGUIEvent());
		if (list != null && list.size() > 0) {
			action.consume();
			for (EAdEffect e : element.getEffects(action.getGUIEvent())) {
				gameState.addEffect(e);
			}
			return true;
		}
		return false;
	}

	@Override
	public EAdString getName() {
		return actor.getName();
	}

	@Override
	public void update(GameState state) {
		if (!isRemoved()) {
			super.update(state);
			actor.update(state);
		}
	}
	
	public AssetDescriptor getCurrentAssetDescriptor(){
		AssetDescriptor a = actor
				.getElement()
				.getResources()
				.getAsset(((ActorGOImpl) actor).getCurrentBundle(),
						EAdBasicActor.appearance);
		
		return getCurrentAssetDescriptor( a );
	}

	@Override
	public List<EAdAction> getValidActions() {
		return actor.getValidActions();
	}

	@Override
	public boolean isVisible() {
		if (!inventoryReference
				&& gameState.getInventoryActors().contains(
						element.getReferencedActor()))
			return false;
		return super.isVisible();
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		if (!isVisible() && !allAssets) {
			return assetList;
		}

		List<EAdBundleId> bundles = new ArrayList<EAdBundleId>();
		if (allAssets)
			bundles.addAll(((ActorGOImpl) actor).getElement().getResources()
					.getBundles());
		else
			bundles.add(((ActorGOImpl) actor).getCurrentBundle());

		for (EAdBundleId bundle : bundles) {
			AssetDescriptor a = actor.getElement().getResources()
					.getAsset(bundle, EAdBasicActor.appearance);
			super.getAssetsRecursively(a, assetList, allAssets);

		}
		return assetList;
	}

	public void setInventoryReference(boolean inventoryReference) {
		this.inventoryReference = inventoryReference;
	}

}
