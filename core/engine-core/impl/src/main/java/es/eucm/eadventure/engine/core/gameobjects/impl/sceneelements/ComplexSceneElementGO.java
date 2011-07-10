package es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements;

import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexSceneElement;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.OrientedAsset;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public class ComplexSceneElementGO extends
		SceneElementGOImpl<EAdComplexSceneElement> {

	private static final Logger logger = Logger
			.getLogger("EAdComplexSceneElement");

	@Inject
	public ComplexSceneElementGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
		logger.info("New instance");
	}

	public GameObject<?> getDraggableElement(MouseState mouseState) {
		// TODO check if draggable
		return this;
	}

	public void setElement(EAdComplexSceneElement basicSceneElement) {
		super.setElement(basicSceneElement);
	}

	@Override
	public boolean processAction(GUIAction action) {
		EAdList<EAdEffect> list = element.getEffects(action
				.getGUIEvent());
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
	public void update(GameState state) {
		super.update(state);
		for (EAdSceneElement sceneElement : element.getComponents())
			gameObjectFactory.get(sceneElement).update(state);
	}

	@Override
	public void doLayout(int offsetX, int offsetY) {
		int newOffsetX = offsetX + this.getPosition().getJavaX(getWidth());
		int newOffsetY = offsetY + this.getPosition().getJavaY(getHeight());
		for (EAdSceneElement sceneElement : element.getComponents()) {
 			SceneElementGO<?> go = (SceneElementGO<?>) gameObjectFactory.get(sceneElement);
 			if (go.isVisible())
 				gui.addElement(go, newOffsetX, newOffsetY);
		}
	}

	@Override
	public DrawableAsset<?> getAsset() {
		AssetDescriptor a = element.getResources().getAsset(getCurrentBundle(),
				EAdBasicSceneElement.appearance);

		if (a instanceof OrientedAsset) {
			a = ((OrientedAsset) a).getAssetDescritpor(orientation);
		}

		DrawableAsset<?> r = (DrawableAsset<?>) assetHandler.getRuntimeAsset(a);
		if (r instanceof DrawableAsset) {
			setWidth(r.getWidth());
			setHeight(r.getHeight());
			return r.getDrawable();
		}
		return r;
	}

	@Override
	public List<EAdAction> getValidActions() {
		// TODO?
		return null;
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {
		if (!isVisible() && !allAssets) {
			return assetList;
		}

		for (EAdSceneElement sceneElement : element.getComponents())
			assetList = gameObjectFactory.get(sceneElement).getAssets(
					assetList, allAssets);
		return assetList;
	}

}
