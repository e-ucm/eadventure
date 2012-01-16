package ead.engine.core.gameobjects.transitions;

import com.google.inject.Inject;

import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.transitions.DisplaceTransition;
import ead.common.model.elements.transitions.enums.DisplaceTransitionType;
import ead.common.model.elements.variables.SystemFields;
import ead.common.resources.StringHandler;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.transitions.SceneLoader;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.util.EAdInterpolator;
import ead.engine.core.util.EAdTransformation;
import ead.engine.core.util.EAdTransformationImpl;

public class DisplaceTransitionGO extends
		AbstractTransitionGO<DisplaceTransition> {

	private boolean finished;

	private int width;

	private int height;

	private int startTime = -1;
	
	private int x1, x2, y1, y2;
	
	private EAdTransformation transformation;

	@Inject
	public DisplaceTransitionGO(AssetHandler assetHandler,
			StringHandler stringHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			SceneLoader sceneLoader) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				eventFactory, sceneLoader);
		finished = false;
		width = gameState.getValueMap().getValue(SystemFields.GAME_WIDTH);
		height = gameState.getValueMap().getValue(SystemFields.GAME_HEIGHT);
		transformation = new EAdTransformationImpl( );
	}

	public void doLayout(EAdTransformation t) {
		super.doLayout(t);

		if (this.isLoadedNextScene() && startTime != -1) {
			transformation.getMatrix().setIdentity();
			transformation.getMatrix().translate(x2, y2, false);
			gui.addElement(nextSceneGO, gui.addTransformation(transformation, t));
			if (!isFinished()) {
				transformation.getMatrix().setIdentity();
				transformation.getMatrix().translate(x1, y1, false);
				gui.addElement(previousScene, gui.addTransformation(transformation, t));
			}
		}
	}

	public void update() {
		super.update();
		if (isLoadedNextScene()) {

			int currentTime = gameState.getValueMap().getValue(getElement(),
					SceneElementImpl.VAR_TIME_DISPLAYED);
			if (startTime == -1) {
				startTime = currentTime;
			}
			
			if (currentTime - startTime >= transition.getTime()) {
				finished = true;
			} else {
				float dispX = getDisp(true, currentTime - startTime);
				float dispY = getDisp(false, currentTime - startTime);
				
				if (dispX != 0.0f) {
					x1 = ((int) (dispX * -width));
					x2 = ((int) ((1 - dispX) * width));
					if ( transition.getForward() ){
						x1 = - x1;
						x2 =  (int) (dispX * width) - width;
					}
				}

				if (dispY != 0.0f) {
					y1 = ((int) (dispY * -height));
					y2 = ((int) ((1 - dispY) * height));
					
					if ( transition.getForward() ){
						y1 = - y1;
						y2 =  (int) (dispY * height) - height;
					}
				}

			}
		}

	}

	public boolean isFinished() {
		return finished;
	}

	private float getDisp(boolean horizontal, int currentTime) {
		if ((horizontal && transition.getType() == DisplaceTransitionType.HORIZONTAL)
				|| (!horizontal && transition.getType() == DisplaceTransitionType.VERTICAL)) {
			float value = EAdInterpolator.LINEAR.interpolate(currentTime,
					transition.getTime(), 1.0f);
			return value;

		} else
			return 0;

	}
}
