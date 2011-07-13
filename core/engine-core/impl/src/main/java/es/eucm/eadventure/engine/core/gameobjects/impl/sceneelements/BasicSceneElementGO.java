package es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements;

import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.engine.core.EvaluatorFactory;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class BasicSceneElementGO extends SceneElementGOImpl<EAdBasicSceneElement> {

	private static final Logger logger = Logger
	.getLogger("BasicSceneElementGOImpl");

	private EvaluatorFactory evaluatorFactory;
	
	@Inject
	public BasicSceneElementGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration, EvaluatorFactory evaluatorFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
		logger.info("New instance");
		this.evaluatorFactory = evaluatorFactory;
	}
	
	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.SceneElementGOImpl#getDraggableElement(es.eucm.eadventure.engine.core.MouseState)
	 */
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		if (evaluatorFactory.evaluate(element.getDraggabe()))
			return this;
		return null;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.SceneElementGOImpl#setElement(es.eucm.eadventure.common.model.elements.EAdSceneElement)
	 */
	public void setElement(EAdBasicSceneElement basicSceneElement) {
		super.setElement(basicSceneElement);
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.SceneElementGOImpl#processAction(es.eucm.eadventure.engine.core.guiactions.GUIAction)
	 */
	@Override
	public boolean processAction(GUIAction action) {
		EAdList<EAdEffect> list = element.getEffects(action.getGUIEvent());
		if (list != null && list.size() > 0) {
			action.consume();
			for (EAdEffect e : list) {
				gameState.addEffect(e, action);
			}
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.SceneElementGOImpl#update(es.eucm.eadventure.engine.core.GameState)
	 */
	@Override
	public void update(GameState state) {
		super.update(state);
	}

	@Override
	public List<EAdAction> getValidActions() {
		//TODO?
		return null;
	}

}
