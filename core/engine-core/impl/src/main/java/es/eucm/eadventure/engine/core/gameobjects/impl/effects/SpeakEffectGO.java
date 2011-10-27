package es.eucm.eadventure.engine.core.gameobjects.impl.effects;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.text.EAdSpeakEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdComplexElementImpl;
import es.eucm.eadventure.common.model.guievents.EAdMouseEvent.MouseActionType;
import es.eucm.eadventure.common.model.variables.impl.SystemFields;
import es.eucm.eadventure.common.params.fills.impl.EAdColor;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BallonShape;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.BezierShape;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneElementGO;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.operator.OperatorFactory;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.assets.impl.RuntimeCaption;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

public class SpeakEffectGO extends AbstractEffectGO<EAdSpeakEffect> {

	private static final int MARGIN_PROPORTION = 35;

	private static final int HEIGHT_PROPORTION = 4;

	private static final int MARGIN = 30;

	private SceneElementGO<?> ballon;

	private RuntimeCaption caption;

	private boolean finished;

	private EAdBasicSceneElement textSE;
	
	private OperatorFactory operatorFactory;
	
	@Inject
	public SpeakEffectGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, OperatorFactory operatorFactory) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState);
		this.operatorFactory = operatorFactory;
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
	}

	private EAdSceneElement getSceneElement() {
		//TODO Check if necessary to use platform configuration
		int width = gameState.getValueMap().getValue(SystemFields.GUI_WIDTH);
		int height = gameState.getValueMap().getValue(SystemFields.GUI_HEIGHT);
		int horizontalMargin = width / MARGIN_PROPORTION;
		int verticalMargin = height / MARGIN_PROPORTION;
		int left = horizontalMargin;
		int right = width - horizontalMargin;
		int top = verticalMargin;
		int bottom = height / HEIGHT_PROPORTION + top;

		BezierShape rectangle = null;

		if (element.getX() != null && element.getY() != null) {
			Integer xOrigin = operatorFactory.operate(Integer.class, element.getX());
			Integer yOrigin = operatorFactory.operate(Integer.class, element.getY());
			rectangle = new BallonShape(left, top, right, bottom,
					element.getBallonType(), xOrigin, yOrigin);
		} else {
			int offsetY = height / 2 - (bottom - top) / 2;
			top += offsetY;
			bottom += offsetY;
			rectangle = new BallonShape(left, top, right, bottom,
					element.getBallonType());
		}

		rectangle.setPaint(element.getBubbleColor());
		CaptionImpl text = new CaptionImpl(element.getString());
		text.setPadding(MARGIN);
		text.setBubblePaint(EAdColor.RED);
		text.setTextPaint(element.getTextColor());
		text.setPreferredWidth(right - left - MARGIN * 2);
		text.setPreferredHeight(bottom - top - MARGIN * 2);
		text.setFont(element.getFont());

		textSE = new EAdBasicSceneElement("text");
		textSE.getResources().addAsset(textSE.getInitialBundle(),
				EAdBasicSceneElement.appearance, text);
		textSE.setPosition(new EAdPositionImpl(left, top));

		EAdComplexElementImpl complex = new EAdComplexElementImpl("complex");
		complex.getResources().addAsset(complex.getInitialBundle(),
				EAdBasicSceneElement.appearance, rectangle);
		complex.getElements().add(textSE);

		caption = (RuntimeCaption) ((SceneElementGO<?>) gameObjectFactory
				.get(textSE)).getRenderAsset();
		caption.reset();

		return complex;
	}

	@Override
	public boolean isVisualEffect() {
		return true;
	}

	public void doLayout(EAdTransformation t) {
		gui.addElement(ballon, t);
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	public void update() {
		super.update();
		ballon.update();
		finished = finished || caption.getTimesRead() > 0;
	}

}
