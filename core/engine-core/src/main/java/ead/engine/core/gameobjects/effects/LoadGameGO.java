package ead.engine.core.gameobjects.effects;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ead.common.model.assets.AssetDescriptor;
import ead.common.model.assets.multimedia.EAdVideo;
import ead.common.model.elements.effects.LoadGameEf;
import ead.common.model.elements.operations.SystemFields;
import ead.common.model.elements.predef.LoadingScreen;
import ead.common.model.elements.scenes.EAdScene;
import ead.engine.core.assets.AssetHandler;
import ead.engine.core.factories.SceneElementGOFactory;
import ead.engine.core.game.interfaces.GUI;
import ead.engine.core.game.interfaces.Game;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.sceneelements.SceneGO;
import ead.reader.AdventureReader;

public class LoadGameGO extends AbstractEffectGO<LoadGameEf> {

	private static final Logger logger = LoggerFactory.getLogger("LoadGame");

	private SceneElementGOFactory sceneElementFactory;

	private GUI gui;

	private Game game;

	private AssetHandler assetHandler;

	private AdventureReader reader;

	private Stack<AssetDescriptor> assetsToLoad;

	private boolean readingXML;

	private boolean readingAssets;

	private int assetsToLoadNumber;

	private boolean done;

	@Inject
	public LoadGameGO(GameState gameState, AdventureReader adventureReader,
			SceneElementGOFactory sceneElementFactory,
			AssetHandler assetHandler, Game game, GUI gui) {
		super(gameState);
		this.reader = adventureReader;
		this.sceneElementFactory = sceneElementFactory;
		this.assetHandler = assetHandler;
		this.game = game;
		this.gui = gui;
		assetsToLoad = new Stack<AssetDescriptor>();
	}

	public void initialize() {
		super.initialize();
		if (effect.isReloadAssets()) {
			assetHandler.clean();
		}
		done = false;
		EAdScene loadingScreen = effect.getLoadingScene();
		if (loadingScreen == null) {
			loadingScreen = new LoadingScreen();
		}
		gui.setScene((SceneGO) sceneElementFactory.get(loadingScreen));
		reader.readXML(assetHandler.getTextFile("@data.xml"), game);
		readingXML = true;
	}

	public void act(float delta) {
		if (readingXML) {
			for (int i = 0; i < 200; i++) {
				if (reader.step()) {
					break;
				}
			}

			if (reader.step()) {
				gameState.setValue(SystemFields.LOADING, 30);
				readingAssets = true;
				readingXML = false;
				assetsToLoad.addAll(reader.getAssets());
				logger.info("Loading {} assets", reader.getAssets().size());
				assetsToLoadNumber = reader.getAssets().size();
			}
		} else if (readingAssets) {
			for (int i = 0; i < 10; i++) {
				if (assetsToLoad.isEmpty()) {
					readingAssets = false;
					done = true;
					break;
				}
				AssetDescriptor a = assetsToLoad.pop();
				if (!(a instanceof EAdVideo)) {
					assetHandler.getRuntimeAsset(a);
				} else {
					assetHandler.addVideo((EAdVideo) a);
				}
			}
			if (assetsToLoadNumber > 0) {
				gameState.setValue(SystemFields.LOADING, (int) (30.0f + 70.f
						* (float) (assetsToLoadNumber - assetsToLoad.size())
						/ (float) assetsToLoadNumber));
			} else {
				gameState.setValue(SystemFields.LOADING, 100);
			}
		}
	}

	public boolean isFinished() {
		return done;
	}

	public void finish() {
		super.finish();
		game.startGame();
	}

	public boolean isQueueable() {
		return true;
	}

}
