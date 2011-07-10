package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.EAdModifyActorState;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

/**
 * <p>
 * Game object for the {@link EAdModifiyActorState} effect
 * </p>
 * <p>
 * This effect places the actor in the appropriate list in the game state, be it
 * the actors in the inventory, the actors removed from the scene and the
 * inventory or no list at all.
 * </p>
 * 
 */
public class ModifyActorStateGO extends AbstractEffectGO<EAdModifyActorState> {

	@Inject
	public ModifyActorStateGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.engine.core.gameobjects.impl.AbstractGameObject#update
	 * (es.eucm.eadventure.engine.core.GameState)
	 */
	@Override
	public void update(GameState state) {
		super.update(state);
		switch (element.getModification()) {
		case PLACE_IN_INVENTORY:
			state.getRemovedActors().remove(element.getActor());
			if (!state.getInventoryActors().contains(element.getActor()))
				state.getInventoryActors().add(element.getActor());
			break;
		case PLACE_IN_SCENE:
			state.getRemovedActors().remove(element.getActor());
			state.getInventoryActors().remove(element.getActor());
			break;
		case REMOVE_SCENE_AND_INVENTORY:
			if (!state.getRemovedActors().contains(element.getActor()))
				state.getRemovedActors().add(element.getActor());
			state.getInventoryActors().remove(element.getActor());
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.gameobjects.EffectGO#isVisualEffect()
	 */
	@Override
	public boolean isVisualEffect() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.engine.core.gameobjects.EffectGO#isFinished()
	 */
	@Override
	public boolean isFinished() {
		return true;
	}

}
