package es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.EAdList;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.OrientedAsset;
import es.eucm.eadventure.engine.core.EvaluatorFactory;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.guiactions.GUIAction;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public class BasicSceneElementGO extends SceneElementGOImpl<EAdBasicSceneElement> {

	private static final Logger logger = Logger
	.getLogger("BasicSceneElementGOImpl");

	private EvaluatorFactory evaluatorFactory;
	
	@Inject
	public BasicSceneElementGO(AssetHandler assetHandler, ValueMap valueMap, EvaluatorFactory evaluatorFactory) {
		logger.info("New instance");
		this.valueMap = valueMap;
		this.assetHandler = assetHandler;
		this.evaluatorFactory = evaluatorFactory;
	}
	
	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.SceneElementGOImpl#getDraggableElement(es.eucm.eadventure.engine.core.MouseState)
	 */
	public GameObject<?> getDraggableElement(MouseState mouseState) {
		if (evaluatorFactory.evaluate(element.getDraggabe()))
			return this;
		return null;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.SceneElementGOImpl#setElement(es.eucm.eadventure.common.model.elements.EAdSceneElement)
	 */
	public void setElement(EAdBasicSceneElement basicSceneElement) {
		super.setElement(basicSceneElement);
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.SceneElementGOImpl#processAction(es.eucm.eadventure.engine.core.guiactions.GUIAction)
	 */
	@Override
	public boolean processAction(GUIAction action) {
		EAdList<EAdEffect> list = element.getEffects(action.getGUIEvent());
		if (list != null && list.size() > 0) {
			action.consume();
			for (EAdEffect e : list) {
				gameState.addEffect(e);
			}
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.SceneElementGOImpl#update(es.eucm.eadventure.engine.core.GameState)
	 */
	@Override
	public void update(GameState state) {
		super.update(state);
	}

	/* (non-Javadoc)
	 * @see es.eucm.eadventure.engine.core.gameobjects.impl.SceneElementGOImpl#getAsset()
	 */
	@Override
	public DrawableAsset<?> getAsset() {
		AssetDescriptor a = element.getResources().getAsset(getCurrentBundle(), EAdBasicSceneElement.appearance);
 
		if (a instanceof OrientedAsset) {
			a = ((OrientedAsset) a).getAssetDescritpor(orientation);
		}

		DrawableAsset<?> r = (DrawableAsset<?>) assetHandler.getRuntimeAsset(a);
		r.loadAsset();
		if (r instanceof DrawableAsset && r.isLoaded()) {
			setWidth( r.getWidth() );
			setHeight( r.getHeight() );
			return r.getDrawable();
		}
		return r;
	}

	@Override
	public List<EAdAction> getValidActions() {
		//TODO?
		return null;
	}

	@Override
	public List<RuntimeAsset<?>> getAssets(List<RuntimeAsset<?>> assetList,
			boolean allAssets) {

		if (!isVisible() && !allAssets) {
			return assetList;
		}
		
		List<EAdBundleId> bundles = new ArrayList<EAdBundleId>();
		if (allAssets)
			bundles.addAll(getElement().getResources().getBundles());
		else
			bundles.add(getCurrentBundle());
		
		for (EAdBundleId bundle : bundles) {
			AssetDescriptor a = getElement().getResources().getAsset(bundle,EAdBasicSceneElement.appearance);
			 
			if (a instanceof OrientedAsset) {
				if (!allAssets)
					assetList.add(assetHandler.getRuntimeAsset(((OrientedAsset) a).getAssetDescritpor(orientation)));
				else {
					assetList.add(assetHandler.getRuntimeAsset(((OrientedAsset) a).getAssetDescritpor(Orientation.EAST)));
					assetList.add(assetHandler.getRuntimeAsset(((OrientedAsset) a).getAssetDescritpor(Orientation.NORTH)));
					assetList.add(assetHandler.getRuntimeAsset(((OrientedAsset) a).getAssetDescritpor(Orientation.SOUTH)));
					assetList.add(assetHandler.getRuntimeAsset(((OrientedAsset) a).getAssetDescritpor(Orientation.WEST)));
				}
			} else {
				assetList.add(assetHandler.getRuntimeAsset(a));
			}
		}
		return assetList;
	}

}
