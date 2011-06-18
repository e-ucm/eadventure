package es.eucm.eadventure.engine.core.gameobjects.impl;

import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.impl.EAdVideoScene;
import es.eucm.eadventure.common.resources.assets.multimedia.Video;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.gameobjects.SceneGO;
import es.eucm.eadventure.engine.core.platform.SpecialAssetRenderer;

public class VideoSceneGO extends AbstractGameObject<EAdVideoScene> implements SceneGO<EAdVideoScene> {

	private static final Logger logger = Logger.getLogger("VideoScreenGOImpl");

	private SpecialAssetRenderer<Video, ?> specialAssetRenderer;
	
	private Object component;
	
	@Inject
	public VideoSceneGO( SpecialAssetRenderer<Video, ?> specialAssetRenderer ) {
		logger.info("New instance");	
		this.specialAssetRenderer = specialAssetRenderer;
		this.component = null;
	}
	
	public void doLayout(int offsetX, int offsetY) {
		if (component == null)
			component = specialAssetRenderer.getComponent((Video) element.getAsset(EAdVideoScene.video));
		if (specialAssetRenderer.isFinished())
			gui.showSpecialResource(null, 0, 0, true);
		else
			gui.showSpecialResource(component, 0, 0, true);
	}
	
	@Override
	public boolean acceptsVisualEffects() {
		return false;
	}
	
	@Override
	public void update(GameState state) {
		super.update(state);
		if (specialAssetRenderer.isFinished()) {
			for ( EAdEffect e: element.getFinalEffects()){
				gameState.addEffect(e);
			}
		}
	}


	
}
