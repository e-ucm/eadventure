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
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.OrientedAsset;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.ActorGO;
import es.eucm.eadventure.engine.core.gameobjects.ActorReferenceGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public class ActorReferenceGOImpl extends SceneElementGOImpl<EAdActorReference>
		implements ActorReferenceGO {

	private static final Logger logger = Logger
			.getLogger("ActorReferneceGOImpl");

	protected ActorGO actor;

	@Inject
	public ActorReferenceGOImpl(AssetHandler assetHandler, ValueMap valueMap,
			GameState gameState) {
		logger.info("New instance");
		this.valueMap = valueMap;
		this.assetHandler = assetHandler;
		this.gameState = gameState;
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
		// TODO check if draggable
		return this;
	}

	@Override
	public void setElement(EAdActorReference actorReference) {
		this.actor = (ActorGO) gameObjectFactory.get(actorReference
				.getReferencedActor());
		super.setElement(actorReference);
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

	@Override
	public RuntimeAsset<?> getAsset() {
		if (isRemoved())
			return null;

		AssetDescriptor a = actor
				.getElement()
				.getResources()
				.getAsset(((ActorGOImpl) actor).getCurrentBundle(),
						EAdBasicActor.appearance);

		if (a instanceof OrientedAsset) {
			a = ((OrientedAsset) a).getAssetDescritpor(orientation);
		}

		RuntimeAsset<?> r = assetHandler.getRuntimeAsset(a);
		if (r instanceof DrawableAsset) {
			setWidth(((DrawableAsset<?>) r).getWidth());
			setHeight(((DrawableAsset<?>) r).getHeight());
			return ((DrawableAsset<?>) r).getDrawable();
		}

		return r;
	}

	@Override
	public List<EAdAction> getValidActions() {
		return actor.getValidActions();
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

			if (a instanceof OrientedAsset) {
				if (!allAssets)
					assetList.add(assetHandler
							.getRuntimeAsset(((OrientedAsset) a)
									.getAssetDescritpor(orientation)));
				else {
					assetList.add(assetHandler
							.getRuntimeAsset(((OrientedAsset) a)
									.getAssetDescritpor(Orientation.EAST)));
					assetList.add(assetHandler
							.getRuntimeAsset(((OrientedAsset) a)
									.getAssetDescritpor(Orientation.NORTH)));
					assetList.add(assetHandler
							.getRuntimeAsset(((OrientedAsset) a)
									.getAssetDescritpor(Orientation.SOUTH)));
					assetList.add(assetHandler
							.getRuntimeAsset(((OrientedAsset) a)
									.getAssetDescritpor(Orientation.WEST)));
				}
			} else {
				assetList.add(assetHandler.getRuntimeAsset(a));
			}
		}
		return assetList;
	}

}
