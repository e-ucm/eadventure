package ead.engine.core.gameobjects.transitions;

import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.scenes.SceneImpl;
import ead.common.model.elements.transitions.EAdTransition;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.EAdFieldImpl;
import ead.common.model.elements.variables.SystemFields;
import ead.common.params.fills.EAdColor;
import ead.common.params.fills.EAdLinearGradient;
import ead.common.params.fills.EAdPaintImpl;
import ead.common.resources.StringHandler;
import ead.common.resources.assets.drawable.basics.shapes.CircleShape;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.engine.core.game.GameState;
import ead.engine.core.gameobjects.SceneGOImpl;
import ead.engine.core.gameobjects.factories.EventGOFactory;
import ead.engine.core.gameobjects.factories.SceneElementGOFactory;
import ead.engine.core.gameobjects.go.SceneGO;
import ead.engine.core.gameobjects.go.transitions.SceneLoader;
import ead.engine.core.gameobjects.go.transitions.TransitionGO;
import ead.engine.core.platform.AssetHandler;
import ead.engine.core.platform.GUI;

public abstract class AbstractTransitionGO<T extends EAdTransition> extends
		SceneGOImpl implements TransitionGO<T> {

	protected T transition;

	protected SceneGO<?> previousScene;

	protected EAdScene nextScene;

	protected SceneGO<?> nextSceneGO;

	protected SceneLoader sceneLoader;

	private boolean loading;

	private boolean loaded;

	private float rotation = 0.0f;

	private EAdField<Float> rotationField;

	public AbstractTransitionGO(AssetHandler assetHandler,
			StringHandler stringHandler,
			SceneElementGOFactory gameObjectFactory, GUI gui,
			GameState gameState, EventGOFactory eventFactory,
			SceneLoader sceneLoader) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState,
				eventFactory);
		this.sceneLoader = sceneLoader;
		EAdScene scene = this.createLoadingScene();
		scene.setReturnable(false);
		this.setElement(scene);
	}

	public void setTransition(T transition) {
		this.transition = transition;
	}

	public void setPrevious(SceneGO<?> scene) {
		this.previousScene = scene;
		gameState.getValueMap().setValue(SystemFields.PROCESS_INPUT, false);
	}

	@Override
	public void sceneLoaded(SceneGO<?> sceneGO) {
		nextSceneGO = sceneGO;
		nextSceneGO.update();
		loaded = true;
		loading = false;
	}

	@Override
	public boolean isLoadedNextScene() {
		return loaded;
	}

	@Override
	public boolean isFinished() {
		return loaded;
	}

	protected EAdScene createLoadingScene() {
		SceneImpl scene = new SceneImpl();

		RectangleShape rs = new RectangleShape(gameState.getValueMap()
				.getValue(SystemFields.GAME_WIDTH), gameState.getValueMap()
				.getValue(SystemFields.GAME_HEIGHT));
		rs.setPaint(new EAdPaintImpl(new EAdColor(100, 100, 100, 30),
				EAdColor.BLACK));

		int circleRaidus = 15;
		CircleShape circle = new CircleShape(circleRaidus, circleRaidus,
				circleRaidus, 40, new EAdLinearGradient(EAdColor.ORANGE,
						EAdColor.YELLOW, circleRaidus * 2, circleRaidus * 2));
		SceneElementImpl loadingText = new SceneElementImpl(circle);
		loadingText.setInitialAlpha(0.8f);
		loadingText.setId("loadingText");
		rotationField = new EAdFieldImpl<Float>(loadingText,
				SceneElementImpl.VAR_ROTATION);

		scene.setBackground(new SceneElementImpl(rs));
		return scene;
	}

	@Override
	public void setNext(EAdScene scene) {
		nextScene = scene;
		loading = false;
	}

	public void update() {
		super.update();
		if (!loading) {
			loading = true;
			sceneLoader.loadScene(nextScene, this);
		}
		
		rotation += 0.5f;
		if (rotation > 2 * Math.PI){
			rotation -= 2 * Math.PI;
		}
		gameState.getValueMap().setValue(rotationField, rotation);

		if (isFinished()) {
			gameState.getValueMap().setValue(SystemFields.PROCESS_INPUT, true);
			gameState.setScene(nextSceneGO);
			sceneLoader.freeUnusedAssets(nextSceneGO);
		}
	}
}
