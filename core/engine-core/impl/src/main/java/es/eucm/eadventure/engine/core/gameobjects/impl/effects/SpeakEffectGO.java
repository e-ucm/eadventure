package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexSceneElement;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.params.guievents.EAdMouseEvent.MouseActionType;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.BallonShape;
import es.eucm.eadventure.common.resources.assets.drawable.impl.BallonShape.BallonType;
import es.eucm.eadventure.common.resources.assets.drawable.impl.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

public class SpeakEffectGO extends AbstractEffectGO<EAdSpeakEffect> {

	private GameObject<?> ballon;

	private boolean finished;

	@Inject
	public SpeakEffectGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				valueMap, platformConfiguration);
	}

	@Override
	public boolean processAction(GUIAction action) {
		if (action instanceof MouseAction) {
			MouseAction mouseAction = (MouseAction) action;

			if (mouseAction.getType() == MouseActionType.LEFT_CLICK) {
				finished = true;
			}
		}
		return super.processAction(action);
	}

	@Override
	public void initilize() {
		super.initilize();
		finished = false;
		int width = platformConfiguration.getVirtualWidth();
		int height = platformConfiguration.getVirtualHeight();
		int horizontalMargin = width / 40;
		int verticalMargin = height / 40;
		int left = horizontalMargin;
		int right = width - horizontalMargin;
		int top = verticalMargin;
		int bottom = height / 4 + top;

		BezierShape rectangle = null;

		if (element.getPosX() != null && element.getPosY() != null) {
			int xOrigin = valueMap.getValue(element.getPosX());
			int yOrigin = valueMap.getValue(element.getPosY());
			
			if ( yOrigin < height / 2 ){
				int offsetY = height - ( bottom - top ) - horizontalMargin * 2;
				top += offsetY;
				bottom += offsetY; 
			}
			
			rectangle = new BallonShape(left, top, right, bottom,
					BallonType.RECTANGLE, xOrigin, yOrigin);
		} else {
			int offsetY = height / 2 - (bottom - top) / 2;
			top += offsetY;
			bottom += offsetY;
			rectangle = new BallonShape(left, top, right, bottom,
					BallonType.RECTANGLE);
		}
		rectangle.setColor(element.getBubbleColor());

		CaptionImpl text = new CaptionImpl( );
		text.setText(element.getString());
		text.setTextColor(element.getTextColor());
		text.setMaximumWidth(right - left);
		text.setMaximumHeight(bottom - top );
		
		EAdBasicSceneElement textSE = new EAdBasicSceneElement("text");
		textSE.getResources().addAsset(textSE.getInitialBundle(),
				EAdBasicSceneElement.appearance, text);
		textSE.setPosition(new EAdPosition( left, top ));
		
		EAdComplexSceneElement complex = new EAdComplexSceneElement("complex");
		complex.getResources().addAsset(complex.getInitialBundle(),
				EAdBasicSceneElement.appearance, rectangle);
		complex.getComponents().add(textSE);
		
		
		ballon = gameObjectFactory.get(complex);
	}

	@Override
	public boolean isVisualEffect() {
		return true;
	}

	public void doLayout(int offsetX, int offsetY) {
		gui.addElement(ballon, offsetX, offsetY);
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

}
