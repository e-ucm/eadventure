package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.EAdAddActorReferenceEffect;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class AddActorReferenceEffectGO extends
		AbstractEffectGO<EAdAddActorReferenceEffect> {

	@Inject
	public AddActorReferenceEffectGO(AssetHandler assetHandler,
			StringHandler stringsReader, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState,
				valueMap, platformConfiguration);
	}

	@Override
	public void initilize() {
		super.initilize();
		EAdScene scene = gameState.getScene().getElement();
		if (scene != null) {
			EAdSceneElementDef actor = element.getActor();
			EAdBasicSceneElement ref = new EAdBasicSceneElement(actor);
			ref.setPosition(element.getPosition());
			EAdSceneElementEvent event = new EAdSceneElementEventImpl();
			event.addEffect(SceneElementEvent.ADDED_TO_SCENE,
					element.getInitialEffect());
			element.getInitialEffect().setSceneElement(ref);
			ref.getEvents().add(event);
			scene.getElements().add(ref, 0);
		}
	}

	@Override
	public boolean isVisualEffect() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

}
