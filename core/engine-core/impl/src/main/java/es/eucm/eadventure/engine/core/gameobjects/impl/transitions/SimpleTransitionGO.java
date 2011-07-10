package es.eucm.eadventure.engine.core.gameobjects.impl.transitions;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.params.EAdBorderedColor;
import es.eucm.eadventure.common.model.params.EAdColor;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.Image;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.impl.RectangleShape;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.gameobjects.SceneGO;
import es.eucm.eadventure.engine.core.gameobjects.TransitionGO;
import es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public class SimpleTransitionGO extends SceneGOImpl implements
		TransitionGO {

	private SceneGO<?> previousSceneGO;
	
	private EAdScene nextEAdScene;
	
	protected SceneGO<?> nextSceneGO;
	
	private RuntimeAsset<Image> background;
	
	private Caption caption;
	
	protected RuntimeAsset<Image> previousSceneImage;
	
	protected EAdBasicSceneElement loadingText;
	
	protected EAdBasicSceneElement screenBlock;
	
	protected boolean loading = false;
	
	protected boolean loaded = false;
	
	@Inject
	public SimpleTransitionGO(AssetHandler assetHandler,
			StringHandler stringHandler, GameObjectFactory gameObjectFactory,
			GUI gui, GameState gameState, ValueMap valueMap,
			PlatformConfiguration platformConfiguration) {
		super(assetHandler, stringHandler, gameObjectFactory, gui, gameState, valueMap,
				platformConfiguration);
		//TODO localize
		EAdString string = new EAdString("Loading");
		caption = new CaptionImpl(string);
		loadingText = new EAdBasicSceneElement("loadingText");
		loadingText.getResources().addAsset(loadingText.getInitialBundle(), EAdBasicSceneElement.appearance, caption);
		loadingText.setPosition(EAdPosition.volatileEAdPosition(750, 550, 1.0f, 1.0f));
		
		//TODO remove static references to 800 and 600
		RectangleShape rs = new RectangleShape(800, 600);
		rs.setColor(new EAdBorderedColor(new EAdColor(100, 100, 100, 30), EAdColor.BLACK));
		
		screenBlock = new EAdBasicSceneElement("screenBlock");
		screenBlock.getResources().addAsset(screenBlock.getInitialBundle(), EAdBasicSceneElement.appearance, rs);
	}
	@Override
	public boolean acceptsVisualEffects() {
		return false;
	}

	@Override
	public void setElement(EAdScene element) {
		// TODO Auto-generated method stub

	}

	@Override
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		return null;
	}
	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.SceneGOImpl#doLayout(int, int)
	 * 
	 * The bloking element and the loading text should never be offset
	 */
	
	@Override
	public void doLayout(int offsetX, int offsetY) {
		gui.addElement(gameObjectFactory.get(screenBlock), 0, 0);
		gui.addElement(gameObjectFactory.get(loadingText), 0, 0);
		if (loaded) {
			gui.addElement(nextSceneGO, 0, 0);
		}
	}

	@Override
	public void setPrevious(SceneGO<?> screen) {
		this.previousSceneGO = screen;
	}

	@Override
	public void setNext(EAdScene screen) {
		this.nextEAdScene = screen;
		if (nextEAdScene == null)
			nextEAdScene = this.gameState.getPreviousScene();
	}

	@Override
	public RuntimeAsset<Image> getBackground() {
		return background;
	}

	@Override
	public void update(GameState gameState) {
		if (!loading) {
			loading = true;
			nextSceneGO = (SceneGO<?>) gameObjectFactory.get(nextEAdScene);

			
			List<RuntimeAsset<?>> newAssetList = nextSceneGO.getAssets(new ArrayList<RuntimeAsset<?>>(), false);

			List<RuntimeAsset<?>> oldAssetList = new ArrayList<RuntimeAsset<?>>();
			oldAssetList = previousSceneGO.getAssets(oldAssetList, true);
			 //unload unnecessary assets
			for (RuntimeAsset<?> asset : oldAssetList) {
				if (asset != null && !newAssetList.contains(asset)) {
					asset.freeMemory();
				}
			}
			System.gc();
			// pre-load minimum required assets
			for (RuntimeAsset<?> asset : newAssetList) {
				if (asset != null && !asset.isLoaded())
					asset.loadAsset();
			}
			loaded = true;
		}
		
		if (previousSceneImage == null) {
			previousSceneImage = gui.commitToImage();
		}
		
		if (loaded) {
			gameState.setScene(nextSceneGO);
			if (previousSceneImage != null)
				previousSceneImage.freeMemory();
		}
	}
	

}
