package ead.engine.core.gameobjects.transitions;

import com.google.inject.Inject;

import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.transitions.FadeInTransition;
import ead.common.util.Interpolator;
import ead.common.util.StringHandler;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.transitions.SceneLoader;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.GUI;
import ead.engine.core.util.EAdTransformation;
import ead.engine.core.util.EAdTransformationImpl;

public class FadeInTransitionGO extends AbstractTransitionGO<FadeInTransition>{
	
	private boolean finished;
	
	private int startTime = -1;

	private float sceneAlpha;
	
	private EAdTransformation transformation;

	@Inject
	public FadeInTransitionGO(AssetHandler assetHandler,
			StringHandler stringHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			SceneLoader sceneLoader) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				eventFactory, sceneLoader);
		finished = false;
		transformation = new EAdTransformationImpl();
	}
	
	public void update() {
		super.update();
		if (isLoadedNextScene()) {

			int currentTime = gameState.getValueMap().getValue(getElement(),
					SceneElementImpl.VAR_TIME_DISPLAYED);
			if (startTime == -1) {
				startTime = currentTime;
				sceneAlpha = 0.0f;
			}
			
			if (currentTime - startTime >= transition.getTime()) {
				finished = true;
			}
			else {
				sceneAlpha = (Interpolator.LINEAR.interpolate(currentTime - startTime, transition.getTime(), 1.0f));
			}
		}
	}
	
	public void doLayout(EAdTransformation t) {
		if (this.isLoadedNextScene()) {
			gui.addElement(previousScene, t);
			transformation.setAlpha(sceneAlpha);
			gui.addElement(nextSceneGO, gui.addTransformation(t, transformation));
		} else {
			super.doLayout(t);
		}
	}
	
	public boolean isFinished( ){
		return finished;
	}

}
