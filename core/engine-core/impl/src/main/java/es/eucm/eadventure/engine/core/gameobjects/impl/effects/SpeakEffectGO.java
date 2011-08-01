package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElement.CommonStates;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexSceneElement;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.params.guievents.EAdMouseEvent.MouseActionType;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.BallonShape;
import es.eucm.eadventure.common.resources.assets.drawable.impl.BezierShape;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeCaption;

public class SpeakEffectGO extends AbstractEffectGO<EAdSpeakEffect> {

	private static final int MARGIN_PROPORTION = 35;

	private static final int HEIGHT_PROPORTION = 4;

	private static final int MARGIN = 30;

	private SceneElementGO<?> ballon;

	private RuntimeCaption caption;

	private boolean finished;

	private String previousState;

	private EAdBasicSceneElement textSE;

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

			if (mouseAction.getType() == MouseActionType.PRESSED) {
				if (caption.getTimesRead() >= 1
						|| caption.getCurrentPart() == caption.getTotalParts() - 1)
					finished = true;
				else
					caption.goForward(1);
			}
		}
		return super.processAction(action);
	}

	@Override
	public void initilize() {
		super.initilize();
		finished = false;
		ballon = (SceneElementGO<?>) gameObjectFactory.get(getSceneElement());

		if (element.getStateVar() != null) {
			previousState = valueMap.getValue(element.getStateVar());
			valueMap.setValue(element.getStateVar(),
					CommonStates.EAD_STATE_TALKING.toString());
		}

	}

	private EAdSceneElement getSceneElement() {
		int width = platformConfiguration.getVirtualWidth();
		int height = platformConfiguration.getVirtualHeight();
		int horizontalMargin = width / MARGIN_PROPORTION;
		int verticalMargin = height / MARGIN_PROPORTION;
		int left = horizontalMargin;
		int right = width - horizontalMargin;
		int top = verticalMargin;
		int bottom = height / HEIGHT_PROPORTION + top;

		BezierShape rectangle = null;

		if (element.getPosX() != null && element.getPosY() != null) {
			int xOrigin = valueMap.getValue(element.getPosX());
			int yOrigin = valueMap.getValue(element.getPosY());

			if (yOrigin < height / 2) {
				int offsetY = height - (bottom - top) - horizontalMargin * 2;
				top += offsetY;
				bottom += offsetY;
			}

			rectangle = new BallonShape(left, top, right, bottom,
					element.getBallonType(), xOrigin, yOrigin);
		} else {
			int offsetY = height / 2 - (bottom - top) / 2;
			top += offsetY;
			bottom += offsetY;
			rectangle = new BallonShape(left, top, right, bottom,
					element.getBallonType());
		}

		rectangle.setColor(element.getBubbleColor());
		CaptionImpl text = new CaptionImpl();
		text.setText(element.getString());
		text.setTextColor(element.getTextColor());
		text.setMaximumWidth(right - left - MARGIN * 2);
		text.setMaximumHeight(bottom - top - MARGIN * 2);
		text.setFont(element.getFont());

		textSE = new EAdBasicSceneElement("text");
		textSE.getResources().addAsset(textSE.getInitialBundle(),
				EAdBasicSceneElement.appearance, text);
		textSE.setPosition(new EAdPosition(left + MARGIN, top));

		EAdComplexSceneElement complex = new EAdComplexSceneElement("complex");
		complex.getResources().addAsset(complex.getInitialBundle(),
				EAdBasicSceneElement.appearance, rectangle);
		complex.getComponents().add(textSE);

		caption = (RuntimeCaption) ((SceneElementGO<?>) gameObjectFactory
				.get(textSE)).getRenderAsset();

		return complex;
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

	public void update(GameState state) {
		super.update(state);
		ballon.update(state);
		finished = finished || caption.getTimesRead() > 0;
	}

	@Override
	public void finish() {
		super.finish();
		if (element.getStateVar() != null) {
			valueMap.setValue(element.getStateVar(), previousState);
		}
	}

}
