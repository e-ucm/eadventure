package ead.engine.core.gameobjects.sceneelements;

import com.google.inject.Inject;

import ead.common.model.elements.scenes.EAdGhostElement;
import ead.common.params.fills.Paint;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.RuntimeDrawable;

public class GhostElementGO extends SceneElementGOImpl<EAdGhostElement> {

	private RuntimeDrawable<?, ?> interactionArea;

	@Inject
	public GhostElementGO(AssetHandler assetHandler,
			SceneElementGOFactory sceneElementFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory) {
		super(assetHandler, sceneElementFactory, gui, gameState, eventFactory);
	}

	@Override
	public void setElement(EAdGhostElement element) {
		interactionArea = assetHandler.getDrawableAsset(element
				.getInteractionArea());
		RectangleShape area = new RectangleShape(interactionArea.getWidth(),
				interactionArea.getHeight(), Paint.TRANSPARENT);
		element.getDefinition().setAppearance(area);
		super.setElement(element);
	}

	@Override
	public boolean contains(int x, int y) {
		return interactionArea.contains(x, y);
	}

}
