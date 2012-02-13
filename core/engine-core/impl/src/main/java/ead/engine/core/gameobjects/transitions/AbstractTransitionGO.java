package ead.engine.core.gameobjects.transitions;

import java.util.ArrayList;
import java.util.List;

import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.transitions.EAdTransition;
import ead.common.model.elements.variables.EAdField;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.fills.PaintFill;
import ead.common.resources.assets.drawable.basics.shapes.CircleShape;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.util.StringHandler;
import ead.common.util.EAdPosition.Corner;
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

	private ArrayList<TransitionListener> listeners;

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
		listeners = new ArrayList<TransitionListener>();
	}

	public void setTransition(T transition) {
		this.transition = transition;
	}

	public void setPrevious(SceneGO<?> scene) {
		this.previousScene = scene;
		gameState.getValueMap().setValue(SystemFields.PROCESS_INPUT, false);
		for (TransitionListener l : this.getTransitionListeners()) {
			l.transitionBegins();
		}
	}

	@Override
	public void sceneLoaded(SceneGO<?> sceneGO) {
		nextSceneGO = sceneGO;
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
		BasicScene scene = new BasicScene();

		RectangleShape rs = new RectangleShape(gameState.getValueMap()
				.getValue(SystemFields.GAME_WIDTH), gameState.getValueMap()
				.getValue(SystemFields.GAME_HEIGHT));
		rs.setPaint(new PaintFill(new ColorFill(100, 100, 100, 0),
				ColorFill.BLACK));

		int circleRadius = 15;
		CircleShape circle = new CircleShape(circleRadius, circleRadius,
				circleRadius, 40, new LinearGradientFill(ColorFill.ORANGE,
						ColorFill.YELLOW, circleRadius * 2, circleRadius * 2));
		SceneElementImpl loadingText = new SceneElementImpl(circle);
		loadingText.setInitialAlpha(0.8f);
		loadingText.setId("loadingText");
		loadingText.setPosition(Corner.CENTER, circleRadius * 2,
				circleRadius * 2);
		rotationField = new BasicField<Float>(loadingText,
				SceneElementImpl.VAR_ROTATION);

		scene.setBackground(new SceneElementImpl(rs));
		scene.getComponents().add(loadingText);
		return scene;
	}

	@Override
	public void setNext(EAdScene scene) {
		nextScene = scene;
		loading = false;
	}

	public void update() {
		super.update();
		if (!loaded && !loading) {
			loading = true;
			sceneLoader.loadScene(nextScene, this);
		}

		rotation += 0.5f;
		if (rotation > 2 * Math.PI) {
			rotation -= 2 * Math.PI;
		}
		gameState.getValueMap().setValue(rotationField, rotation);

		if (isFinished()) {
			sceneLoader.freeUnusedAssets(nextSceneGO);
			gameState.setScene(nextSceneGO);
			gameState.getValueMap().setValue(SystemFields.PROCESS_INPUT, true);
			for (TransitionListener l : this.getTransitionListeners()) {
				l.transitionEnds();
			}
		}
	}

	public List<TransitionListener> getTransitionListeners() {
		return listeners;
	}

	public SceneGO<?> getNextSceneGO() {
		return nextSceneGO;
	}
}
