package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.EAdAddActorReferenceEffect;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.AbstractSceneElementEffect;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent;
import es.eucm.eadventure.common.model.events.EAdSceneElementEvent.SceneElementEvent;
import es.eucm.eadventure.common.model.events.impl.EAdSceneElementEventImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.factories.SceneElementGOFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;

public class AddActorReferenceEffectGO extends
		AbstractEffectGO<EAdAddActorReferenceEffect> {

	@Inject
	public AddActorReferenceEffectGO(AssetHandler assetHandler,
			StringHandler stringsReader,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState) {
		super(assetHandler, stringsReader, gameObjectFactory, gui, gameState);
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
			((AbstractSceneElementEffect) element.getInitialEffect())
					.setSceneElement(ref);
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
